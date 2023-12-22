import java.util.Objects;
import java.util.Random;
//Обычная задача
public class Task {
    Random rdn = new Random();
    private String name;
    private String description;
    private final int ID;
    protected StatusOfTask status;

    public Task(String name, String description, int ID) {
        this.name = name;
        this.description = description;
        this.ID = ID;
        //ID = rdn.nextInt(100);
        status = StatusOfTask.NEW;

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
    public final String toString() {
        String allTasks = "Ваши задачи: " + name + ", описание " + description;
        return allTasks;
    }




}
