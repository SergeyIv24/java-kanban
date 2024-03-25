package tasks; //Отдельный пакет для всех классов задач

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

//Обычная задача
public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected StatusOfTask status;
    protected Duration duration; //Продолжительность в минутах
    protected LocalDateTime startTime; //Время начала задачи
    protected LocalDateTime endTime;

    //Конструктор без продолжительности и времени начала
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = StatusOfTask.NEW; //Как только задача создана, она новая.
    }

    //Конструктор с продолжительностью и временем начала
    public Task(String name, String description, long minutes, String startTime) {
        this.name = name;
        this.description = description;
        this.status = StatusOfTask.NEW; //Как только задача создана, она новая.
        duration = Duration.ofMinutes(minutes);
        this.startTime = LocalDateTime.parse(startTime, Constants.FORMATTER);
    }

    public LocalDateTime getStartTime() {
        return startTime;
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
    public LocalDateTime getEndTime() {
        if (startTime == null | duration == null) return null;
        endTime = startTime.plus(duration);
        return endTime;
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
        return id + "," + "TASK," + name + "," + status + "," + description + ","
                + ((duration != null) ? duration.toMinutes() : duration) + ","
                + ((startTime != null) ? startTime.format(Constants.FORMATTER) + "," + getEndTime().format(Constants.FORMATTER)
                : startTime + "," + getEndTime());

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

        if (taskInStr[5].equals("null")) {
            return task;
        }

        final Duration duration = Duration.ofMinutes(Long.parseLong(taskInStr[5]));
        final LocalDateTime startTime = LocalDateTime.parse(taskInStr[6], Constants.FORMATTER);
        final LocalDateTime endTime = LocalDateTime.parse(taskInStr[7], Constants.FORMATTER);

        task.duration = duration;
        task.startTime = startTime;
        task.endTime = endTime;
        return task;
    }


}
