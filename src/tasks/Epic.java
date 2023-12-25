import java.util.ArrayList;
import java.util.HashMap;
//Класс эпик
public class Epic extends Task {
    //private HashMap<Integer, Subtask> subtasks; // Мапа для хранения подзадач, так каждый эпик знает свои подзадачи
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, int id) {
        super(name, description, id);
        subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        String result = "Epic{name = '" + name + "\'" + " description = '" + description + "\'" +
                " ID = '" + id + "\'" + " status = \'" + status + "\'}";
        return result;
    }

}
