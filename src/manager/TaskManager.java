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
    public Epic printOneEpic(int epicId){
        if (epicTable.containsKey(epicId)) { //Проверка наличия ключа в мапе
            return epicTable.get(epicId);
        }
        return null;
    }

    //Удаление всех эпиков
    public String deleteAllEpics() {
        epicTable.clear();
        return "Все эпики удалены";
    }

    //Удаление эпика по идентификатору
    public void deleteEpic(int epicId) {
        if (epicTable.containsKey(epicId)) { //Если эпик в мапе
            epicTable.remove(epicId, epicTable.get(epicId));
        }
    }

    //Обновление статуса эпика
    public void updateEpic(Epic epic) {
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
        epicTable.put(epic.getId(), epic);
    }


//Методы для подзадач эпиков

    //Добавление подзадачи в эпик
    public void addSubTaskInEpic(int epicId, String name, String description) {
        counter += 1;
        Subtask subtask = new Subtask(name, description, counter); //Создание объекта подзадачи
        Epic epic = epicTable.get(epicId); // Получение объекта эпика по ID
        if (epic != null) {
            epic.getSubtasks().add(subtask); // Подзадач в список подзадач эпика
            subtaskTable.put(counter, subtask); // Подазадча в мапу подзадач
        }

    }

    //Вывод подзадачи по идентификатору
    public Subtask printSubtasksUseID(int subtaskId) {
        if (subtaskTable.containsKey(subtaskId)) {
            return subtaskTable.get(subtaskId);
        }
        return null;
    }


    //Удаление всех подзадач одного эпика
    public void deleteAllSubtasksOfEpic(int epicId) {
        Epic epic = epicTable.get(epicId); //Эпик по id
        for (Subtask sub : epic.getSubtasks()) { //Удаление подзадачи из мапы подзадач по ключу
            subtaskTable.remove(sub.getId());
        }
        epic.getSubtasks().clear(); //Удаление элементов из списка
    }


    //Удаление подзадачи по идентификатору
    public boolean removeParticularSubtask(int subtaskId) {
        if (subtaskTable.containsKey(subtaskId)) {
            subtaskTable.remove(subtaskId);
            return true;
        }
        return false;
    }

    //Обновление подзадачи по идентификатору, смена статуса
    //На вход поступают параметры для создания нового объекта
    public void updateSubtask(int subtaskId, String  name, String description, String status) {
        StatusOfTask statusStrToEnum = StatusOfTask.valueOf(status);
        Epic newEpic = null; //Стартовое значение для запуска цикла
        for (Epic epic : epicTable.values()) { //Цикл по значениям мапы эпиков
            int i = 0; //Счетчик индекса подзадачи в списке
            for (Subtask sub : epic.getSubtasks()) { //Цикл по подзадачам эпика
                if (sub.getId() == subtaskId) { //Если найден по id
                    Subtask newSubtask = new Subtask(name, description, subtaskId); // Новый объект
                    newSubtask.setStatus(statusStrToEnum); //Смена статуса
                    epic.getSubtasks().set(i, newSubtask); //Замена элемента в списке
                    newEpic = epic;
                    break; //Если if сработал не нужно продолжать цикл
                }
                i ++; //Увеличения счетчика индекса
            }
        }
        if (newEpic != null) { //Если эпик по id подзадачи найден
            updateEpic(newEpic); //Вызов метода обновления статуса эпика
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
    public HashMap<Integer, Task> printAllTasks(){
        return tasksTable;
    }

    //Метод вывода по идентификатору
    public String printOneTask(int id) {
        if (tasksTable.containsKey(id)) {
            return tasksTable.get(id).toString();
        } else {
            return "";
        }

    }

    //Обновление задачи
    public void updateTask(int taskId, String name, String description, String status) { //Полное обновление задачи
        StatusOfTask statusStrToEnum = StatusOfTask.valueOf(status);
        if (tasksTable.containsKey(taskId)) {
            Task newTask = new Task(name, description, taskId); //Новый объект
            tasksTable.put(taskId, newTask); //Заменяет собой прошлый объект в мапе
            newTask.setStatus(statusStrToEnum); //Можно заменить статус и остальные пункты
        }
    }
}
