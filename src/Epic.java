import java.util.HashMap;
//Класс эпик
public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks; // Мапа для хранения подзадач

    public Epic(String name, String description, int ID) {
        super(name, description, ID);
        subtasks = new HashMap<>();
        status = StatusOfTask.NEW;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}
