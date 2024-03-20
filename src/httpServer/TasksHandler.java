package httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;
import java.io.IOException;
import java.util.Optional;
import static httpServer.HttpTaskServer.gsonBuilder;
import static httpServer.HttpTaskServer.manager;
import static httpServer.HttpTaskServer.DEFAULT_CHARSET;
import static httpServer.HttpTaskServer.requestBodyWriter;


public class TasksHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod(); //Метод
        switch (requestMethod) {
            case "GET":
                Optional<Integer> idForGetting = HttpTaskServer.parseId(exchange); //id задачи
                if (idForGetting.isEmpty()) { //Если id нет
                    String allTasksJson = gsonBuilder.toJson(manager.receiveAllTasks().toString());
                    requestBodyWriter(exchange, 200, allTasksJson);
                    return;
                }

                Optional<Task> task = manager.receiveOneTask(idForGetting.get()); //Получение задачи по id

                if (task.isEmpty()) { //Если задачи нет
                    requestBodyWriter(exchange, 404, "Задача не существует");
                    return;
                }
                String task1 = task.get().toString();

                String taskJson = gsonBuilder.toJson(task1); //Возврат запрашиваемой задачи
                requestBodyWriter(exchange, 200, taskJson);
                return;

            case "POST":
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                Optional<Integer> idForPosting = HttpTaskServer.parseId(exchange); //id задачи
                Task taskAdding = gsonBuilder.fromJson(requestBody, Task.class); //Десириализация
                if (idForPosting.isEmpty()) { //Если id не передан
                    if (!manager.addTask(taskAdding)) { //Если есть пересечение по времени
                        requestBodyWriter(exchange, 406, "Задача пересекается с существующей");
                        return;
                    }
                    requestBodyWriter(exchange, 201, ""); //Пересечений нет
                    return;
                }

                //id передан
                taskAdding.setId(idForPosting.get());
                if (manager.updateTask(taskAdding)) {
                    requestBodyWriter(exchange, 201, "");
                    return;
                }
                requestBodyWriter(exchange, 404, "Задача не существует");
                return;

            case "DELETE":
                Optional<Integer> idForDeleting = HttpTaskServer.parseId(exchange); //id задачи
                if (idForDeleting.isEmpty()) { //Если нет id, удаление всех задач
                    manager.deleteAllTask();
                    requestBodyWriter(exchange, 200, ""); //Удаление задач возвращает void
                    return;
                }

                Optional<Task> taskForDeleting = manager.receiveOneTask(idForDeleting.get()); //Получение задачи по id

                if (taskForDeleting.isEmpty()) { //Если задачи не существует
                    requestBodyWriter(exchange, 404, "Задача не существует");
                    return;
                }
                manager.deleteUseID(idForDeleting.get());
                requestBodyWriter(exchange, 200, ""); //Задача удалена по id (void)
        }
    }
}
