import java.util.HashMap;
public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        //Тест эпиков
        //Эпик 1
        manager.createEpic("Устроить праздник", "Устраиваем званный ужин");
        manager.addSubTaskInEpic(1, "Сходить в магазин", "Купить макароны");
        manager.addSubTaskInEpic(1, "Вернутся домой", "Там будем готовить");
        manager.addSubTaskInEpic(1, "Готовим ужин", "Желательно вкусный");
        manager.addSubTaskInEpic(1, "Зовем гостей", "Ждем гостей");

        //Эпик 2
        manager.createEpic("Доехать до дома", "Едем домой");
        manager.addSubTaskInEpic(2, "Вызываем такси", "Но надежды мало");
        manager.addSubTaskInEpic(2, "Ищем каршеринг", "Такси же дорого");
        manager.addSubTaskInEpic(2, "Едем домой", "Желательно без ДТП");


        //Обычная задача 1
        manager.addTask("Проверить правильность", "Надежды еще меньше, чем с такси");

        //Обычная задача 2
        manager.addTask("Дебаг работы", "Проверить выводы программы");

        //Печать всех эпиков
        System.out.println("Все эпики " + manager.printAllEpics());

        //Печать всех подзадач

        //Печать всех задач
        System.out.println("Все задачи " + manager.printAllTasks());








    }
}
