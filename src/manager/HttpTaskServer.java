package manager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasks.Constants;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class HttpTaskServer {

    public static final int PORT = 8080; //Порт программы
    public static FileBackedTaskManager manager;
    public static Gson gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTimeAdapter())
            //.registerTypeAdapter(StatusOfTask.class, new StatusAdapter())
            .create();

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Учеба\\Java 2023 - 2024" +
                "\\Задачи\\Проекты ЯП\\Спринт 4\\java-kanban\\src\\manager\\File.csv");
        manager = Managers.getFileBackedTaskManager(file);
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0); //Сервер + привязка порта
        server.createContext("/tasks", new TasksHandler()); //Связь пути и обработчика
        server.createContext("/subtasks", new SubtaskHandler());
        server.createContext("/epics", new EpicHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedHandler());
        server.start(); // Запуск сервера
    }

   private static Optional<Integer> parseId(HttpExchange exchange) {
       URI requestURI = exchange.getRequestURI(); //URI
       String parameters = requestURI.getQuery();
       if (parameters != null) { //Если параметры пути есть
           String idStr = parameters.split("=")[1];
           return Optional.of(Integer.parseInt(idStr));
       }
       return Optional.empty(); //Если id нет пустой Optional
   }

   private static void requestBodyWriter(HttpExchange exchange, int rCode, String body) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(rCode, 0); //Ошибка
            os.write(body.getBytes(DEFAULT_CHARSET));
        }
   }

    //Обработчик задач
    static class TasksHandler implements HttpHandler {
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

                    Task task = manager.receiveOneTask(idForGetting.get()).get(); //Получение задачи по id

                    if (task == null) { //Если задачи нет
                        requestBodyWriter(exchange, 404, "Задача не существует");
                        return;
                    }

                    String taskJson = gsonBuilder.toJson(task); //Возврат запрашиваемой задачи
                    requestBodyWriter(exchange, 200, taskJson);
                    return;










                case "POST":
                    String requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    Optional<Integer> idForPosting = HttpTaskServer.parseId(exchange); //id задачи
                    Task taskAdding = gsonBuilder.fromJson(requestBody, Task.class); //Десириализация
                    System.out.println(taskAdding.toString()); //Todo check working
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

    static class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                case "POST":
                case "DELETE":
            }
        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                case "POST":
                case "DELETE":
            }
        }
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                case "POST":
                case "DELETE":
            }
        }
    }

    static class PrioritizedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                case "POST":
                case "DELETE":
            }
        }
    }
}

class LocalDataTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.value(value.format(Constants.FORMATTER));
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), Constants.FORMATTER);
    }
}

class DurationTimeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration value) throws IOException {
        jsonWriter.value(value.toString());
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
    }
}

/*class StatusAdapter extends TypeAdapter<StatusOfTask> {
    @Override
    public void write(JsonWriter jsonWriter, StatusOfTask value) throws IOException {
        jsonWriter.value(value.toString());

    }

    @Override
    public StatusOfTask read(JsonReader jsonReader) throws IOException {
        return StatusOfTask.valueOf(jsonReader.nextString());
    }
}*/






/*class Adapter extends TypeAdapter<Task> {

    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {

    }

    @Override
    public Task read(JsonReader jsonReader) throws IOException {
        return new Task(jsonReader.toString(), jsonReader.toString());
    }
}*/

class ListTokenType extends TypeToken<List<Task>> {

}
