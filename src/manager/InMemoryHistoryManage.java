package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManage implements HistoryManager {
    private List<Task> historyOf10Elem;  //Список для хранения истории задач

    public InMemoryHistoryManage() {
        historyOf10Elem = new ArrayList<>();
    }


    @Override
    public void addTaskInHistory(Task task) {
        historyOf10Elem.add(task);
/*        //Проверка переполнения списка истории из 10 элементов
        if (historyOf10Elem.size() > 10) {
            historyOf10Elem.remove(0);
        }*/
    }

    @Override
    public void removeItem(int id) {

    }




    @Override
    public List<Task> getListOfHistory() {
        return historyOf10Elem;
    }




}
