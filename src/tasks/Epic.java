package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

//Класс эпик
public class Epic extends Task {
    private LocalDateTime epicStartTime;
    private Duration epicDuration;
    private LocalDateTime epicEndTime;


    private ArrayList<Subtask> subtasks;


    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    //Расчет временных полей
    public void solveStartTimeAndDuration() {
       if (subtasks.isEmpty()) {
            epicDuration = null;
            epicStartTime = null;
            epicEndTime = null;
            return;
        }
        epicDuration = Duration.ofMinutes(0);
        subtasks.stream()
                .filter(subtask -> subtask.duration != null) //Если продолжительности нет, то задача не интересна
                .forEach(subtask -> epicDuration = epicDuration.plus(subtask.duration)); //Прибавить к epicDuration
                                                                                    //durations subtasks
       epicStartTime = subtasks.stream().min(Comparator.comparing((Subtask sub) -> sub.startTime)).get().startTime;

        if (epicStartTime != null) {
            epicEndTime = epicStartTime.plus(epicDuration); //Время окончания = время начала первой задачи + общая продолжительность
        }

    }

    @Override
    public String toString() {
        return id + "," + "EPIC," + name + "," + status + "," + description  + ","
                + ((epicDuration != null) ? epicDuration.toMinutes() : epicDuration)
                + "," + ((epicStartTime != null) ? epicStartTime.format(Constants.FORMATTER) +
                 "," + epicEndTime.format(Constants.FORMATTER)
                : epicStartTime + "," + epicEndTime);
    }

    //Из строки в объект Epic
    public static Epic fromString(String[]  epicInStr) {
        final int id = Integer.parseInt(epicInStr[0]);
        final String name = epicInStr[2];
        final String description = epicInStr[4];
        final String status = epicInStr[3];
        Epic epic = new Epic(name, description); //Элементы в конструктор
        epic.setId(id);
        epic.status = StatusOfTask.valueOf(status);

        if (epicInStr[5].equals("null") | epicInStr[6].equals("null")) {
            return epic;
        }

        final Duration duration = Duration.ofMinutes(Long.parseLong(epicInStr[5]));
        final LocalDateTime startTime = LocalDateTime.parse(epicInStr[6], Constants.FORMATTER);
        final LocalDateTime endTime = LocalDateTime.parse(epicInStr[7], Constants.FORMATTER);

        epic.epicDuration = duration;
        epic.epicStartTime = startTime;
        epic.epicEndTime = endTime;
        return epic;
    }



}
