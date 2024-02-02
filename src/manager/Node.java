package manager;
import java.util.LinkedList;
public class Node <T> {
    public T data;
    public Node<T> prev;
    public Node<T> next;

    public Node(T data) {
        this.data = data;
        prev = null;
        next = null;
    }

}
