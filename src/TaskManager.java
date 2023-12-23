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
        Epic epic = new Epic(name, description, counterEpicID); //Создание объектов
        epicTable.put(counterEpicID, epic); //Эпик в мапу эпиков
    }

    //Получение всех эпиков
    protected String printAllEpics() {
        return epicTable.toString(); //Переопределенный toString класса Epic
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
        }
    }


    //Обновление эпика TODO смена статуса
    protected void refreshEpic(int epicID) {
        if (epicTable.containsKey(epicID)) {
            Epic epic = epicTable.get(epicID);
            Boolean checkStatusNew = false;
            Boolean checkStatusDone = false;
            for (Integer subtaskID : epic.getSubtasks().keySet()) {
                if ((epic.getSubtasks().get(subtaskID).status != StatusOfTask.NEW) &&
                    (epic.getSubtasks().get(subtaskID).status != StatusOfTask.DONE)) {
                    epic.status = StatusOfTask.IN_PROGRESS;
                    return;
                } if (StatusOfTask.DONE == epic.getSubtasks().get(subtaskID).status) {
                    checkStatusDone = true;
                } if (StatusOfTask.NEW == epic.getSubtasks().get(subtaskID).status) {
                    checkStatusNew = true;
                }
            }
            if (checkStatusDone) {
                epic.status = StatusOfTask.DONE;
            } if (checkStatusNew) {
                epic.status = StatusOfTask.NEW;
            }
        }
    }

//Методы для подзадач эпиков

    //Добавление подзадачи в эпик
    protected void addSubTaskInEpic(int epicID, String name, String description) {
        counterSubtaskID += 1; //Счет с 1
        Subtask subtask = new Subtask(name, description, counterSubtaskID); //Создание объекта
        Epic epic = epicTable.get(epicID); //Получение объекта эпика по ID
        epic.getSubtasks().put(counterSubtaskID, subtask); //Новый объект в мапу подзадач
    }

    //TODO Вывод подзадач эпика

    //Todo Вывод подзадачи по идентификатору ВОЗМОЖЕН NULL
    protected String printSubtasksUseID(int epicID, int subtaskID) { //Чтобы найти подзадачу в эпике, нужно найти
        Epic epic = epicTable.get(epicID);                          //сам эпик
        if (epic.getSubtasks().containsKey(subtaskID)) { //Проверка наличия ключа
            String particularSubtask = epic.getSubtasks().get(subtaskID).toString(); //Получение значение мапы
            return particularSubtask;
        } else {
            return ""; //Не найдено - пустая строка
        }
    }

    //Todo Удаление всех подзадач одного эпика
    protected void deleteAllSubtasksOfEpic(int epicID) {
        Epic epic = epicTable.get(epicID);
        epic.getSubtasks().clear();
    }


    //Todo Удаление подзадачи по идентификатору
    protected void removeParticularSubtask(int epicID, int subtaskID) {
        Epic epic = epicTable.get(epicID);
        if (epic.getSubtasks().containsKey(subtaskID)) {
            epic.getSubtasks().remove(subtaskID);
        }
    }

    //Обновление подзадачи по идентификатору, смена статуса
    protected void updateSubtask(int epicID, int subtaskID, StatusOfTask status) {
        Epic epic = epicTable.get(epicID);
        if (epic.getSubtasks().containsKey(subtaskID)) {
            epic.getSubtasks().get(subtaskID).status = status;
        }
        refreshEpic(epicID);
    }


    //Todo Все подзадачи эпика ВОЗМОЖЕН NULL
    protected String printAllSubtasksOfEpic(int epicID) {
        Epic epic = epicTable.get(epicID);
        return epic.getSubtasks().toString();
    }


// Методы для простых задач.

    //Метод добавления простой задачи
    protected void addTask(String name, String description) {
        counterTaskID += 1;
        Task task = new Task(name, description, counterTaskID);
        tasksTable.put(task.getID(), task);
    }

    //Метод удаление всех задач
    protected void deleteAllTask() {
        tasksTable.clear();
    }

    //Удаление по идентификатору
    protected void deleteUseID(int ID) {
        if (tasksTable.containsKey(ID)) {
            tasksTable.remove(ID, tasksTable.get(ID));
        }
    }

    //Метод вывода всех задач
    protected String printAllTasks(){
        return tasksTable.toString();
    }

    //Метод вывода по идентификатору
    protected String printOneTask(int ID) {
        if (tasksTable.containsKey(ID)) {
            String taskByID = tasksTable.get(ID).toString();
            return taskByID;
        } else {
            return "";
        }

    }

    //Обновление задачи
    protected void updateTask(int taskID, StatusOfTask status) {
        if (tasksTable.containsKey(taskID)) {
            Task task = tasksTable.get(taskID);
            tasksTable.put(taskID, task);
            task.status = status;
        }
    }

}
