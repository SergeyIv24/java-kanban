package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class HttpTaskServer {

    public static final int PORT = 8080; //Порт программы
    public static FileBackedTaskManager manager;
    public static Gson gson = new Gson();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    public static void main(String[] args) throws IOException {
        //manager = Managers.getFileBackedTaskManager(File.createTempFile("file.csv", null));
        File file = new File("C:\\Учеба\\Java 2023 - 2024" +
                "\\Задачи\\Проекты ЯП\\Спринт 4\\java-kanban\\src\\manager\\File.csv");
        manager = Managers.getFileBackedTaskManager(file);



        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0); //Сервер + привязка порта
                                                                                        //совет + бэклог
        server.createContext("/tasks", new TasksHandler()); //Связь пути и обработчика
        server.createContext("/subtasks", new SubtaskHandler());
        server.createContext("/epics", new EpicHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedHandler());
        server.start(); // Запуск сервера
    }




   private static Optional<Integer> parseId(HttpExchange exchange) {
       URI requestURI = exchange.getRequestURI(); //URI
       String path = requestURI.getPath(); // Путь
       String[] pathArray = path.split("/");

       if (pathArray.length != 2) { // Если пользователь передал id в параметрах пути
            return Optional.of(Integer.parseInt(pathArray[2])); //Возврат id в Optional
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
                        manager.receiveAllTasks(); //Возврат всех задач
                        String allTasksJson = gson.toJson(manager.receiveAllTasks());
                        requestBodyWriter(exchange, 200, allTasksJson);
                        return;
                    }

                    Optional<Task> task = manager.receiveOneTask(idForGetting.get()); //Получение задачи по id

                    if (task.isEmpty()) { //Если задачи нет
                        requestBodyWriter(exchange, 404, "Задача не существует");
                        return;
                    }

                    String taskJson = gson.toJson(task.get()); //Возврат запрашиваемой задачи
                    requestBodyWriter(exchange, 200, taskJson);
                    return;


                case "POST":
                    Optional<Integer> idForPosting = HttpTaskServer.parseId(exchange); //id задачи
                    if (idForPosting.isEmpty()) {


                    }


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

class ListTokenType extends TypeToken<List<Task>> {

}
