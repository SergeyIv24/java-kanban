package manager;

import java.io.File;

public class Managers {

    //static File file = new File("C:\\Учеба\\Java 2023 - 2024" +
           // "\\Задачи\\Проекты ЯП\\Спринт 4\\java-kanban\\src\\manager\\File.csv");

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getFileBackedTaskManager(File file) {
        return new FileBackedTaskManager(file.toPath());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManage();
    }


}
