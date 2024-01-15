package tasks;

import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Managers managers;
    InMemoryTaskManager testObj;
    HistoryManager testHistory;


    @BeforeEach
    public void createTask1AndTask2() {
        managers = new Managers();
        testObj = managers.getDefault();
        testHistory = managers.getDefaultHistory();
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
        assertSame(testObj.receiveOneTask(0), testObj.receiveOneTask(0), "Something went wrong");
    }

    //Наследники Task с одинаковы id равны
    @Test
    public void shouldBeEqualsSubTasksWhenTheSameId() {
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
    public void shouldBeNothingWhenAddSubtaskInEpicDoesntExist(){
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
        //Последний вызванный элемент был с id 3, должен быть равен объекту с тем же id из мапы
        assertTrue(testObj.getHistory().getListOfHistory().get(2).equals(testObj.getEpicTable().get(3)));
        assertTrue(testObj.getHistory().getListOfHistory().get(1).equals(testObj.getEpicTable().get(2)));

    }

    //Проверка, что при создании Task или наследников id уникален
    @Test
    public void shouldUniqueIdWhenCreateTask() {
        //При создании любой задачи id увеличивается, поэтому всегда уникален
        testObj.createEpic("Уникальность", "Id");
        assertEquals(4, testObj.getCounter());
        testObj.createEpic("Уникальность", "Id");
        assertEquals(5, testObj.getCounter());
    }

    //Проверка, что обновляемая subtask, есть в хранилище
    @Test
    public void shouldNotFoundSubtaskWhenIsGivenIdWhichDoesntExist() {
        assertFalse(testObj.updateSubtask(new Subtask("Такой", "не существует", 15)));
    }


}