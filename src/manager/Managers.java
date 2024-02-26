package manager;

import java.io.File;

public class Managers {


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
