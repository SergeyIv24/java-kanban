import java.util.HashMap;

public class TaskManager {
    int counterTaskID = 0;
    int counterEpicID = 0;
    int counterSubtaskID = 0;
    HashMap<Integer, Task> tasksTable; //Объявление мапы для обычных задач
    HashMap<Integer, Epic> epicTable; //Объявление мапы для эпиков

    public TaskManager() {
        tasksTable = new HashMap<>(); // Инициализация мап
        epicTable = new HashMap<>();
    }

//Методы для эпиков

    //Создание эпика
    void createEpic(String name, String description) {
        counterEpicID += 1;
        Epic epic = new Epic(name, description, counterEpicID);
        epicTable.put(counterEpicID, epic);
    }

    //Получение всех эпиков
    String printAllEpics() {
        return "Ваши эпики " + epicTable.toString();
    }


    //Получение эпика по идентификатору
    String printOneEpic(int epicID){
        if (epicTable.containsKey(epicID)) {
            String taskByID = epicTable.get(epicID).toString();
            return taskByID;
        } else {
            return "None";
        }
    }

    //Удаление всех эпиков
    void deleteAllEpics() {
        epicTable.clear();
    }

    //TODO Удаление эпика по идентификатору
    void deleteEpic(int epicID) {
        if (epicTable.containsKey(epicID)) {
            epicTable.remove(epicID, epicTable.get(epicID));
        } else {

        }

    }




    //TODO Обновление эпика


//Методы для подзадач эпиков

    void addSubTaskInEpic(int epicID, String name, String description) {
        counterSubtaskID += 1;
        Subtask subtask = new Subtask(name, description, counterSubtaskID);
        Epic epic = epicTable.get(epicID);
        epic.getSubtasks().put(counterSubtaskID, subtask);
    }






// Методы для простых задач.

    //Метод добавления простой задачи
    void addTask(String name, String description) {
        counterTaskID += 1;
        Task task = new Task(name, description, counterTaskID);
        tasksTable.put(task.getID(), task);
    }

    //Метод удаление всех задач
    void deleteAllTask() {
        tasksTable.clear();
    }

    //TODO Удаление по идентификатору
    void deleteUseID(int ID) {
        if (tasksTable.containsKey(ID)) {
            tasksTable.remove(ID, tasksTable.get(ID));
        } else {

        }

    }

    //Метод вывода всех задач
    String printAllTasks(){
        return "Список ваших задач " + tasksTable.toString();
    }

    //Метод вывода по идентификатору
    String printOneTask(int ID) {
        if (tasksTable.containsKey(ID)) {
            String taskByID = tasksTable.get(ID).toString();
            return taskByID;
        } else {
            return "None";
        }

    }

    //Обновление задачи
    void refreshTask(String name, String description, int ID) {
        if (tasksTable.containsKey(ID)) {
            tasksTable.put(ID, new Task(name, description, ID));
        }
    }



}
