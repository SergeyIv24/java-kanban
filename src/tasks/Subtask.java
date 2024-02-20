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
    public static Subtask fromString(String[]  value) {
        //String[] value = value.split(","); //Строку в массив по разделителю
        Subtask subtask = new Subtask(value[2], value[4], Integer.parseInt(value[0])); //Элементы в конструктор
        //Для сохранения статуса задач
        switch (value[3]) {
            case "IN_PROGRESS": //Если элемент массива IN_PROGRESS
                subtask.status = StatusOfTask.IN_PROGRESS; //Обновление статуса
                break;
            case "DONE":
                subtask.status = StatusOfTask.DONE;
                break;
        }
        return subtask;
    }
}
