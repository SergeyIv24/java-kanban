package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    //Добавление задачи в историю
    void add(Task task);

    //Получение истории
    List<Task> getListOfHistory();

}
