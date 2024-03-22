package httpserver;

import com.google.gson.*;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTaskManager;
import manager.Managers;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class HttpTaskServer {

    public static final int PORT = 8080; //Порт программы
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static FileBackedTaskManager manager;
    static HttpServer server;
    public static File file = new File("C:\\Учеба\\Java 2023 - 2024" +
            "\\Задачи\\Проекты ЯП\\Спринт 4\\java-kanban\\src\\manager\\File.csv");
    public static Gson gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTimeAdapter())
            .create();


    public static void startServer() throws IOException {
        manager = Managers.getFileBackedTaskManager(file);
        server = HttpServer.create(new InetSocketAddress(PORT), 0); //Сервер + привязка порта
        server.createContext("/tasks", new TasksHandler()); //Связь пути и обработчика
        server.createContext("/subtasks", new SubtaskHandler());
        server.createContext("/epics", new EpicHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedHandler());
        server.start();
    }

    public static void stopServer() {
        server.stop(1);
    }



    public static void main(String[] args) throws IOException {
        startServer(); // Запуск сервера
    }

   protected static Optional<Integer> parseId(HttpExchange exchange) {
       URI requestURI = exchange.getRequestURI(); //URI
       String parameters = requestURI.getQuery();
       if (parameters != null) { //Если параметры пути есть
           String idStr = parameters.split("=")[1];
           return Optional.of(Integer.parseInt(idStr));
       }
       return Optional.empty(); //Если id нет пустой Optional
   }

   protected static void requestBodyWriter(HttpExchange exchange, int rCode, String body) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(rCode, 0);
            os.write(body.getBytes(DEFAULT_CHARSET));
        }
   }
}

