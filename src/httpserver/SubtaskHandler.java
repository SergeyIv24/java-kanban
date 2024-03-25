package httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Subtask;
import java.io.IOException;
import java.util.Optional;
import static httpserver.HttpTaskServer.gsonBuilder;
import static httpserver.HttpTaskServer.manager;
import static httpserver.HttpTaskServer.DEFAULT_CHARSET;
import static httpserver.HttpTaskServer.requestBodyWriter;
import static httpserver.HttpConstance.*;

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
                    requestBodyWriter(exchange, NOT_FOUND, "Задача не существует");
                    return;
                }
                int idSub = idForGetting.get();
                String subtaskForGetting = gsonBuilder.toJson(manager.receiveSubtasksUseID(idSub).get().toString());

                requestBodyWriter(exchange, OK, gsonBuilder.toJson(subtaskForGetting));
                return;

            case "POST":
                //В InMemoryTaskManager метод addSubtask(int epicId, Subtask subtask), принимает id эпика к которому
                //относится. Для POST запроса на создание подзадачи epicId передается в заголовке запроса.
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                Headers epicIdInHeadersForAdd = exchange.getRequestHeaders();
                Optional<Integer> idForPosting = HttpTaskServer.parseId(exchange); //id задачи
                Subtask subtaskAdding = gsonBuilder.fromJson(requestBody, Subtask.class); //Десириализация

                int epicIdForAdd = 0;
                if (epicIdInHeadersForAdd.containsKey("X-epicId")) { //Если содержит epicId
                    epicIdForAdd = Integer.parseInt(epicIdInHeadersForAdd.get("X-epicId").get(0));
                }

                if (idForPosting.isEmpty()) { //Если id нет
                    if (!manager.addSubTaskInEpic(epicIdForAdd, subtaskAdding)) {
                        requestBodyWriter(exchange, BAD_REQUEST, "Задача пересекается с существующей");
                        return;
                    }
                    requestBodyWriter(exchange, CREATED, ""); //Пересечений нет
                    return;
                }
                subtaskAdding.setId(idForPosting.get());
                boolean isUpdated = manager.updateSubtask(subtaskAdding);
                if (!isUpdated) {
                    requestBodyWriter(exchange, BAD_REQUEST, "Задача пересекается с существующей");
                    return;
                }
                requestBodyWriter(exchange, CREATED, ""); //Пересечений нет
                return;

            case "DELETE":
                Optional<Integer> idForDeleting = HttpTaskServer.parseId(exchange); //id задачи
                int id = idForDeleting.get();
                Optional<Subtask> subtaskForDeleting = manager.receiveSubtasksUseID(idForDeleting.get()); //Получение задачи по id

                if (subtaskForDeleting.isEmpty()) { //Если задачи не существует
                    requestBodyWriter(exchange, NOT_FOUND, "Задача не существует");
                    return;
                }
                manager.deleteParticularSubtask(id);
                requestBodyWriter(exchange, OK, ""); //Задача удалена по id (void)
        }
    }
}
