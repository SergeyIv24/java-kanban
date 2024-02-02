package manager;

import tasks.Task;

public class Node <E extends Task> {
    public E data; //Данные
    public Node<E> prev; //Ссылка не предыдущий элемент
    public Node<E> next; //Ссылка на следующий элемент

    public Node(E data, Node<E> prev, Node<E> next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }

    public E getData() {
        return data;
    }
}
