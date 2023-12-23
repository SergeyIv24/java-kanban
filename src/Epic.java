import java.util.HashMap;
//Класс эпик
public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks; // Мапа для хранения подзадач, так каждый эпик знает свои подзадачи

    public Epic(String name, String description, int ID) {
        super(name, description, ID);
        subtasks = new HashMap<>();
        status = StatusOfTask.NEW;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        String result = "Epic{name = '" + name + "\'" + " description = '" + description + "\'" +
                " ID = '" + ID + "\'" + " status = \'" + status + "\'}";
        return result;
    }

}
