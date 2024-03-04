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


    public HashMap<Integer, Epic> getEpicTable() {
        return epicTable;
    }

    public HashMap<Integer, Subtask> getSubtaskTable() {
        return subtaskTable;
    }

    public HashMap<Integer, Task> getTaskTable() {
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

    //Метод поиска пересечений задач
    public boolean isCrossingOther(Task task) {
        //Если старт не задан, то пересечений не будет
        if (task.getStartTime() == null) {
            return false;
        }

        for (Task task1 : getPrioritizedTasks()) {
            if (task.getEndTime().isBefore(task1.getEndTime())
                && task.getStartTime().isAfter(task1.getStartTime())) {
                return true;
            }
            if (task.getEndTime().isAfter(task1.getEndTime())
                && task.getStartTime().isBefore(task1.getStartTime())) {
                return true;
            }
            if (task.getStartTime().equals(task1.getStartTime())
                    || task.getEndTime().equals(task1.getEndTime())) {
                return true;
            }
        }
        return false; //Пересечений нет
    }


//Методы для эпиков

    //Создание эпика
    @Override
    public void createEpic(Epic epic) {
        counter += 1; //ID считается с 1
        epic.setId(counter);
        epicTable.put(counter, epic); //Эпик в мапу эпиков
        epic.solveStartTimeAndDuration(); //Расчет времени при создании Эпика
    }

    //Получение эпика по идентификатору
    @Override
    public Optional<Epic> receiveOneEpic(int epicId) {
        Optional<Epic> particularEpic = epicTable.values().stream().filter(epic -> epic.getId() == epicId).findFirst();
        if (particularEpic.isPresent()) {
            history.addTaskInHistory(epicTable.get(epicId)); //Добавление вызванной задачи в историю
            return particularEpic;
        } else {
            return Optional.empty();
        }
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
    public void addSubTaskInEpic(int epicId, Subtask subtask) {
        if (isCrossingOther(subtask)) { //Если есть пересечение, задача не добавляется
            return;
        }

        Epic epic = epicTable.get(epicId); // Получение объекта эпика по ID
        if (epic != null) {
            counter += 1;
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
    public Optional<Subtask> receiveSubtasksUseID(int subtaskId) {
        Optional<Subtask> particularSubtask = subtaskTable.values().stream().filter(subtask -> subtask.getId() == subtaskId).findFirst();
        if (particularSubtask.isPresent()) {
            history.addTaskInHistory(subtaskTable.get(subtaskId)); //Добавление вызванной задачи в историю
            return particularSubtask;
        } else {
            return Optional.empty();
        }
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
                i++; //Увеличения счетчика индекса
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
    public void addTask(Task task) {
        if (isCrossingOther(task)) { //Если есть пересечение, не добавляется
            return;
        }

        counter += 1;
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
    public void deleteUseID(int id) {
        if (tasksTable.containsKey(id)) {
            tasksTable.remove(id, tasksTable.get(id));
            history.removeItem(id); //Удаление элемента из истории
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
    public Optional<Task> receiveOneTask(int id) {
        Optional<Task> particularTask = tasksTable.values().stream().filter(task -> task.getId() == id).findFirst();
        if (particularTask.isPresent()) {
            history.addTaskInHistory(tasksTable.get(id)); //Добавление вызванной задачи в историю
            return particularTask;
        } else {
            return Optional.empty();
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
