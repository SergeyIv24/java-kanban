package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    //Методы для эпиков
    //Создание эпика
    void createEpic(String name, String description);

    //Получение эпика по идентификатору
    Epic receiveOneEpic(int epicId);

    //Удаление всех эпиков
    String deleteAllEpics();
    //Удаление эпика по идентификатору
    void deleteEpic(int epicId);

    //Обновление статуса эпика
    void updateEpic(Epic epic);

    //Методы для подзадач эпиков
    //Добавление подзадачи в эпик
    void addSubTaskInEpic(int epicId, String name, String description);

    //Вывод подзадачи по идентификатору
    Subtask receiveSubtasksUseID(int subtaskId);

    //Удаление всех подзадач одного эпика
    void deleteAllSubtasksOfEpic(int epicId);

    //Удаление подзадачи по идентификатору
    boolean deleteParticularSubtask(int subtaskId);

    //Обновление подзадачи по идентификатору, смена статуса
    void updateSubtask(Subtask subtask);

    //Все подзадачи эпика
    ArrayList<Subtask> receiveAllSubtasksOfEpic(int epicID);

    // Методы для простых задач.
    //Метод добавления простой задачи
    void addTask(String name, String description);

    //Метод удаление всех задач
    void deleteAllTask();

    //Удаление по идентификатору
    void deleteUseID(int ID);

    //Метод возвращающий коллекцию всех обычных задач
    ArrayList<Task> receiveAllTasks();

    //Метода возвращающий все обычные задачи и все подзадачи в одной коллекции
    ArrayList<Task> receiveSubtasksAndTasks();

    //Метод вывода по идентификатору
    Task receiveOneTask(int id);

    //Обновление задачи
    void updateTask(Task task);

    List<Task> getHistory();







}
