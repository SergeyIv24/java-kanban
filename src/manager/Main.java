package manager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

/*        //Тест эпиков
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

        //Печать всех эпиков
        System.out.println("Все эпики " + manager.printAllEpics());

        //Эпик по Id
        System.out.println("Эпик по Id " + manager.printOneEpic(1));

        //Печать всех подзадач эпика 1
        System.out.println("Подзадачи эпика 1 " + manager.printAllSubtasksOfEpic(1));
        //Печать всех подзадач эпика 2
        System.out.println("Подзадачи эпика 2 " + manager.printAllSubtasksOfEpic(6));

        //Подзадача по ID
        System.out.println("Задача с ID 2 " + manager.printSubtasksUseID(2));

        //Удаление всех эпиков
        //manager.deleteAllEpics();
        //System.out.println(manager.printAllEpics());

        //Удаление эпика по Id
        //manager.deleteEpic(1);
        //System.out.println(manager.printAllEpics());*/










        //Обычная задача 1
        manager.addTask("Проверить правильность", "Надежды еще меньше, чем с такси");

        //Обычная задача 2
        manager.addTask("Дебаг работы", "Проверить выводы программы");

        //Печать всех задач
        System.out.println("Все задачи " + manager.printAllTasks());

        //Смена статуса обычной задачи
        manager.updateTask(1, "Проверить правильность", "Надежды еще меньше, чем с такси", "DONE");
        System.out.println("Статус обычной задачи после изменения " + manager.printOneTask(1));

        //System.out.println(manager.printOneEpic(5));


        //Смена статуса подзадач
        //manager.updateSubtask(1, 1, "NEW");
        //manager.updateSubtask(1, 2, "IN_PROGRESS");
        //System.out.println(manager.printAllSubtasksOfEpic(1));
        //System.out.println(manager.printOneEpic(1));

        //Удаление одного эпика
        //manager.deleteEpic(1);
        //System.out.println(manager.printAllEpics());

        //Удаление одной задачи
        //manager.deleteUseID(1);
        //System.out.println(manager.printAllTasks());











    }
}
