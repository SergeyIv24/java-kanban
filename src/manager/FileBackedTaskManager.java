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

    public FileBackedTaskManager() throws IOException {
        Path newFile = Paths.get(
                "C:\\Учеба\\Java 2023 - 2024" +
                        "\\Задачи\\Проекты ЯП\\Спринт 4\\java-kanban\\src\\manager\\File.csv");
        //Если файл не существует
        if (!Files.exists(newFile)) {
            file = Files.createFile(newFile); //Создание файла
            try (FileWriter writer = new FileWriter(newFile.toFile())) { //Заполнение названием колонок
                writer.write("id,type,name,status,description,epic");
            } //todo catch??

        }
        file = newFile; //Присвоение в поле класса
    }

    //Метод сохранения данных
    public void save() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()));
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

    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager backedManager = new FileBackedTaskManager();
        BufferedReader reader = new BufferedReader(new FileReader(file));
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
    public void createEpic(String name, String description) throws IOException {
        super.createEpic(name, description);
        save();
    }



//Методы для подзадач эпиков
    //Создание подзадачи
    @Override
    public void addSubTaskInEpic(int epicId, String name, String description) throws IOException {
        super.addSubTaskInEpic(epicId, name, description);
        save();
    }




//Методы для обычных задач
    //Создание задачи
    @Override
    public void addTask(String name, String description) throws IOException {
        super.addTask(name, description);
        save();
    }

}
