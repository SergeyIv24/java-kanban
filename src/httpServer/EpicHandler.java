package httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;
import tasks.Task;

import java.io.IOException;
import java.util.Optional;

import static httpServer.HttpTaskServer.gsonBuilder;
import static httpServer.HttpTaskServer.manager;
import static httpServer.HttpTaskServer.DEFAULT_CHARSET;
import static httpServer.HttpTaskServer.requestBodyWriter;

public class EpicHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        switch (requestMethod) {
            case "GET":
            case "POST":
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                Epic epicAdding = gsonBuilder.fromJson(requestBody, Epic.class); //Десириализация
                System.out.println(epicAdding);
                System.out.println(epicAdding.getSubtasks());
                manager.createEpic(epicAdding);

                requestBodyWriter(exchange, 201, "");
                return;

            case "DELETE":
        }
    }
}
