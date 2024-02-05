package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManage implements HistoryManager {

    static class Node <E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
        @Override //Переопределяем в суперклассе, чтобы все другие унаследовали полностью готовый метод
        public final int hashCode() {
            int hash = 17;
            hash = hash * 17 + data.hashCode();
            hash = hash * 17 + next.hashCode();
            hash = hash * 17 + prev.hashCode();
            return hash;
        }
    }

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;
    Map<Integer, Node<Task>> nodeMap;

    public InMemoryHistoryManage() {
        nodeMap = new HashMap<>();
    }

    //Добавление элемента в конец списка
    public void linkLast(Task element) {
        final Node<Task> oldTail = tail; //Старый хвост
        final Node<Task> newTail = new Node<>(element, oldTail, null); //Новый хвост
        tail = newTail; //Обновление хвоста
        if(oldTail == null) { //Если хвоста нет
            head = newTail; //Голова - это хвост
        } else {
            oldTail.next = newTail; //Связка нового хвоста и старого хвоста
        }
        size++;//Увеличение размера коллекции

    }

    //Метод удаления узла из связного списка
    public void removeNode(Node<Task> node) { //Узел заранее известен
        Node<Task> prevElement = node.prev;
        node.data = null;
        node.prev = null;
        tail = prevElement;
        if (prevElement == null) {
            head = null;
        } else {
            prevElement.next = null;
        }
        size--;
    }

    public List<Task> getList() {
        List<Task> taskList = new ArrayList<>();
        for (Node<Task> node : nodeMap.values()) {
            taskList.add(node.data);
        }
        return taskList;
    }




    @Override
    public void addTaskInHistory(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            removeItem(task.getId());
        }
        linkLast(task);
        nodeMap.put(task.getId(), tail); //Добавление узла в мапу по Id этого узла (Задачи)

    }

    @Override
    public void removeItem(int id) {
        removeNode(nodeMap.get(id));
        nodeMap.remove(id);
    }

    @Override
    public List<Task> getListOfHistory() {
        return getList();
    }
}
