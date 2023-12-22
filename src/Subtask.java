//Класс подзадачи
public class Subtask extends Task {

    public Subtask(String name, String description, int ID) {
        super(name, description, ID);
    }

    @Override
    public String toString() {
        String result = "Subtask{name = '" + name + "\'" + " description = '" + description + "\'" +
                " ID = '" + ID + "\'" + " status = \'" + status + "\'";
        return result;
    }
}
