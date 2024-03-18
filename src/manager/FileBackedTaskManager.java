package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    public Path file; //Поле файла с данными менеджера

    public FileBackedTaskManager(Path file) {
        this.file = file;
    }


    //Метод сохранения данных
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {
            for (Integer id : tasksTable.keySet()) {
                writer.write(tasksTable.get(id).toString() + "\n");
            }
            writer.write("\n"); //Пустая строка отделяет обычные задачи
            for (Integer id : epicTable.keySet()) {
                writer.write(epicTable.get(id).toString() + "\n");
            }
            writer.write("\n"); //Пустая строка отделяет эпики
            for (Integer id : subtaskTable.keySet()) {
                writer.write(subtaskTable.get(id).toString() + "\n");
            }
            writer.write("\n"); //Пустая строка отделяет подзадачи

            if (!history.getListOfHistory().isEmpty()) { //Если история не пустая - запись
                writer.write(historyToString(history)); //Запись истории
            }

        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при записи файла");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager backedManager;
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            backedManager = new FileBackedTaskManager(file.toPath());
            while (reader.ready()) {
                line = reader.readLine(); //Чтение строки
                if (line.isEmpty()) { //Если строка пустая, дальнейшая обработка не нужна
                    continue;
                }

                String[] lineArr = line.split(",");
                //Если объект типа Task
                if (lineArr[1].equalsIgnoreCase("TASK")) {
                    Task task = Task.fromString(lineArr); //Строка в объект Task
                    backedManager.getTaskTable().put(task.getId(), task); //В соответствующую мапу
                } else if (lineArr[1].equalsIgnoreCase("EPIC")) {
                    Epic epic = Epic.fromString(lineArr); //Строка в объект Epic
                    backedManager.getEpicTable().put(epic.getId(), epic);
                } else if (lineArr[1].equalsIgnoreCase("SUBTASK")) {
                    Subtask subtask = Subtask.fromString(lineArr); //Строка в объект subtask
                    backedManager.getSubtaskTable().put(subtask.getId(), subtask);
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при загрузке данных");
        }

        //Последняя строка - история, если не пустая
        List<Integer> listForHistory = new ArrayList<>();
        if (!line.isEmpty()) {
            listForHistory = historyFromString(line);
        }

        for (Integer historyId : listForHistory) { //Цикл по массиву строк
            int id = historyId; //Стартовое значение id

            if (backedManager.getEpicTable().containsKey(id)) { //Если id есть в массиве эпиков
                backedManager.getHistory().addTaskInHistory(backedManager.getEpicTable().get(id)); //Добавление в историю
            }

            if (backedManager.getSubtaskTable().containsKey(id)) { //Если id есть в массиве подзадач
                backedManager.getHistory().addTaskInHistory(backedManager.getSubtaskTable().get(id)); //Добавление в историю
            }

            if (backedManager.getTaskTable().containsKey(id)) { //Если id есть в массиве задач
                backedManager.getHistory().addTaskInHistory(backedManager.getTaskTable().get(id)); //Добавление в историю
            }
        }
        return backedManager;
    }

    //Список истории в строку для записи в файл. В конце файла хранятся только id задач истории
    public static String historyToString(HistoryManager manager) {
        String idOfHistory = ""; //Выходная строка
        for (Task task : manager.getListOfHistory()) { //Цикл по элементам истории
            idOfHistory = idOfHistory + task.getId() + ",";
        }
        return idOfHistory;
    }

    //Строка из документа в массив Id
    public static List<Integer> historyFromString(String value) {
        List<Integer> idOfHistory = new ArrayList<>(); //Выходной список id
        String[] valueLikeArray = value.split(","); //В файле id разделены запятой
        for (String id : valueLikeArray) {
            idOfHistory.add(Integer.parseInt(id));
        }
        return idOfHistory;
    }


//Методы для эпиков
    //Создание эпика
    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }


    //После удаления задачи, файл перезаписывается
    @Override
    public String deleteAllEpics() {
        super.deleteAllEpics();
        save();
        return "Все эпики удалены";
    }

    //Удаление эпика по id
    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    //Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    //Получение эпика по id, запись истории, обновление файла
    @Override
    public Optional<Epic> receiveOneEpic(int epicId) {
        Optional<Epic> epic = super.receiveOneEpic(epicId);
        save();
        return epic;
    }



//Методы для подзадач эпиков
    //Создание подзадачи
    @Override
    public void addSubTaskInEpic(int epicId, Subtask subtask) {
        super.addSubTaskInEpic(epicId, subtask);
        save();
    }

    //Удаление всех подзадач эпика
    @Override
    public void deleteAllSubtasksOfEpic(int epicId) {
        super.deleteAllSubtasksOfEpic(epicId);
        save();
    }

    //Удаление подзадачи по идентификатору
    @Override
    public boolean deleteParticularSubtask(int subtaskId) {
        boolean result = super.deleteParticularSubtask(subtaskId);
        save();
        return result;
    }

    //Обновление подзадачи
    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean result = super.updateSubtask(subtask);
        save();
        return result;
    }

    //Получение подзадачи по идентификатору, запись в историю, сохранение в файл
    @Override
    public Optional<Subtask> receiveSubtasksUseID(int subtaskId) {
        Optional<Subtask> subtask = super.receiveSubtasksUseID(subtaskId);
        save();
        return subtask;
    }


//Методы для обычных задач
    //Создание задачи
    @Override
    public boolean addTask(Task task) {
        super.addTask(task);
        save();
        return true;
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    //Удаление по идентификатору
    @Override
    public void deleteUseID(int id) {
        super.deleteUseID(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    //Метод вывода по идентификатору
    @Override
    public Optional<Task> receiveOneTask(int id) {
        Optional<Task> task = super.receiveOneTask(id);
        save();
        return task;
    }

}
