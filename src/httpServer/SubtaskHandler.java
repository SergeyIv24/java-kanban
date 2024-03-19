package httpServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import httpServer.HttpTaskServer;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.Optional;

import static httpServer.HttpTaskServer.gsonBuilder;
import static httpServer.HttpTaskServer.manager;
import static httpServer.HttpTaskServer.DEFAULT_CHARSET;
import static httpServer.HttpTaskServer.requestBodyWriter;

public class SubtaskHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                Optional<Integer> idForGetting = HttpTaskServer.parseId(exchange); //id задачи
                Headers epicIdInHeaders = exchange.getRequestHeaders();
                int epicId = 0;

                if (epicIdInHeaders.containsKey("X-epicId")) {
                    epicId = Integer.parseInt(epicIdInHeaders.get("X-epicId").get(0));
                }

                if (manager.receiveOneEpic(epicId).isEmpty()
                        || manager.receiveSubtasksUseID(idForGetting.get()).isEmpty()) { //Если эпика нет, то и задач нет
                    requestBodyWriter(exchange, 404, "Задача не существует");
                    return;
                }

                requestBodyWriter(exchange, 200, gsonBuilder.toJson(manager.receiveSubtasksUseID(idForGetting.get())));
                return;


            case "POST":
                //В InMemoryTaskManager метод addSubtask(int epicId, Subtask subtask), принимает id эпика к которому
                //относится. Для POST запроса на создание подзадачи epicId передается в заголовке запроса.
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                Headers epicIdInHeadersForAdd = exchange.getRequestHeaders();
                int epicIdForAdd = 0;
                if (epicIdInHeadersForAdd.containsKey("X-epicId")) {
                    epicIdForAdd = Integer.parseInt(epicIdInHeadersForAdd.get("X-epicId").get(0));
                }
                Optional<Integer> idForPosting = HttpTaskServer.parseId(exchange); //id задачи
                Subtask subtaskAdding = gsonBuilder.fromJson(requestBody, Subtask.class); //Десириализация
                if (idForPosting.isEmpty()) {
                    manager.addSubTaskInEpic(epicIdForAdd, subtaskAdding);
                    requestBodyWriter(exchange, 201, "");
                    return;
                }

                if (manager.receiveSubtasksUseID(idForPosting.get()).isEmpty()) {
                    requestBodyWriter(exchange, 404, "Задача не существует");
                    return;
                }
                manager.updateTask(subtaskAdding);
                requestBodyWriter(exchange, 201, "");
                return;

            case "DELETE":
                Optional<Integer> idForDeleting = HttpTaskServer.parseId(exchange); //id задачи
                if (idForDeleting.isEmpty()) {
                    requestBodyWriter(exchange, 404, "Задача не существует");
                    return;
                }
                Optional<Subtask> subtask = manager.receiveSubtasksUseID(idForDeleting.get());

                if (subtask.isEmpty()) {
                    requestBodyWriter(exchange, 404, "Задача не существует");
                    return;
                }
                manager.deleteParticularSubtask(idForDeleting.get());
                requestBodyWriter(exchange, 201, "");
                return;
        }

    }
}
