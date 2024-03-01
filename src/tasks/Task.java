package tasks; //Отдельный пакет для всех классов задач

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

//Обычная задача
public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected StatusOfTask status;
    protected Duration duration; //Продолжительность в минутах
    protected LocalDateTime startTime; //Время начала задачи
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy, HH:mm"); //Формат для даты и времени

    //Todo добавить Duration min +
    //Todo LocalDateTime startTime - время начала задачи +
    //Todo метод getEndTime() - время окончания задачи startTime + duration +
    //Todo Конструктор принимающий duration и startTime +

    //Конструктор без продолжительности и времени начала
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        //this.id = id;
        status = StatusOfTask.NEW; //Как только задача создана, она новая.
    }

    //Конструктор с продолжительностью и временем начала
    public Task(String name, String description, int id, long minutes, String startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        status = StatusOfTask.NEW; //Как только задача создана, она новая.
        duration = Duration.ofMinutes(minutes);
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public void setStatus(String newStatus) {
        StatusOfTask statusStrToEnum = StatusOfTask.valueOf(newStatus);
        status = statusStrToEnum;
    }

    //Метод определения окончания задачи по началу и продолжительности
    public LocalDateTime getEndTime(){ //Todo как использовать форматер?
        return startTime.plus(duration);
    }

    @Override //Переопределяем в суперклассе, чтобы все другие унаследовали полностью готовый метод
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task anotherTask = (Task) obj;
        return Objects.equals(id, anotherTask.id);
    }

    @Override //Переопределяем в суперклассе, чтобы все другие унаследовали полностью готовый метод
    public final int hashCode() {
        int hash = 2;

        if (id != 0) {
            hash = hash * 31 + id;

        }
        return hash;
    }


    @Override
    public String toString() {
        return id + "," + "TASK," + name + "," + status + "," + description + ",";
    }

    //Из строки в объект Task
    public static Task fromString(String[]  taskInStr) {
        final int id = Integer.parseInt(taskInStr[0]);
        final String name = taskInStr[2];
        final String description = taskInStr[4];
        final String status = taskInStr[3];
        Task task = new Task(name, description); //Элементы в конструктор
        task.setId(id);
        task.status = StatusOfTask.valueOf(status);
        return task;
    }


}
