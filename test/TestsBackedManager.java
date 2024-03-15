import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class TestsBackedManager extends TaskManagerTests {

    Managers managers;
    FileBackedTaskManager testObj;
    HistoryManager testHistory;
    File tempFile;

    @Override
    @BeforeEach
    public void createTask1AndTask2() throws IOException {
        tempFile = File.createTempFile("file.csv", null);
        managers = new Managers();
        testHistory = managers.getDefaultHistory();
        testObj = Managers.getFileBackedTaskManager(tempFile);
        testObj.addTask(new Task("Полет", "Нормальный")); //id 1
        testObj.createEpic(new Epic("Тест", "Тест")); //id 2
        testObj.createEpic(new Epic("Тестовый", "Эпик")); //id 3
        testObj.receiveOneTask(1);
        testObj.receiveOneEpic(2);
        testObj.receiveOneEpic(3);
    }

    //Объекты обычные задачи с одинаковым id равны
    @Override
    @Test
    public void shouldBeEqualsWhenTheSameId() {
        FileBackedTaskManager.loadFromFile(tempFile);
        assertSame(testObj.receiveOneTask(1).get(), testObj.receiveOneTask(1).get(), "Something went wrong");
    }

    //Наследники Task с одинаковы id равны
    @Override
    @Test
    public void shouldBeEqualsSubTasksWhenTheSameId() {
        FileBackedTaskManager.loadFromFile(tempFile);
        assertSame(testObj.receiveOneEpic(2).get(), testObj.receiveOneEpic(2).get(), "Something went wrong");
    }

    //Утилитарный класс инициализирует HistoryManager
    @Override
    @Test
    public void managersShouldBeNotNullAfterInnit() {
        assertNotNull(testHistory);
    }

    //Утилитарный класс инициализирует TaskManager
    @Override
    @Test
    public void inMemoryTaskManagerShouldBeNotNullAfterInnit() {
        assertNotNull(testObj);
    }

    //Добавление subtask в несуществующий Epic.
    @Override
    @Test
    public void shouldBeNothingWhenAddSubtaskInEpicDoesntExist() {
        FileBackedTaskManager.loadFromFile(tempFile);
        testObj.addSubTaskInEpic(4, new Subtask("Подзадача", "Не должна быть добавлена")); //id 4 нет
        //Проверка есть ли в одной из мап Subtask, относящийся к несуществующему эпику
        assertNull(testObj.getEpicTable().get(4), "Something went wrong");
        assertNull(testObj.getSubtaskTable().get(4), "Something went wrong");
        assertNull(testObj.getTaskTable().get(4), "Something went wrong");
    }

    //Проверка, что история существует
    @Override
    @Test
    public void historyShouldExist() {
        assertFalse(testObj.getHistory().getListOfHistory().isEmpty()); //В истории должно быть 3 элемента
    }

    //Проверка, что в истории хранится предыдущий элемент и последний
    @Override
    @Test
    public void shouldStorePreviousItemOfHistoryAndLastItem() {
        FileBackedTaskManager.loadFromFile(tempFile);
        //Последний вызванный элемент был с id 3, должен быть равен объекту с тем же id из мапы
        assertTrue(testObj.getHistory().getListOfHistory().get(2).equals(testObj.getEpicTable().get(3)));
        assertTrue(testObj.getHistory().getListOfHistory().get(1).equals(testObj.getEpicTable().get(2)));
    }

    //Проверка, что история хранит только уникальные элементы
    @Override
    @Test
    public void shouldStoreUniqueThings() {
        FileBackedTaskManager.loadFromFile(tempFile);
        for (int i = 0; i < testObj.getHistory().getListOfHistory().size(); i++) {
            for (int j = 0; j < testObj.getHistory().getListOfHistory().size(); j++) {
                if (i != j) {
                    Assertions.assertNotEquals(testObj.getHistory().getListOfHistory().get(i),
                            testObj.getHistory().getListOfHistory().get(j));
                }
            }
        }
    }

    //Проверка удаления элементов истории из начала
    @Override
    @Test
    public void shouldRemoveItemsFromStart() {
        FileBackedTaskManager.loadFromFile(tempFile);
        Task removingTaskFromStart = testObj.getHistory().getListOfHistory().get(0);
        testObj.getHistory().removeItem(1);
        assertFalse(testObj.getHistory().getListOfHistory().contains(removingTaskFromStart));
    }

    //Проверка удаления элементов истории c конца
    @Override
    @Test
    public void shouldRemoveItemFromEnd() {
        FileBackedTaskManager.loadFromFile(tempFile);
        Task removingTaskFromEnd = testObj.getHistory().getListOfHistory().get(2);
        testObj.getHistory().removeItem(3);
        assertFalse(testObj.getHistory().getListOfHistory().contains(removingTaskFromEnd));
    }

    //Проверка, что при создании Task или наследников id уникален
    @Override
    @Test
    public void shouldUniqueIdWhenCreateTask() {
        FileBackedTaskManager.loadFromFile(tempFile);
        //При создании любой задачи id увеличивается, поэтому всегда уникален
        testObj.createEpic(new Epic("Уникальность", "Id")); // id 4
        assertEquals(4, testObj.getCounter());
        testObj.createEpic(new Epic("Уникальность", "Id")); //id 5
        assertEquals(5, testObj.getCounter());
    }

    //Проверка, что обновляемая subtask, есть в хранилище
    @Override
    @Test
    public void shouldNotFoundSubtaskWhenIsGivenIdWhichDoesntExist() {
        FileBackedTaskManager.loadFromFile(tempFile);
        assertFalse(testObj.updateSubtask(new Subtask("Такой", "не существует")));
    }

    //Проверка, что задача создается и ее можно найти
    @Override
    @Test
    public void shouldCreateTaskAndFindIts() {
        FileBackedTaskManager.loadFromFile(tempFile);
        testObj.addTask(new Task("1", "2")); //id 4
        assertNotNull(testObj.receiveOneTask(4));
    }

    //Проверка, что Subtask и Epic создаются и могут быть найдены по id
    @Override
    @Test
    public void shouldCreateAndFindEpicAndSubtask() {
        FileBackedTaskManager.loadFromFile(tempFile);
        testObj.createEpic(new Epic("1", "2")); //id 4
        testObj.addSubTaskInEpic(4, new Subtask("Подзадача", "Подзадача")); // id 5
        assertNotNull(testObj.receiveOneEpic(4));
        assertNotNull(testObj.receiveSubtasksUseID(5));
    }

    //Изменение статуса эпика
    @Override
    @Test
    public void shouldChangeStatus() {
        FileBackedTaskManager.loadFromFile(tempFile);
        testObj.addSubTaskInEpic(2, new Subtask("Сходить в магазин", "Купить макароны")); //id 4
        Subtask subtask1 = new Subtask("Сходить в магазин", "Купить макароны");
        subtask1.setId(4);
        subtask1.setStatus("DONE");
        testObj.updateSubtask(subtask1);
        assertEquals("DONE", testObj.receiveOneEpic(2).get().getStatus().toString());
    }

    //Обновление статуса подзадачи
    @Override
    @Test
    public void shouldChangeStatusOfTask() {
        FileBackedTaskManager.loadFromFile(tempFile);
        Task taskTest = new Task("Смена", "Статуса");
        taskTest.setId(1);
        taskTest.setStatus("IN_PROGRESS");
        testObj.updateTask(taskTest);
        assertEquals("IN_PROGRESS", testObj.receiveOneTask(1).get().getStatus().toString());
    }

    //Проверка исключений
    @Test
    public void shouldThrowRightException() {
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            File file = new File("C:\\File.csv");
            FileBackedTaskManager.loadFromFile(file);
        });
        Assertions.assertDoesNotThrow(() ->{
            FileBackedTaskManager.loadFromFile(tempFile);
        });
    }

}
