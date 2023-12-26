package manager;

import tasks.Subtask;
import tasks.Task;

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
        manager.addSubTaskInEpic(6, "Вызываем такси", "Надежды мало");
        manager.addSubTaskInEpic(6, "Ищем каршеринг", "Такси же дорого");
        manager.addSubTaskInEpic(6, "Едем домой", "Желательно без ДТП");

        //Обычные задачи
        manager.addTask("Проверить", "Правильность работы");
        manager.addTask("Сдать работу", "До НГ");


        //Вывод результатов
        System.out.println("Все обычные задачи задачи " + manager.receiveAllTasks());
        System.out.println("Все задачи + все подзадачи " + manager.receiveSubtasksAndTasks());
        System.out.println("Все задачи эпика 1 " + manager.receiveAllSubtasksOfEpic(1));
        System.out.println("Подзадача по ID " + manager.receiveSubtasksUseID(3));
        System.out.println("Эпик по ID " + manager.receiveOneEpic(6));
        System.out.println("Задача по ID " + manager.printOneTask(10) + "\n");

        //Изменение статуса подзадач
        manager.updateSubtask("NEW", new Subtask("Сходить в магазин", "Купить макароны", 2));
        manager.updateSubtask("DONE", new Subtask("Вернутся домой", "Там будем готовить", 3));
        manager.updateSubtask("IN_PROGRESS", new Subtask("Готовим ужин", "Желательно вкусный", 4));
        manager.updateSubtask("DONE", new Subtask("Зовем гостей", "Ждем гостей", 5));

        //Изменение статуса задач
        manager.updateTask("DONE", new Task("Проверить", "Правильность работы", 10));
        manager.updateTask("IN_PROGRESS", new Task("Сдать работу", "До НГ", 11));

        //Выводы после изменения статуса
        System.out.println("Статус эпика после изменений " + manager.receiveOneEpic(1));
        System.out.println("Все подзадачи после изменения " + manager.receiveAllSubtasksOfEpic(1) + "\n");

        System.out.println("Задача после смены статуса " + manager.printOneTask(10));
        System.out.println("Другая задача после смены статуса " + manager.printOneTask(11) + "\n");

        //Удаление
        manager.deleteAllSubtasksOfEpic(6);
        System.out.println("Все подзадачи удалены " + manager.receiveAllSubtasksOfEpic(6));
        manager.deleteEpic(1);
        System.out.println("Эпик удален " + manager.receiveOneEpic(1));
        manager.deleteUseID(10);
        System.out.println("Задача была удалена " + manager.receiveAllTasks());



















    }
}
