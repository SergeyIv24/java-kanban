package manager; //отдельный пакет для менеджера

import tasks.*; //Импорт всех классов из пакета tasks
import java.util.HashMap;

public class TaskManager {
    private int counter = 0;
    private HashMap<Integer, Task> tasksTable; //Объявление мапы для обычных задач
    private HashMap<Integer, Epic> epicTable; //Объявление мапы для эпиков
    private HashMap<Integer, Subtask> subtaskTable; //Объявление мапы для отдельного хранения подзадач

    public TaskManager() {
        tasksTable = new HashMap<>(); // Инициализация мап
        epicTable = new HashMap<>();
        subtaskTable = new HashMap<>();
    }

//Методы для эпиков

    //Создание эпика
    public void createEpic(String name, String description) {
        counter += 1; //ID считается с 1
        Epic epic = new Epic(name, description, counter); //Создание объектов
        epicTable.put(counter, epic); //Эпик в мапу эпиков
    }

    //Получение эпика по идентификатору
    public Epic printOneEpic(int epicID){
        if (epicTable.containsKey(epicID)) { //Проверка наличия ключа в мапе
            return epicTable.get(epicID);
        }
        return null; //TODO не уверен
    }

    //Удаление всех эпиков
    public String deleteAllEpics() {
        epicTable.clear();
        return "Все эпики удалены";
    }

    //Удаление эпика по идентификатору
    public void deleteEpic(int epicID) {
        if (epicTable.containsKey(epicID)) { //Если эпик в мапе
            epicTable.remove(epicID, epicTable.get(epicID));
        }
    }

    public String printAllEpics() {
        return epicTable.toString(); //Переопределенный toString класса Epic
    }


    //Обновление эпика
    public void refreshEpic(int epicID) {
        if (epicTable.containsKey(epicID)) {
            Epic epic = epicTable.get(epicID);
            int amountOfNew = 0; //Счет количества элементов со статусом NEW
            int amountOfDone = 0; //Счет количества элементов со статусом DONE
            for (Integer subtaskID : epic.getSubtasks().keySet()) {
                if (StatusOfTask.NEW == epic.getSubtasks().get(subtaskID).status) {
                    amountOfNew += 1;
                }
                if (StatusOfTask.DONE == epic.getSubtasks().get(subtaskID).status) {
                    amountOfDone += 1;
                }
            }

            if (amountOfNew == epic.getSubtasks().size()) { //Если все элементы NEW
                epic.status = StatusOfTask.NEW;
            } else if (amountOfDone == epic.getSubtasks().size()) { //Если все элементы Done
                epic.status = StatusOfTask.DONE;
            } else {
                epic.status = StatusOfTask.IN_PROGRESS;
            }
        }
    }

//Методы для подзадач эпиков

    //Добавление подзадачи в эпик
    public void addSubTaskInEpic(int epicID, String name, String description) {
        counter += 1;
        Subtask subtask = new Subtask(name, description, counter); //Создание объекта подзадачи
        Epic epic = epicTable.get(epicID); // Получение объекта эпика по ID
        if (epic != null) {
            epic.getSubtasks().add(subtask); // Подзадач в список подзадач эпика
            subtaskTable.put(counter, subtask); // Подазадча в мапу подзадач
        }

    }

    //Вывод подзадачи по идентификатору
    public Subtask printSubtasksUseID(int subtaskID) {
        if (subtaskTable.containsKey(subtaskID)) {
            return subtaskTable.get(subtaskID);
        }
        return null;
    }


    //Удаление всех подзадач одного эпика //TODO не удаляет из мапы, только из списка, нужно ли?
    public void deleteAllSubtasksOfEpic(int epicId) {
        Epic epic = epicTable.get(epicId);
        epic.getSubtasks().clear();
    }


    //Удаление подзадачи по идентификатору
    public void removeParticularSubtask(int subtaskId) {
        if (subtaskTable.containsKey(subtaskId)) {
            subtaskTable.remove(subtaskId);
        }
    }

    //Обновление подзадачи по идентификатору, смена статуса
    public void updateSubtask(int epicID, int subtaskID, String status) {
        StatusOfTask status1 = StatusOfTask.valueOf(status);
        Epic epic = epicTable.get(epicID);
        if (epic.getSubtasks().containsKey(subtaskID)) {
            epic.getSubtasks().get(subtaskID).status = status1;
        }
        refreshEpic(epicID);
    }


    //Все подзадачи эпика
    public String printAllSubtasksOfEpic(int epicID) {
        Epic epic = epicTable.get(epicID);
        if (epic == null) return "";
        return epic.getSubtasks().toString();
    }


// Методы для простых задач.

    //Метод добавления простой задачи
    public void addTask(String name, String description) {
        counter += 1;
        Task task = new Task(name, description, counter);
        tasksTable.put(task.getId(), task);
    }

    //Метод удаление всех задач
    public void deleteAllTask() {
        tasksTable.clear();
    }

    //Удаление по идентификатору
    public void deleteUseID(int ID) {
        if (tasksTable.containsKey(ID)) {
            tasksTable.remove(ID, tasksTable.get(ID));
        }
    }

    //Метод вывода всех задач
    public String printAllTasks(){
        return tasksTable.toString();
    }

    //Метод вывода по идентификатору
    public String printOneTask(int ID) {
        if (tasksTable.containsKey(ID)) {
            return tasksTable.get(ID).toString();
        } else {
            return "";
        }

    }

    //Обновление задачи
/*    public void updateTask(int taskID, String status) {
        StatusOfTask status1 = StatusOfTask.valueOf(status);
        if (tasksTable.containsKey(taskID)) {
            Task task = tasksTable.get(taskID);
            tasksTable.put(taskID, task);
            task.status = status1;
        }

    }*/
}
