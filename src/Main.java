import java.util.HashMap;
public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        //Тест эпиков
        //Эпик 1
        manager.createEpic("Устроить праздник", "Устраиваем званный ужин");
        manager.addSubTaskInEpic(1, "Сходить в магазин", "Купить макароны");
        manager.addSubTaskInEpic(1, "Вернутся домой", "Там будем готовить");
        /*manager.addSubTaskInEpic(1, "Готовим ужин", "Желательно вкусный");
        manager.addSubTaskInEpic(1, "Зовем гостей", "Ждем гостей");*/

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

        //Печать всех подзадач эпика 1
        System.out.println("Подзадачи эпика 1 " + manager.printAllSubtasksOfEpic(1));
        //Печать всех подзадач эпика 2
        System.out.println("Подзадачи эпика 1 " + manager.printAllSubtasksOfEpic(2));
        //Печать всех задач
        System.out.println("Все задачи " + manager.printAllTasks());

        //Смена статуса обычной задачи
        manager.updateTask(1, "IN_PROGRESS");
        System.out.println("Статус обычной задачи после изменения " + manager.printOneTask(1));

        System.out.println(manager.printOneEpic(5));



        //Смена статуса подзадач
        manager.updateSubtask(1, 1, "NEW");
        manager.updateSubtask(1, 2, "IN_PROGRESS");
        System.out.println(manager.printAllSubtasksOfEpic(1));
        System.out.println(manager.printOneEpic(1));

        //Удаление всех эпиков
        //manager.deleteAllEpics();
        //System.out.println(manager.deleteAllEpics());

        //Удаление по идентификатору
        //manager.deleteEpic(2);
        //System.out.println(manager.printAllEpics());

        //Вывод подзадачи по ID
        System.out.println(manager.printSubtasksUseID(1, 1));










    }
}
