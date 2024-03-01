package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

//Класс эпик
public class Epic extends Task {
    private LocalDateTime epicStartTime;
    private Duration epicDuration;
    private LocalDateTime epicEndTime;


    private ArrayList<Subtask> subtasks;

    //Todo расчет поля startTime по первой задаче в списке +
    //Todo Duration по продолжительностям каждый подзадачи из списка +
    //Todo рассчитать поле endTime по последней задаче в списке +
    //Todo Конструктор принимающий duration и startTime


    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    //Расчет временных полей
    public void solveStartTimeAndDuration() {
        subtasks.stream()
                .filter(subtask -> subtask.duration != null) //Если продолжительности нет, то задача не интересна
                .forEach(subtask -> epicDuration = duration.plus(subtask.duration)); //Прибавить к epicDuration
                                                                                    //durations subtasks
       startTime = subtasks.stream().min((sub1, sub2) -> { //Минимальная дата начала подзадачи
           return sub1.startTime.compareTo(sub2.startTime);
       }).get().startTime;

        if (epicStartTime != null) {
            epicEndTime = epicStartTime.plus(duration); //Время окончания = время начала первой задачи + общая продолжительность
        }

    }

    @Override
    public String toString() {
        return id + "," + "EPIC," + name + "," + status + "," + description + "," + description + "," + epicDuration
                + "," + epicStartTime + "," + epicEndTime;
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
        return epic;
    }



}
