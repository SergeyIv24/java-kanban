package tasks;

import java.util.ArrayList;
import java.util.HashMap;
//Класс эпик
public class Epic extends Task {
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
