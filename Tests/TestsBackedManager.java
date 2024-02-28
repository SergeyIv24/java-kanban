import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class TestsBackedManager {

    Managers managers;
    FileBackedTaskManager testObj;
    HistoryManager testHistory;
    File tempFile;

    @BeforeEach
    public void createTask1AndTask2() throws IOException {
        tempFile = File.createTempFile("file.csv", null);
        managers = new Managers();
        testHistory = managers.getDefaultHistory();
        testObj = Managers.getFileBackedTaskManager(tempFile);
        testObj.addTask("Полет", "Нормальный"); //id 1
        testObj.createEpic("Тест", "Тест"); //id 2
        testObj.createEpic("Тестовый", "Эпик"); //id 3
        testObj.receiveOneTask(1);
        testObj.receiveOneEpic(2);
        testObj.receiveOneEpic(3);
    }

    //Объекты обычные задачи с одинаковым id равны
    @Test
    public void shouldBeEqualsWhenTheSameId() {
        FileBackedTaskManager.loadFromFile(tempFile);
        assertSame(testObj.receiveOneTask(1), testObj.receiveOneTask(1), "Something went wrong");
    }

    //Наследники Task с одинаковы id равны
    @Test
    public void shouldBeEqualsSubTasksWhenTheSameId() {
        FileBackedTaskManager.loadFromFile(tempFile);
        assertSame(testObj.receiveOneEpic(2), testObj.receiveOneEpic(2), "Something went wrong");
    }

    //Утилитарный класс инициализирует HistoryManager
    @Test
    public void managersShouldBeNotNullAfterInnit() {
        assertNotNull(testHistory);
    }

    //Утилитарный класс инициализирует TaskManager
    @Test
    public void inMemoryTaskManagerShouldBeNotNullAfterInnit() {
        assertNotNull(testObj);
    }

    //Добавление subtask в несуществующий Epic.
    @Test
    public void shouldBeNothingWhenAddSubtaskInEpicDoesntExist() {
        FileBackedTaskManager.loadFromFile(tempFile);
        testObj.addSubTaskInEpic(4, "Подзадача", "Не должна быть добавлена"); //id 4 нет
        //Проверка есть ли в одной из мап Subtask, относящийся к несуществующему эпику
        assertNull(testObj.getEpicTable().get(4), "Something went wrong");
        assertNull(testObj.getSubtaskTable().get(4), "Something went wrong");
        assertNull(testObj.getTaskTable().get(4), "Something went wrong");
    }

    //Проверка, что история существует
    @Test
    public void historyShouldExist() {
        assertFalse(testObj.getHistory().getListOfHistory().isEmpty()); //В истории должно быть 3 элемента
    }

    //Проверка, что в истории хранится предыдущий элемент и последний
    @Test
    public void shouldStorePreviousItemOfHistoryAndLastItem() {
        FileBackedTaskManager.loadFromFile(tempFile);
        //Последний вызванный элемент был с id 3, должен быть равен объекту с тем же id из мапы
        assertTrue(testObj.getHistory().getListOfHistory().get(2).equals(testObj.getEpicTable().get(3)));
        assertTrue(testObj.getHistory().getListOfHistory().get(1).equals(testObj.getEpicTable().get(2)));
    }

    //Проверка, что при создании Task или наследников id уникален
    @Test
    public void shouldUniqueIdWhenCreateTask() {
        FileBackedTaskManager.loadFromFile(tempFile);
        //При создании любой задачи id увеличивается, поэтому всегда уникален
        testObj.createEpic("Уникальность", "Id"); // id 4
        assertEquals(4, testObj.getCounter());
        testObj.createEpic("Уникальность", "Id"); //id 5
        assertEquals(5, testObj.getCounter());
    }

    //Проверка, что обновляемая subtask, есть в хранилище
    @Test
    public void shouldNotFoundSubtaskWhenIsGivenIdWhichDoesntExist() {
        FileBackedTaskManager.loadFromFile(tempFile);
        assertFalse(testObj.updateSubtask(new Subtask("Такой", "не существует", 15)));
    }

    //Проверка, что задача создается и ее можно найти
    @Test
    public void shouldCreateTaskAndFindIts() {
        FileBackedTaskManager.loadFromFile(tempFile);
        testObj.addTask("1", "2"); //id 4
        assertNotNull(testObj.receiveOneTask(4));
    }

    //Проверка, что Subtask и Epic создаются и могут быть найдены по id
    @Test
    public void shouldCreateAndFindEpicAndSubtask() {
        FileBackedTaskManager.loadFromFile(tempFile);
        testObj.createEpic("1", "2"); //id 4
        testObj.addSubTaskInEpic(4, "Подзадача", "Подзадача"); // id 5
        assertNotNull(testObj.receiveOneEpic(4));
        assertNotNull(testObj.receiveSubtasksUseID(5));
    }

    //Изменение статуса эпика
    @Test
    public void shouldChangeStatus() {
        FileBackedTaskManager.loadFromFile(tempFile);
        testObj.addSubTaskInEpic(2, "Сходить в магазин", "Купить макароны"); //id 4
        Subtask subtask1 = new Subtask("Сходить в магазин", "Купить макароны", 4);
        subtask1.setStatus("DONE");
        testObj.updateSubtask(subtask1);
        assertEquals("DONE", testObj.receiveOneEpic(2).getStatus().toString());
    }

    //Обновление статуса подзадачи
    @Test
    public void shouldChangeStatusOfTask() {
        FileBackedTaskManager.loadFromFile(tempFile);
        Task taskTest = new Task("Смена", "Статуса", 1);
        taskTest.setStatus("IN_PROGRESS");
        testObj.updateTask(taskTest);
        assertEquals("IN_PROGRESS", testObj.receiveOneTask(1).getStatus().toString());
    }




}
