package httpserver;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import static httpserver.HttpTaskServer.gsonBuilder;
import static httpserver.HttpTaskServer.manager;
import static httpserver.HttpTaskServer.requestBodyWriter;

public class HistoryHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        switch (requestMethod) {
            case "GET":
                requestBodyWriter(exchange, 200, gsonBuilder.toJson(manager.getHistory().getListOfHistory().toString()));
        }
    }

}
