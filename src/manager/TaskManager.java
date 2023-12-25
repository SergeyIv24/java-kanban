import java.util.HashMap;

public class TaskManager {
    private int counter = 0;
    private HashMap<Integer, Task> tasksTable; //Объявление мапы для обычных задач
    private HashMap<Integer, Epic> epicTable; //Объявление мапы для эпиков

    public TaskManager() {
        tasksTable = new HashMap<>(); // Инициализация мап
        epicTable = new HashMap<>();
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
        counter += 1; //Счет с 1
        Subtask subtask = new Subtask(name, description, counter); //Создание объекта
        Epic epic = epicTable.get(epicID); //Получение объекта эпика по ID
        epic.getSubtasks().put(counter, subtask); //Новый объект в мапу подзадач
    }


    //Вывод подзадачи по идентификатору
    public String printSubtasksUseID(int epicID, int subtaskID) { //Чтобы найти подзадачу в эпике, нужно найти
        Epic epic = epicTable.get(epicID);                          //сам эпик
        if (epic == null) { //Если объекта нет, пустая строка вместо исключения
            return "";
        }
        if (epic.getSubtasks().containsKey(subtaskID)) { //Проверка наличия ключа
            return epic.getSubtasks().get(subtaskID).toString(); //Получение мапы подзадач, получение подзадачи
        } else {
            return ""; //Не найдено - пустая строка
        }
    }

    //Удаление всех подзадач одного эпика
    public void deleteAllSubtasksOfEpic(int epicID) {
        Epic epic = epicTable.get(epicID);
        epic.getSubtasks().clear();
    }


    //Удаление подзадачи по идентификатору
    public String removeParticularSubtask(int epicID, int subtaskID) {
        Epic epic = epicTable.get(epicID);
        if (epic == null) return "";
        if (epic.getSubtasks().containsKey(subtaskID)) {
            epic.getSubtasks().remove(subtaskID);
            return "Подзадача удалена";
        } else return "";
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
    public void updateTask(int taskID, String status) {
        StatusOfTask status1 = StatusOfTask.valueOf(status);
        if (tasksTable.containsKey(taskID)) {
            Task task = tasksTable.get(taskID);
            tasksTable.put(taskID, task);
            task.status = status1;
        }

    }
}
