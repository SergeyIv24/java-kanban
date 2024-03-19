package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Optional;

//Todo endpoints для каждого метода в этом интерфейсе
//Todo 5 групп методов и их пути: /tasks, /subtasks, /Epics, /history, /prioritized
//Todo для каждого пути - несколько методов: GET - чтение, POST - обновление, DELETE - удаление
//Todo коды: 200 - успешно выполнено, возврат, 201 - успешно обновлено, возврата нет
//Todo коды: 404 - задача не найдена, 406 - пересечение задач, 500 - ошибки при выполнении запроса
//Todo обмен данными в JSON
//Todo HttpTaskServer - порт 8080, метод main для старта
//Todo обработчик для каждого пути, привязать к пути
//Todo добавить библиотеку GSON
//Todo тесты

public interface TaskManager {

    //Методы для эпиков
    //Создание эпика
    void createEpic(Epic epic);

    //Получение эпика по идентификатору
    Optional<Epic> receiveOneEpic(int epicId);

    //Удаление всех эпиков
    String deleteAllEpics();

    //Удаление эпика по идентификатору
    void deleteEpic(int epicId);

    //Обновление статуса эпика
    void updateEpic(Epic epic);

    //Методы для подзадач эпиков
    //Добавление подзадачи в эпик
    void addSubTaskInEpic(int epicId, Subtask subtask);

    //Вывод подзадачи по идентификатору
    Optional<Subtask> receiveSubtasksUseID(int subtaskId);

    //Удаление всех подзадач одного эпика
    void deleteAllSubtasksOfEpic(int epicId);

    //Удаление подзадачи по идентификатору
    boolean deleteParticularSubtask(int subtaskId);

    //Обновление подзадачи по идентификатору, смена статуса
    boolean updateSubtask(Subtask subtask);

    //Все подзадачи эпика
    ArrayList<Subtask> receiveAllSubtasksOfEpic(int epicID);

    // Методы для простых задач.
    //Метод добавления простой задачи
    boolean addTask(Task task);

    //Метод удаление всех задач
    void deleteAllTask();

    //Удаление по идентификатору
    void deleteUseID(int id);

    //Метод возвращающий коллекцию всех обычных задач
    ArrayList<Task> receiveAllTasks();

    //Метода возвращающий все обычные задачи и все подзадачи в одной коллекции
    ArrayList<Task> receiveSubtasksAndTasks();

    //Метод вывода по идентификатору
    Optional<Task> receiveOneTask(int id);

    //Обновление задачи
    boolean updateTask(Task task);

}
