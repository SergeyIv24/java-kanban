//Класс подзадачи
public class Subtask extends Task {

    public Subtask(String name, String description, int id) {
        super(name, description, id);
        status = StatusOfTask.NEW;
    }

    @Override
    public String toString() {
        String result = "Subtask{name = '" + name + "\'" + " description = '" + description + "\'" +
                " ID = '" + id + "\'" + " status = \'" + status + "\'}";
        return result;
    }
}
