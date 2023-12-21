
//Класс эпик
public class Epic extends Task {


    public Epic(String name, String description) {
        super(name, description);
        status = StatusOfTask.NEW;
    }
}
