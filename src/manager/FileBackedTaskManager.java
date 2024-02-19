package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    Path file; //Поле файла с данными менеджера
    //private final HistoryManager history = Managers.getDefaultHistory();

    public FileBackedTaskManager() {
        Path newFile = Paths.get(
                "C:\\Учеба\\Java 2023 - 2024" +
                        "\\Задачи\\Проекты ЯП\\Спринт 4\\java-kanban\\src\\manager\\File.csv");
        //Если файл не существует
        if (!Files.exists(newFile)) {
            try {
                file = Files.createFile(newFile); //Создание файла
            } catch (IOException exception) {
                throw new ManagerSaveException("Ошибка создания файла");
            }

        }
        file = newFile; //Присвоение в поле класса
    }

    //Метод сохранения данных
    public void save(){
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

    public static FileBackedTaskManager loadFromFile(File file){
        FileBackedTaskManager backedManager;
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            backedManager = new FileBackedTaskManager();
            while (reader.ready()) {
                line = reader.readLine(); //Чтение строки
                if (line.isEmpty()) { //Если строка пустая, дальнейшая обработка не нужна
                    continue;
                }

                String idStr = ""; //Чтобы из строки получить id, если в нем больше  цифры
                for (int i = 0; i <= 3; i++) {
                    if (Character.isDigit(line.charAt(i))) {
                        idStr = idStr + line.charAt(i);
                    } else {
                        break;
                    }
                }
                int id = Character.getNumericValue(idStr.charAt(0)); //Получение id

                Task task = Task.fromString(line); //Строка в объект Task

                //Если объект типа Task
                if (task.getClass() == Task.class) {
                    backedManager.getTaskTable().put(id, task); //В соответствующую мапу
                } else if (task.getClass() == Epic.class) {
                    backedManager.getEpicTable().put(id, (Epic) task);
                } else {
                    backedManager.getSubtaskTable().put(id, (Subtask) task);
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при загрузке данных");
        }

        //Последняя строка - история, если не пустая
        String[] arrForHistory = new String[0];
        if (!line.isEmpty()) {
            arrForHistory = line.split(","); // История в массив строк
        }

        for (int i = 0; i < arrForHistory.length; i++) { //Цикл по массиву строк
            int id = -1; //Стартовое значение id
            id = Integer.parseInt(arrForHistory[i]); // id String в int
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
    public void createEpic(String name, String description) {
        super.createEpic(name, description);
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
    public Epic receiveOneEpic(int epicId) {
        Epic epic = super.receiveOneEpic(epicId);
        save();
        return epic;
    }



//Методы для подзадач эпиков
    //Создание подзадачи
    @Override
    public void addSubTaskInEpic(int epicId, String name, String description) {
        super.addSubTaskInEpic(epicId, name, description);
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
    public Subtask receiveSubtasksUseID(int subtaskId) {
        Subtask subtask = super.receiveSubtasksUseID(subtaskId);
        save();
        return subtask;
    }


//Методы для обычных задач
    //Создание задачи
    @Override
    public void addTask(String name, String description){
        super.addTask(name, description);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    //Удаление по идентификатору
    @Override
    public void deleteUseID(int ID) {
        super.deleteUseID(ID);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    //Метод вывода по идентификатору
    @Override
    public Task receiveOneTask(int id) {
        Task task = super.receiveOneTask(id);
        save();
        return task;
    }

}
