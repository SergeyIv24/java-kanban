package manager;

import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    Path file; //Поле файла с данными менеджера

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
    public void save() {

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
    public void addTask(String name, String description) {
        super.addTask(name, description);
        save();
    }

}
