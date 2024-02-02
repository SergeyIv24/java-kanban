package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Параметризованный класс - Task и его наследники
public class InMemoryHistoryManage  implements HistoryManager {
    private List<Task> historyOf10Elem;  //Список для хранения истории задач

    private LinkedDataStructure<Task> history = new LinkedDataStructure<>();



    class LinkedDataStructure<T extends Task> {

        private Map<Integer, Node> history = new HashMap<>();

        private Node<T> head; //Голова списка
        private Node<T> tail; //Хвост списка
        private int size; //Размер списка

        //Добавление элемента в конец списка
        public void linkLast(T element) {
            final Node<T> oldTail = tail; //Старый хвост
            final Node<T> newTail = new Node<>(element, oldTail, null); //Новый хвост
            tail = newTail; //Обновление хвоста
            if(oldTail == null) { //Если хвоста нет
                head = newTail; //Голова - это хвост
            } else {
                oldTail.next = newTail; //Связка нового хвоста и старого хвоста
            }
            size++;//Увеличение размера коллекции
            history.put(element.getId(), tail); //Добавление узла в мапу по Id этого узла (Задачи)

        }

        //Получение списка истории из мапы
        public List<Node> getListOfHistory() {
            return new ArrayList<>(history.values());
        }

    }

    @Override
    public List<Task> getListOfHistory() {
        List<Task> tasksHistory = new ArrayList<>();
        for (Node node : history.getListOfHistory()) {
            tasksHistory.add(node.getData());
        }
        return tasksHistory;
    }










    public InMemoryHistoryManage() {
        historyOf10Elem = new ArrayList<>();
    }


    @Override
    public void addTaskInHistory(Task task) {
        //historyOf10Elem.add(task);
        history.linkLast(task);



    }

    @Override
    public void removeItem(int id) {

    }






/*    @Override
    public List<Task> getListOfHistory(i) {
        return historyOf10Elem;
    }*/




}
