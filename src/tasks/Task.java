package tasks; //Отдельный пакет для всех классов задач

import java.util.Objects;
//Обычная задача
public class Task {
    protected String name;
    protected String description;
    protected final int id;
    protected StatusOfTask status;
    //Todo добавить Duration min
    //Todo LocalDateTime startTime - время начала задачи
    //Todo метод getEndTime() - время окончания задачи startTime + duration
    //Todo Конструктор принимающий duration и startTime

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
        status = StatusOfTask.NEW; //Как только задача создана, она новая.
    }

    public int getId() {
        return id;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public void setStatus(String newStatus) {
        StatusOfTask statusStrToEnum = StatusOfTask.valueOf(newStatus);
        status = statusStrToEnum;
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
        Task task = new Task(name, description, id); //Элементы в конструктор
        task.status = StatusOfTask.valueOf(status);
        return task;
    }


}
