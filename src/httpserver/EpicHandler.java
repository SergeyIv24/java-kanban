package httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;
import java.io.IOException;
import java.util.Optional;

import static httpserver.HttpTaskServer.gsonBuilder;
import static httpserver.HttpTaskServer.manager;
import static httpserver.HttpTaskServer.DEFAULT_CHARSET;
import static httpserver.HttpTaskServer.requestBodyWriter;

public class EpicHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        switch (requestMethod) {
            case "GET":
                Optional<Integer> idForGetting = HttpTaskServer.parseId(exchange); //id эпика
                if (idForGetting.isEmpty()) {
                    requestBodyWriter(exchange, 404, "Эпик не существует");
                    return;
                }
                Optional<Epic> epic = manager.receiveOneEpic(idForGetting.get());
                if (epic.isPresent()) {
                    String epicJson = gsonBuilder.toJson(epic.get().toString());
                    requestBodyWriter(exchange, 200, epicJson);
                }
                requestBodyWriter(exchange, 404, "Эпик не существует");
                return;

            case "POST":
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                Epic epicAdding = gsonBuilder.fromJson(requestBody, Epic.class); //Десириализация
                manager.createEpic(epicAdding);
                requestBodyWriter(exchange, 201, "");
                return;

            case "DELETE":
                Optional<Integer> idForDeleting = HttpTaskServer.parseId(exchange); //id задачи
                Optional<Epic> epicDel = manager.receiveOneEpic(idForDeleting.get());
                if (epicDel.isEmpty()) {
                    requestBodyWriter(exchange, 404, "Эпик не существует");
                    return;
                }
                manager.deleteEpic(idForDeleting.get());
                requestBodyWriter(exchange, 200, "");
        }
    }
}
