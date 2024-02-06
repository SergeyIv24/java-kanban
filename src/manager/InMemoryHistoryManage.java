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
        @Override
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
    Map<Integer, Node<Task>> nodeMap;

    public InMemoryHistoryManage() {
        nodeMap = new HashMap<>();
    }

    //Добавление элемента в конец списка
    private void linkLast(Task element) {
        final Node<Task> newNode = new Node<>(element, tail, null);
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }


    //Метод удаления узла из связного списка
    private void removeNode(int id) { //Узел заранее известен
        final Node<Task> node = nodeMap.get(id);
        nodeMap.remove(id);
        if (node == null) {
            return;
        }
        Node<Task> prevElement = node.prev;
        Node<Task> nextElement = node.next;

        if (prevElement == null) {
            head = nextElement;
        } else {
            prevElement.next = nextElement;
            node.prev = null;
        }

        if (nextElement == null) {
            tail = prevElement;
        } else {
            node.next = null;
            nextElement.prev = prevElement;
        }
        node.data = null;
    }

    private List<Task> getList() {
        List<Task> taskList = new ArrayList<>();
        for (Node<Task> x = head; x != null; ) {
            Node<Task> next = x.next;
            taskList.add(x.data);
            x = next;
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
        removeNode(id);

    }

    @Override
    public List<Task> getListOfHistory() {
        return getList();
    }
}
