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

    public HashMap<Integer, Subtask> getSubtaskTable(){
        return subtaskTable;
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
        return null;
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


    //Обновление статуса эпика
    public void refreshEpic(Epic epic) {
        int amountOfNew = 0; //Счет количества элементов со статусом NEW
        int amountOfDone = 0; //Счет количества элементов со статусом DONE
        if (epic.getSubtasks() != null) {
            for (Subtask subtasks : epic.getSubtasks()) {
                if (subtasks.getStatus() == StatusOfTask.NEW) { //Считается количество элементов со статусом NEW
                    amountOfNew += 1;
                }
                if (subtasks.getStatus() == StatusOfTask.DONE) { //Считается количество элементов со статусом DONE
                    amountOfDone += 1;
                }
            }

            if (amountOfNew == epic.getSubtasks().size()) { //Если количество элементов NEW равно размеру коллекции
                epic.setStatus(StatusOfTask.NEW); //Значит все задачи NEW эпик - NEW
            } else if (amountOfDone == epic.getSubtasks().size()) { //Если количество элементов DONE равно размеру списка
                epic.setStatus(StatusOfTask.DONE); // //Значит все задачи DONE эпик - DONE
            } else { //Во всех остальных случаях IN_PROGRESS
                epic.setStatus(StatusOfTask.IN_PROGRESS);
            }
        }
        tasksTable.put(epic.getId(), epic);
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
    public void upSub(int subtaskId, String  name, String decription, String status) {
        StatusOfTask statusStrToEnum = StatusOfTask.valueOf(status);
        Epic newEpic = null;
        for (Epic epic : epicTable.values()) {
            for (Subtask sub : epic.getSubtasks()) {
                int i = 0;
                if (sub.getId() == subtaskId) {
                    Subtask newSubtask = new Subtask(name, decription, subtaskId);
                    newSubtask.setStatus(statusStrToEnum);
                    epic.getSubtasks().set(i, newSubtask);
                    subtaskTable.put(subtaskId, newSubtask);
                    newEpic = epic;
                    break;
                }
                i += 1;
            }
            break;

        }
        if (newEpic != null) {
            refreshEpic(newEpic);
        }
    }


    public void updateSubtask(int subtaskId, String  name, String decription, String status) {
        StatusOfTask statusStrToEnum = StatusOfTask.valueOf(status);
        if (subtaskTable.containsKey(subtaskId)) {
            Subtask newSubtask = new Subtask(name, decription, subtaskId);
            subtaskTable.put(subtaskId, newSubtask);
            newSubtask.setStatus(statusStrToEnum);
        }

        Epic newEpic = null;
        for (Epic epic : epicTable.values()) {
            for (Subtask sub : epic.getSubtasks()) {
                if (sub.getId() == subtaskId) {
                    newEpic = epic;
                    break;
                }
            }
            break;
        }
        if (newEpic != null) {
            refreshEpic(newEpic);
        }
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
    public void updateTask(int taskID, String name, String description, String status) { //Полное обновление задачи
        StatusOfTask statusStrToEnum = StatusOfTask.valueOf(status);
        if (tasksTable.containsKey(taskID)) {
            Task newTask = new Task(name, description, taskID); //Новый объект
            tasksTable.put(taskID, newTask); //Заменяет собой прошлый объект в мапе
            newTask.setStatus(statusStrToEnum); //Можно заменить статус и остальные пункты
        }
    }

    public void updateTask(Task task) {

    }
}
