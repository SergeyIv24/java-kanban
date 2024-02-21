package tasks;

//Класс подзадачи
public class Subtask extends Task {

    public Subtask(String name, String description, int id) {
        super(name, description, id);
        status = StatusOfTask.NEW;
    }


    @Override
    public String toString() {
        return id + "," +"SUBTASK," + name + "," + status + "," + description + ",";
    }

    //Из строки в объект subtask
    public static Subtask fromString(String[]  subtaskInStr) {
        final int id = Integer.parseInt(subtaskInStr[0]);
        final String name = subtaskInStr[2];
        final String description = subtaskInStr[4];
        final String status = subtaskInStr[3];
        Subtask subtask = new Subtask(name, description, id); //Элементы в конструктор
        subtask.status = StatusOfTask.valueOf(status);
        return subtask;
    }
}
