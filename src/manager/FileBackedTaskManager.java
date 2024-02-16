package manager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
