package manager; //отдельный пакет для менеджера

import tasks.*; //Импорт всех классов из пакета tasks

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager history = Managers.getDefaultHistory(); // поля должны быть унаследованы

    private int counter = 0;
    protected HashMap<Integer, Task> tasksTable; //Объявление мапы для обычных задач
    protected HashMap<Integer, Epic> epicTable; //Объявление мапы для эпиков
    protected HashMap<Integer, Subtask> subtaskTable; //Объявление мапы для отдельного хранения подзадач
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));



    public InMemoryTaskManager() {
        tasksTable = new HashMap<>(); // Инициализация мап
        epicTable = new HashMap<>();
        subtaskTable = new HashMap<>();
    }

    //Todo метод сортировки задачи с TreeSet
    //Todo циклы на stream, удалить временные переменные

    public HashMap<Integer, Epic> getEpicTable(){
        return epicTable;
    }

    public HashMap<Integer, Subtask> getSubtaskTable(){
        return subtaskTable;
    }

    public HashMap<Integer, Task> getTaskTable(){
        return tasksTable;
    }



    public HistoryManager getHistory() {
        return history;
    }

    public int getCounter() {
        return counter;
    }

    public List<Task> getPrioritizedTasks() {
        return List.copyOf(prioritizedTasks);
    }


//Методы для эпиков

    //Создание эпика
    @Override
    public void createEpic(Epic epic){
        counter += 1; //ID считается с 1
        epic.setId(counter);
        epicTable.put(counter, epic); //Эпик в мапу эпиков
        epic.solveStartTimeAndDuration(); //Расчет времени при создании Эпика
    }

    //Получение эпика по идентификатору
    @Override
    public Epic receiveOneEpic(int epicId) {
        if (epicTable.containsKey(epicId)) { //Проверка наличия ключа в мапе
            history.addTaskInHistory(epicTable.get(epicId)); //Добавление вызванной задачи в историю
            return epicTable.get(epicId);
        }
        return null;
    }

    //Удаление всех эпиков
    @Override
    public String deleteAllEpics() {
        epicTable.clear();
        return "Все эпики удалены";
    }

    //Удаление эпика по идентификатору
    @Override
    public void deleteEpic(int epicId) {
        if (epicTable.containsKey(epicId)) { //Если эпик в мапе
            deleteAllSubtasksOfEpic(epicId);
            epicTable.remove(epicId, epicTable.get(epicId));
            history.removeItem(epicId);
        }
    }

    //Обновление статуса эпика
    @Override
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
                epic.setStatus("NEW"); //Значит все задачи NEW эпик - NEW
            } else if (amountOfDone == epic.getSubtasks().size()) { //Если количество элементов DONE равно размеру списка
                epic.setStatus("DONE"); // //Значит все задачи DONE эпик - DONE
            } else { //Во всех остальных случаях IN_PROGRESS
                epic.setStatus("IN_PROGRESS");
            }
        }
        epicTable.put(epic.getId(), epic);
    }


//Методы для подзадач эпиков

    //Добавление подзадачи в эпик
    @Override
    public void addSubTaskInEpic(int epicId, Subtask subtask){
        Epic epic = epicTable.get(epicId); // Получение объекта эпика по ID
        if (epic != null) {
            counter += 1;
            //Subtask subtask = new Subtask(name, description, counter); //Создание объекта подзадачи
            subtask.setId(counter);
            epic.getSubtasks().add(subtask); // Подзадач в список подзадач эпика
            subtaskTable.put(counter, subtask); // Подазадча в мапу подзадач
            epic.solveStartTimeAndDuration(); //Пересчет времени при добавлении подзадачи
        }
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }


    }

    //Получение подзадачи по идентификатору
    @Override
    public Subtask receiveSubtasksUseID(int subtaskId) {
        if (subtaskTable.containsKey(subtaskId)) {
            history.addTaskInHistory(subtaskTable.get(subtaskId)); //Добавление вызванной задачи в историю
            return subtaskTable.get(subtaskId);
        }
        return null;
    }


    //Удаление всех подзадач одного эпика
    @Override
    public void deleteAllSubtasksOfEpic(int epicId) {
        Epic epic = epicTable.get(epicId); //Эпик по id

        for (Subtask sub : epic.getSubtasks()) { //Удаление подзадачи из мапы подзадач по ключу
            subtaskTable.remove(sub.getId());
            history.removeItem(sub.getId()); //Удаление элемента из истории
        }
        epic.getSubtasks().clear(); //Удаление элементов из списка
    }


    //Удаление подзадачи по идентификатору
    @Override
    public boolean deleteParticularSubtask(int subtaskId) {
        if (subtaskTable.containsKey(subtaskId)) {
            subtaskTable.remove(subtaskId);
            history.removeItem(subtaskId); //Удаление элемента из истории
            return true;
        }
        return false;
    }

    //Обновление подзадачи по идентификатору, смена статуса
    @Override
    public boolean updateSubtask(Subtask subtask) { //int subtaskId, String  name, String description, String status
        Epic newEpic = null; //Стартовое значение для запуска цикла
        for (Epic epic : epicTable.values()) { //Цикл по значениям мапы эпиков
            int i = 0; //Счетчик индекса подзадачи в списке
            for (Subtask sub : epic.getSubtasks()) { //Цикл по подзадачам эпика
                if (sub.getId() == subtask.getId()) { //Если найден по id
                    epic.getSubtasks().set(i, subtask); //Замена элемента в списке
                    newEpic = epic;
                    break; //Если if сработал не нужно продолжать цикл
                }
                i ++; //Увеличения счетчика индекса
            }
        }
        if (newEpic != null) { //Если эпик по id подзадачи найден
            updateEpic(newEpic); //Вызов метода обновления статуса эпика
            return true;
        } else {
            return false;
        }
    }

    //Все подзадачи эпика
    @Override
    public ArrayList<Subtask> receiveAllSubtasksOfEpic(int epicID) {
        Epic epic = epicTable.get(epicID);
        if (epic == null) return null;
        return epic.getSubtasks();
    }


// Методы для простых задач.

    //Метод добавления простой задачи
    @Override
    public void addTask(Task task){
        counter += 1;
        //Task task = new Task(name, description, counter);
        task.setId(counter);
        tasksTable.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    //Метод удаление всех задач
    @Override
    public void deleteAllTask() {
        tasksTable.clear();
    }

    //Удаление по идентификатору
    @Override
    public void deleteUseID(int ID) {
        if (tasksTable.containsKey(ID)) {
            tasksTable.remove(ID, tasksTable.get(ID));
            history.removeItem(ID); //Удаление элемента из истории
        }
    }

    //Метод возвращающий коллекцию всех обычных задач
    @Override
    public ArrayList<Task> receiveAllTasks() {
        return new ArrayList<>(tasksTable.values());
    }

    //Метода возвращающий все обычные задачи и все подзадачи в одной коллекции
    @Override
    public ArrayList<Task> receiveSubtasksAndTasks() { //Возвращает коллекцию
        ArrayList<Task> allTasks = new ArrayList<>(); //Создание нового списка для хранения подзадач и задач
        allTasks.addAll(tasksTable.values()); //Добавление всех обычных задач из мапы в список
        allTasks.addAll(subtaskTable.values()); //Добавление всех подзадач из мапы в список
        return allTasks;
    }

    //Метод вывода по идентификатору
    @Override
    public Task receiveOneTask(int id) {
        if (tasksTable.containsKey(id)) {
            history.addTaskInHistory(tasksTable.get(id)); //Добавление вызванной задачи в историю
            return tasksTable.get(id);
        } else {
            return null;
        }

    }

    //Обновление задачи
    @Override
    public void updateTask(Task task) { //Полное обновление задачи
        if (tasksTable.containsKey(task.getId())) {
            tasksTable.put(task.getId(), task); //Заменяет собой прошлый объект в мапе
        }
    }





}
