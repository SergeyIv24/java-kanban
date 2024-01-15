package manager;

public class Managers {

    public InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManage();
    }


}
