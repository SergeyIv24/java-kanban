package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManage <T> implements HistoryManager {
    private List<Task> historyOf10Elem;  //Список для хранения истории задач


    private Node<T> head; //Голова списка
    private Node<T> tail; //Хвост списка
    private int size; //Размер списка

    //Вложенный класс узел
    class Node <E> {
        public E data; //Данные
        public Node<E> prev; //Ссылка не предыдущий элемент
        public Node<E> next; //Ссылка на следующий элемент

        public Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
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
    }










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
