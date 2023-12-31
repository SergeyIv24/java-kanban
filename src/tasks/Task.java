package tasks; //Отдельный пакет для всех классов задач

import java.util.Objects;
//Обычная задача
public class Task {
    protected String name;
    protected String description;
    protected final int id;
    protected StatusOfTask status;

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
        int hash = 10;
        if (id != 0) {
            hash = hash * 20;
        }
        return hash;
    }

    @Override
    public String toString() {
        String result = "Task{name = '" + name + "\'" + " description = '" + description + "\'" +
                 " ID = '" + id + "\'" + " status = \'" + status + "\'}";
        return result;
    }

}
