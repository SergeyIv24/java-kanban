
import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;



import static org.junit.jupiter.api.Assertions.*;

public class Test extends TaskManagerTests{
    Managers managers;
    InMemoryTaskManager testObj;
    HistoryManager testHistory;

    @Override
    @BeforeEach
    public void createTask1AndTask2() {
        managers = new Managers();
        testObj = managers.getDefault();
        testHistory = managers.getDefaultHistory();
        testObj.addTask(new Task("Полет", "Нормальный")); //id 1
        testObj.createEpic(new Epic("Тест", "Тест")); //id 2
        testObj.createEpic(new Epic("Тестовый", "Эпик")); //id 3
        testObj.receiveOneTask(1);
        testObj.receiveOneEpic(2);
        testObj.receiveOneEpic(3);
    }

    //Объекты обычные задачи с одинаковым id равны
    @Override
    @org.junit.jupiter.api.Test
    public void shouldBeEqualsWhenTheSameId() {
        assertSame(testObj.receiveOneTask(0), testObj.receiveOneTask(0), "Something went wrong");
    }

    //Наследники Task с одинаковы id равны
    @Override
    @org.junit.jupiter.api.Test
    public void shouldBeEqualsSubTasksWhenTheSameId() {
        assertSame(testObj.receiveOneEpic(2).get(), testObj.receiveOneEpic(2).get(), "Something went wrong");
    }

    //Утилитарный класс инициализирует HistoryManager
    @Override
    @org.junit.jupiter.api.Test
    public void managersShouldBeNotNullAfterInnit() {
        assertNotNull(testHistory);
    }

    //Утилитарный класс инициализирует TaskManager
    @Override
    @org.junit.jupiter.api.Test
    public void inMemoryTaskManagerShouldBeNotNullAfterInnit() {
        assertNotNull(testObj);
    }

    //Добавление subtask в несуществующий Epic.
    @Override
    @org.junit.jupiter.api.Test
    public void shouldBeNothingWhenAddSubtaskInEpicDoesntExist(){
        testObj.addSubTaskInEpic(4, new Subtask("Подзадача", "Не должна быть добавлена")); //id 4 нет
        //Проверка есть ли в одной из мап Subtask, относящийся к несуществующему эпику
        assertNull(testObj.getEpicTable().get(4), "Something went wrong");
        assertNull(testObj.getSubtaskTable().get(4), "Something went wrong");
        assertNull(testObj.getTaskTable().get(4), "Something went wrong");
    }

    //Проверка, что история существует
    @Override
    @org.junit.jupiter.api.Test
    public void historyShouldExist() {
        assertFalse(testObj.getHistory().getListOfHistory().isEmpty()); //В истории должно быть 3 элемента
    }

    //Проверка, что в истории хранится предыдущий элемент и последний
    @Override
    @org.junit.jupiter.api.Test
    public void shouldStorePreviousItemOfHistoryAndLastItem() {
        //Последний вызванный элемент был с id 3, должен быть равен объекту с тем же id из мапы
        assertTrue(testObj.getHistory().getListOfHistory().get(2).equals(testObj.getEpicTable().get(3)));
        assertTrue(testObj.getHistory().getListOfHistory().get(1).equals(testObj.getEpicTable().get(2)));

    }

    //Проверка, что история хранит только уникальные элементы
    @Override
    @org.junit.jupiter.api.Test
    public void shouldStoreUniqueThings() {
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
    @org.junit.jupiter.api.Test
    public void shouldRemoveItemsFromStart() {
        Task removingTaskFromStart = testObj.getHistory().getListOfHistory().get(0);
        testObj.getHistory().removeItem(1);
        assertFalse(testObj.getHistory().getListOfHistory().contains(removingTaskFromStart));
    }

    //Проверка удаления элементов истории c конца
    @Override
    @org.junit.jupiter.api.Test
    public void shouldRemoveItemFromEnd() {
        Task removingTaskFromEnd = testObj.getHistory().getListOfHistory().get(2);
        testObj.getHistory().removeItem(3);
        assertFalse(testObj.getHistory().getListOfHistory().contains(removingTaskFromEnd));
    }

    //Проверка, что при создании Task или наследников id уникален
    @Override
    @org.junit.jupiter.api.Test
    public void shouldUniqueIdWhenCreateTask() {
        //При создании любой задачи id увеличивается, поэтому всегда уникален
        testObj.createEpic(new Epic("Уникальность", "Id")); // id 4
        assertEquals(4, testObj.getCounter());
        testObj.createEpic(new Epic("Уникальность", "Id")); //id 5
        assertEquals(5, testObj.getCounter());
    }

    //Проверка, что обновляемая subtask, есть в хранилище
    @Override
    @org.junit.jupiter.api.Test
    public void shouldNotFoundSubtaskWhenIsGivenIdWhichDoesntExist() {
        assertFalse(testObj.updateSubtask(new Subtask("Такой", "не существует")));
    }

    //Проверка, что задача создается и ее можно найти
    @Override
    @org.junit.jupiter.api.Test
    public void shouldCreateTaskAndFindIts() {
        testObj.addTask(new Task("1", "2")); //id 4
        assertNotNull(testObj.receiveOneTask(4));
    }

    //Проверка, что Subtask и Epic создаются и могут быть найдены по id
    @Override
    @org.junit.jupiter.api.Test
    public void shouldCreateAndFindEpicAndSubtask() {
        testObj.createEpic(new Epic("1", "2")); //id 4
        testObj.addSubTaskInEpic(4, new Subtask("Подзадача", "Подзадача")); // id 5
        assertNotNull(testObj.receiveOneEpic(4));
        assertNotNull(testObj.receiveSubtasksUseID(5));
    }

    //Изменение статуса эпика
    @Override
    @org.junit.jupiter.api.Test
    public void shouldChangeStatus() {
        testObj.addSubTaskInEpic(2, new Subtask("Сходить в магазин", "Купить макароны")); //id 4
        Subtask subtask1 = new Subtask("Сходить в магазин", "Купить макароны");
        subtask1.setId(4);
        subtask1.setStatus("DONE");
        testObj.updateSubtask(subtask1);
        assertEquals("DONE", testObj.receiveOneEpic(2).get().getStatus().toString());
    }

    //Обновление статуса подзадачи
    @Override
    @org.junit.jupiter.api.Test
    public void shouldChangeStatusOfTask() {
        Task taskTest = new Task("Смена", "Статуса");
        taskTest.setId(1);
        taskTest.setStatus("IN_PROGRESS");
        testObj.updateTask(taskTest);
        assertEquals("IN_PROGRESS", testObj.receiveOneTask(1).get().getStatus().toString());
    }



}
