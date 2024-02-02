package manager;
import tasks.Task;

public class Node <E extends Task> {
    E data; //Данные
    Node<E> prev; //Ссылка не предыдущий элемент
    Node<E> next; //Ссылка на следующий элемент

    public Node(E data, Node<E> prev, Node<E> next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }
}
