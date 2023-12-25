package manager;

import tasks.StatusOfTask;

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
        System.out.println("Все задачи " + manager.printAllTasks());
        System.out.println("Все задачи эпика 1 " + manager.printAllSubtasksOfEpic(1));
        System.out.println("Подзадача по ID " + manager.printSubtasksUseID(3));
        System.out.println("Эпик по ID " + manager.printOneEpic(6));
        System.out.println("Задача по ID " + manager.printOneTask(10) + "\n");

        //Изменение статуса подзадач
        manager.updateSubtask(2, "Сходить в магазин", "Купить макароны", "NEW");
        manager.updateSubtask(3, "Вернутся домой", "Там будем готовить", "DONE");
        manager.updateSubtask(4, "Готовим ужин", "Желательно вкусный", "IN_PROGRESS");
        manager.updateSubtask(5, "Зовем гостей", "Ждем гостей", "DONE");

        //Изменение статуса задач
        manager.updateTask(10, "Проверить", "Правильность работы", "DONE");
        manager.updateTask(11, "Сдать работу", "До НГ", "IN_PROGRESS");

        //Выводы после изменения статуса
        System.out.println("Статус эпика после изменений " + manager.printOneEpic(1));
        System.out.println("Все подзадачи после изменения " + manager.printAllSubtasksOfEpic(1) + "\n");

        System.out.println("Задача после смены статуса " + manager.printOneTask(10));
        System.out.println("Другая задача после смены статуса " + manager.printOneTask(11) + "\n");

        //Удаление
        manager.deleteAllSubtasksOfEpic(6);
        System.out.println("Все подзадачи удалены " + manager.printAllSubtasksOfEpic(6));
        manager.deleteEpic(1);
        System.out.println("Эпик удален " + manager.printOneEpic(1));
        manager.deleteUseID(10);
        System.out.println("Задача была удалена " + manager.printAllTasks());



















    }
}
