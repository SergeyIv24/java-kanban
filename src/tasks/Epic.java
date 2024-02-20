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
    public static Epic fromString(String[]  value) {
        //String[] value = value.split(","); //Строку в массив по разделителю
        Epic epic = new Epic(value[2], value[4], Integer.parseInt(value[0])); //Элементы в конструктор
        //Для сохранения статуса задач
        switch (value[3]) {
            case "IN_PROGRESS": //Если элемент массива IN_PROGRESS
                epic.status = StatusOfTask.IN_PROGRESS; //Обновление статуса
                break;
            case "DONE":
                epic.status = StatusOfTask.DONE;
                break;
        }
        return epic;
    }



}
