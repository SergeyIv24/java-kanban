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
    private final HistoryManager history = Managers.getDefaultHistory();

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
            try (FileWriter writer = new FileWriter(newFile.toFile())) { //Заполнение названием колонок
                writer.write("id,type,name,status,description,epic");
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

            writer.write(historyToString(history)); //Запись истории
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при записи файла");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file){
        FileBackedTaskManager backedManager;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            backedManager = new FileBackedTaskManager();
            while (reader.ready()) {
                String line = reader.readLine(); //Чтение строки
                int id = Character.getNumericValue(line.charAt(0)); //Получение id
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

        return backedManager;
    }

    //Список истории в строку для записи в файл. В конце файла хранятся только id задач истории
    public static String historyToString(HistoryManager manager) {
        String idOfHistory = ""; //Выходная строка
        for (Task task : manager.getListOfHistory()) { //Цикл по элементам истории
            idOfHistory += task.getId() + ",";
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



//Методы для подзадач эпиков
    //Создание подзадачи
    @Override
    public void addSubTaskInEpic(int epicId, String name, String description) {
        super.addSubTaskInEpic(epicId, name, description);
        save();
    }




//Методы для обычных задач
    //Создание задачи
    @Override
    public void addTask(String name, String description){
        super.addTask(name, description);
        save();
    }

}
