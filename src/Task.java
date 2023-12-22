import java.util.Objects;
//Обычная задача
public class Task {
    protected String name;
    protected String description;
    protected final int ID;
    protected StatusOfTask status;

    public Task(String name, String description, int ID) {
        this.name = name;
        this.description = description;
        this.ID = ID;
        status = StatusOfTask.NEW; //Как только задача создана, она новая.

    }

    public int getID() {
        return ID;
    }

    @Override //Переопределяем в суперклассе, чтобы все другие унаследовали полностью готовый метод
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task anotherTask = (Task) obj;
        return Objects.equals(ID, anotherTask.ID);
    }

    @Override //Переопределяем в суперклассе, чтобы все другие унаследовали полностью готовый метод
    public final int hashCode() {
        int hash = 10;
        if (ID != 0) {
            hash = hash * 20;
        }
        return hash;
    }

    @Override
    public String toString() {
        String result = "Task{name = '" + name + "\'" + " description = '" + description + "\'" +
                 " ID = '" + ID + "\'" + " status = \'" + status + "\'";
        return result;
    }

}
