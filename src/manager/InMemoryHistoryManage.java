package manager;

import tasks.Task;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManage implements HistoryManager {

    static class Node <E> {
        E data;
        Node <E> prev;
        Node <E> next;

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

         //Переопределен, так как при использовании собственных классов в HashMap, следует переопределить хеш функцию.
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
    Map<Integer, Node<Task>> nodeMap; //Мапа для хранения пары ключ - node

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
    private void removeNode(int id) { //Заранее известен id узла
        final Node<Task> node = nodeMap.get(id); //Получение node из мапы
        nodeMap.remove(id); //Удаление из мапы
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

    //Метод получения истории в виде списка
    private List<Task> getList() {
        List<Task> taskList = new ArrayList<>();
        for (Node<Task> x = head; x != null; ) { //Цикл по элементам связного списка, так сохраняется порядок вызова задач
            Node<Task> next = x.next;
            taskList.add(x.data);
            x = next;
        }
        return taskList;
    }

    @Override
    public void addTaskInHistory(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            removeNode(task.getId());
        }
        linkLast(task);
        nodeMap.put(task.getId(), tail); //Добавление узла в мапу по Id этого узла (Задачи)

    }

    @Override
    public void removeItem(int id) {
        removeNode(id); //Если задача удаляется, метод вызывается в InMemoryTaskManager для удаления также, из истории
    }

    @Override
    public List<Task> getListOfHistory() {
        return getList();
    }
}
