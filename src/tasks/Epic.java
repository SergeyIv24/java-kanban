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
        return id + "," + "EPIC," + name + "," + status + "," + description + ",";
    }

    //Из строки в объект Epic
    public static Epic fromString(String[]  epicInStr) {
        final int id = Integer.parseInt(epicInStr[0]);
        final String name = epicInStr[2];
        final String description = epicInStr[4];
        final String status = epicInStr[3];
        Epic epic = new Epic(name, description, id); //Элементы в конструктор
        epic.status = StatusOfTask.valueOf(status);
        return epic;
    }



}
