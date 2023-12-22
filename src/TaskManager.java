import java.util.HashMap;

public class TaskManager {
    int counterTaskID = 0; //Счетчик ID обычных задач
    int counterEpicID = 0; //Счетчик ID эпиков
    int counterSubtaskID = 0; //Счетчик ID подзадач эпиков
    HashMap<Integer, Task> tasksTable; //Объявление мапы для обычных задач
    HashMap<Integer, Epic> epicTable; //Объявление мапы для эпиков

    public TaskManager() {
        tasksTable = new HashMap<>(); // Инициализация мап
        epicTable = new HashMap<>();
    }

//Методы для эпиков
//Модификаторы protected, так как нет необходимости использовать методы в других пакетах
    //Создание эпика
    protected void createEpic(String name, String description) {
        counterEpicID += 1; //ID считается с 1
        Epic epic = new Epic(name, description, counterEpicID);
        epicTable.put(counterEpicID, epic); //Эпик в мапу эпиков
    }

    //Получение всех эпиков
    protected String printAllEpics() {
        return "Эпики " + epicTable.toString(); //Переопределенный toString класса Epic
    }


    //Получение эпика по идентификатору
    protected String printOneEpic(int epicID){
        if (epicTable.containsKey(epicID)) { //Проверка наличия ключа в мапе
            String taskByID = epicTable.get(epicID).toString(); //Ключ найден, вывод
            return taskByID;
        } else {
            return ""; //Ключ не найден, то пустая строка
        }
    }

    //Удаление всех эпиков
    protected void deleteAllEpics() {
        epicTable.clear();
    }

    //Удаление эпика по идентификатору
    protected void deleteEpic(int epicID) {
        if (epicTable.containsKey(epicID)) { //Если эпик в мапе
            epicTable.remove(epicID, epicTable.get(epicID));
        } else { //Если эпика нет в мапе, метод завершается
            return;
        }
    }

    //TODO Обновление эпика


//Методы для подзадач эпиков

    //Добавление подзадачи в эпик
    protected void addSubTaskInEpic(int epicID, String name, String description) {
        counterSubtaskID += 1;
        Subtask subtask = new Subtask(name, description, counterSubtaskID);
        Epic epic = epicTable.get(epicID);
        epic.getSubtasks().put(counterSubtaskID, subtask);
    }

    //TODO Вывод подзадач эпика


    //Todo Вывод подзадачи по идентификатору
    protected String printSubtasksUseID(int epicID, int subtaskID) { //Чтобы найти подзадачу в эпике, нужно найти
        Epic epic = epicTable.get(epicID);                          //сам эпик
        if (epic.getSubtasks().containsKey(subtaskID)) { //Проверка наличия ключа
            String particularSubtask = epic.getSubtasks().get(subtaskID).toString(); //Получение значение мапы
            return particularSubtask;
        } else {
            return ""; //Не найдено - пустая строка
        }
    }

    //Todo Удаление всех подзадач

    //Todo Удаление подзадачи по идентификатору
    protected void removeParticularSubtask(int epicID, int subtaskID) {
        Epic epic = epicTable.get(epicID);
        if (epic.getSubtasks().containsKey(subtaskID)) {
            epic.getSubtasks().remove(subtaskID);
        } else {
            return;
        }
    }

    //Todo Обновление подзадачи по идентификатору

    //Todo Все подзадачи эпика
    protected String printAllSubtasksOfEpic(int epicID) {
        Epic epic = epicTable.get(epicID);
        return epic.getSubtasks().toString();
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
