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
    public void add(Task task) {
        historyOf10Elem.add(task);
    }


    @Override
    public List<Task> getHistory() {
        return historyOf10Elem;
    }




}
