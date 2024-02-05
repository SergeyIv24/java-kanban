package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManage  implements HistoryManager {

    private LinkedDataStructure<Task> history; //Поле истории в созданной структуре данных

    public InMemoryHistoryManage() {
        history = new LinkedDataStructure<>(); //Инициализация истории
    }

    //Структура данных для хранения истории
    //Параметризованный класс - Task и его наследники
    static class LinkedDataStructure<T extends Task> {

        private Map<Integer, Node> nodesOfHistory = new HashMap<>(); //Мапа id - узел
        private Node<T> head; //Голова списка
        private Node<T> tail; //Хвост списка
        private int size; //Размер списка

        public Map<Integer, Node> getNodesOfHistory() {
            return nodesOfHistory;
        }

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
            nodesOfHistory.put(element.getId(), tail); //Добавление узла в мапу по Id этого узла (Задачи)
        }

        //Получение списка истории из мапы
        public List<Task> getTasks() {
            ArrayList<Node> nodes = new ArrayList<>(nodesOfHistory.values());
            List<Task> tasksHistory = new ArrayList<>();
            for (Node node : nodes) {
                tasksHistory.add(node.data);
            }
            return tasksHistory;
        }

        //Метод удаления узла из связного списка
        public void removeNode(Node node) { //Узел заранее известен
            Node prevElem = node.prev; //Ссылка на объект перед рассматриваемым
            Node nextElem = node.next; //Ссылка на объект после рассматриваемого

            if (prevElem != null) {
                prevElem.next = node.next; //Следующая ссылка, прошлого элемента, привязывается на следующую ссылку текущего
                node.prev = null; //Ссылка на предыдущий элемент обнулена
                node.next = null; //Ссылка на следующий обнулена
                node.data = null; //Данные обнулены
            }
        }

    }

    @Override
    public List<Task> getListOfHistory() {
        return history.getTasks();
    }

    @Override
    public void addTaskInHistory(Task task) {
        if (history.getNodesOfHistory().containsValue(task)) {
            removeItem(task.getId());
        }
        history.linkLast(task);
    }

    @Override
    public void removeItem(int id) {
        history.removeNode(history.getNodesOfHistory().get(id));
        history.getNodesOfHistory().remove(id);
    }

}
