package manager;
import java.io.File;
import tasks.*;

public class Main {

    public static void main(String[] args) {

        File file = new File("C:\\Учеба\\Java 2023 - 2024" +
                "\\Задачи\\Проекты ЯП\\Спринт 4\\java-kanban\\src\\manager\\File.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file.toPath());

        //Тест эпиков
        //Эпик 1
        manager.createEpic("Устроить праздник", "Устраиваем званный ужин"); //1
        manager.addSubTaskInEpic(1, "Сходить в магазин", "Купить макароны");//2
        manager.addSubTaskInEpic(1, "Вернутся домой", "Там будем готовить"); //3
        manager.addSubTaskInEpic(1, "Готовим ужин", "Желательно вкусный"); //4
        manager.addSubTaskInEpic(1, "Зовем гостей", "Ждем гостей"); //5

        //Эпик 2
        manager.createEpic("Доехать до дома", "Едем домой"); //6

        //Обычные задачи
        manager.addTask("Проверить", "Правильность работы"); //7
        manager.addTask("Сдать работу", "До НГ"); //8
        manager.addTask("Обычная", "задача"); //9
        manager.addTask("задача", "обычная"); //10

        //Запись истории, вызвав методы получения задач по Id
        manager.receiveOneTask(8);
        manager.receiveOneEpic(1);
        manager.receiveOneEpic(6);
        manager.receiveSubtasksUseID(4);
        manager.receiveSubtasksUseID(3);
        manager.receiveSubtasksUseID(2);
        manager.receiveOneTask(8);
        manager.receiveOneTask(7);
        manager.receiveOneTask(10);
        FileBackedTaskManager.loadFromFile(file);
        //Вывод истории до удаления задач. Повторов нет, порядок вызова сохранен
        for (Task task : manager.getHistory().getListOfHistory()) {
            System.out.println(task);
        }







/*        InMemoryTaskManager manager = new InMemoryTaskManager();
        //Тест эпиков
        //Эпик 1
        manager.createEpic("Устроить праздник", "Устраиваем званный ужин"); //1
        manager.addSubTaskInEpic(1, "Сходить в магазин", "Купить макароны");//2
        manager.addSubTaskInEpic(1, "Вернутся домой", "Там будем готовить"); //3
        manager.addSubTaskInEpic(1, "Готовим ужин", "Желательно вкусный"); //4
        manager.addSubTaskInEpic(1, "Зовем гостей", "Ждем гостей"); //5
        //Эпик 2
        manager.createEpic("Доехать до дома", "Едем домой"); //6

        //Обычные задачи
        manager.addTask("Проверить", "Правильность работы"); //7
        manager.addTask("Сдать работу", "До НГ"); //8
        manager.addTask("Обычная", "задача"); //9
        manager.addTask("задача", "обычная"); //10


        //Запись истории, вызвав методы получения задач по Id
        manager.receiveOneTask(8);
        manager.receiveOneEpic(1);
        manager.receiveOneEpic(6);
        manager.receiveSubtasksUseID(4);
        manager.receiveSubtasksUseID(3);
        manager.receiveSubtasksUseID(2);
        manager.receiveOneTask(8);
        manager.receiveOneTask(7);
        manager.receiveOneTask(11);
        manager.receiveOneTask(10);

        manager.receiveSubtasksUseID(3);
        manager.receiveSubtasksUseID(2);
        manager.receiveOneTask(8);
        manager.receiveOneEpic(1);
        manager.receiveOneEpic(6);
        manager.receiveSubtasksUseID(4);
        manager.receiveSubtasksUseID(3);
        manager.receiveSubtasksUseID(2);
        manager.receiveOneTask(8);
        manager.receiveOneTask(7);
        manager.receiveOneTask(11);
        manager.receiveOneTask(10);

        manager.receiveSubtasksUseID(3);
        manager.receiveSubtasksUseID(2);


        //Вывод истории до удаления задач. Повторов нет, порядок вызова сохранен
        for (Task task : manager.getHistory().getListOfHistory()) {
            System.out.println("История до удаления " + task);
        }
        System.out.println("\n");


        manager.deleteParticularSubtask(2); //Подзадача удалена
        manager.deleteUseID(8); //Задача удалена
        manager.deleteEpic(1); //Удален эпик и все его подзадачи
        manager.deleteEpic(20); //Удаление несуществующего эпика.

        //Вывод истории. Удаленные элементы не отображаются в истории
        for (Task task : manager.getHistory().getListOfHistory()) {
            System.out.println("История после удаления " + task);
        }*/









/*        manager.addSubTaskInEpic(6, "Вызываем такси", "Надежды мало");
        manager.addSubTaskInEpic(6, "Ищем каршеринг", "Такси же дорого");
        manager.addSubTaskInEpic(6, "Едем домой", "Желательно без ДТП");*/




/*        //Вывод результатов
        System.out.println("Все обычные задачи задачи " + manager.receiveAllTasks());
        System.out.println("Все задачи + все подзадачи " + manager.receiveSubtasksAndTasks());
        System.out.println("Все задачи эпика 1 " + manager.receiveAllSubtasksOfEpic(1));
        System.out.println("Подзадача по ID " + manager.receiveSubtasksUseID(3));
        System.out.println("Эпик по ID " + manager.receiveOneEpic(6));
        System.out.println("Задача по ID " + manager.receiveOneTask(10) + "\n");*/

/*        //Изменение статуса подзадач
        Subtask subtask1 = new Subtask("Сходить в магазин", "Купить макароны", 2);
        subtask1.setStatus("DONE");
        Subtask subtask2 = new Subtask("Вернутся домой", "Там будем готовить", 3);
        subtask2.setStatus("IN_PROGRESS");
        Subtask subtask3 = new Subtask("Готовим ужин", "Желательно вкусный", 4);
        Subtask subtask4 = new Subtask("Зовем гостей", "Ждем гостей", 5);*/

/*        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask3);
        manager.updateSubtask(subtask4);*/

/*        //Изменение статуса задач
        Task task1 = new Task("Проверить", "Правильность работы", 10);
        task1.setStatus("DONE");
        manager.updateTask(task1);
        Task task2 = new Task("Сдать работу", "До НГ", 11);
        task2.setStatus("IN_PROGRESS");
        manager.updateTask(task2);*/

/*        //Выводы после изменения статуса
        System.out.println("Статус эпика после изменений " + manager.receiveOneEpic(1));
        System.out.println("Все подзадачи после изменения " + manager.receiveAllSubtasksOfEpic(1) + "\n");

        System.out.println("Задача после смены статуса " + manager.receiveOneTask(10));
        System.out.println("Другая задача после смены статуса " + manager.receiveOneTask(11) + "\n");*/










/*        //Удаление
        manager.deleteAllSubtasksOfEpic(6);
        System.out.println("Все подзадачи удалены " + manager.receiveAllSubtasksOfEpic(6));
        manager.deleteEpic(1);
        System.out.println("Эпик удален " + manager.receiveOneEpic(1));
        manager.deleteUseID(10);
        System.out.println("Задача была удалена " + manager.receiveAllTasks());*/



















    }
}
