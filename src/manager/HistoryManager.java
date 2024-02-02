package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    //Добавление задачи в историю
    void addTaskInHistory(Task task);

    //Удаление задачи из истории
    void removeItem(int id);

    //Получение истории
    List<Task> getListOfHistory();

}
