import java.util.Objects;
//Обычная задача
public class Task {
    private String name;
    private String description;
    private int ID;
    protected StatusOfTask status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = StatusOfTask.NEW;
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
    public final int hashCode()  {
        int hash = 2;
        if (ID != 0) {
            hash = hash * 31;
        }
        return hash;
    }




}
