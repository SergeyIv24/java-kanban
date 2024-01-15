package tasks;

import manager.HistoryManager;
import manager.InMemoryHistoryManage;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Managers managers;
    TaskManager testObj;
    HistoryManager testHistory;

    @BeforeEach
    public void createTask1AndTask2() {
        managers = new Managers();
        testObj = managers.getDefault();
        testHistory = managers.getDefaultHistory();
        testObj.addTask("Полет", "Нормальный");
        testObj.createEpic("Тест", "Тест");
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


    @Test
    public void inMemoryHistoryManageShouldBeNotNullAfterInnit() {
        assertNotNull(Managers.getDefaultHistory());
    }


}