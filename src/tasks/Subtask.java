package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

//Класс подзадачи
public class Subtask extends Task {

    public Subtask(String name, String description) {
        super(name, description);
        status = StatusOfTask.NEW;
    }


    @Override
    public String toString() {
        return id + "," +"SUBTASK," + name + "," + status + "," + description + "," + duration
                + "," + startTime + "," + getEndTime();
    }

    //Из строки в объект subtask
    public static Subtask fromString(String[]  subtaskInStr) {
        final int id = Integer.parseInt(subtaskInStr[0]);
        final String name = subtaskInStr[2];
        final String description = subtaskInStr[4];
        final String status = subtaskInStr[3];
        final Duration duration = Duration.ofMinutes(Integer.parseInt(subtaskInStr[5]));
        final LocalDateTime startTime = LocalDateTime.parse(subtaskInStr[6]);
        final LocalDateTime endTime = LocalDateTime.parse(subtaskInStr[7]);
        Subtask subtask = new Subtask(name, description); //Элементы в конструктор
        subtask.setId(id);
        subtask.status = StatusOfTask.valueOf(status);
        subtask.duration = duration;
        subtask.startTime = startTime;
        subtask.endTime = endTime;
        return subtask;
    }
}
