import manager.TaskManager;

import java.io.IOException;

abstract class TaskManagerTests <T extends TaskManager> {

    abstract void createTask1AndTask2() throws IOException;
    abstract void shouldBeEqualsWhenTheSameId();
    abstract void shouldBeEqualsSubTasksWhenTheSameId();
    abstract void managersShouldBeNotNullAfterInnit();
    abstract void inMemoryTaskManagerShouldBeNotNullAfterInnit();

    abstract void shouldBeNothingWhenAddSubtaskInEpicDoesntExist();
    abstract void historyShouldExist();
    abstract void shouldStorePreviousItemOfHistoryAndLastItem();
    abstract void shouldStoreUniqueThings();
    abstract void shouldRemoveItemsFromStart();
    abstract void shouldRemoveItemFromEnd();
    abstract void shouldUniqueIdWhenCreateTask();
    abstract void shouldNotFoundSubtaskWhenIsGivenIdWhichDoesntExist();
    abstract void shouldCreateTaskAndFindIts();
    abstract void shouldCreateAndFindEpicAndSubtask();
    abstract void shouldChangeStatus();
    abstract void shouldChangeStatusOfTask();





}
