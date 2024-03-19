package manager;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasks.Constants;
import tasks.StatusOfTask;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HttpServer1 {
    public static final int PORT = 8080; //Порт программы
    public static FileBackedTaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTimeAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Учеба\\Java 2023 - 2024" +
                "\\Задачи\\Проекты ЯП\\Спринт 4\\java-kanban\\src\\manager\\File.csv");
        manager = Managers.getFileBackedTaskManager(file);
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0); //Сервер + привязка порта
        server.createContext("/tasks", new TaskHandler()); //Связь пути и обработчика
        server.start();



    }

    static class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod(); //Метод

            switch (requestMethod) {
                case "POST":
                    //String requestBody = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    String requestBody = "{\"name\": \"test\",\"description\": \"tes\"}";
                    JsonElement jsonElement = JsonParser.parseString(requestBody);
                    TaskTest task = gson.fromJson(requestBody, TaskTest.class);
                    System.out.println(task.name);
                    System.out.println(task.description);
                    System.out.println(task.id);
                    return;
            }

        }
    }


    static class LocalDataTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            out.value(value.format(Constants.FORMATTER));


        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), Constants.FORMATTER);
        }
    }

    static class DurationTimeAdapter extends TypeAdapter<Duration> {


        @Override
        public void write(JsonWriter jsonWriter, Duration value) throws IOException {
            jsonWriter.value(value.toString());
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
        }
    }



    static class TaskTest {

        public String name;
        public String description;
        @Expose (deserialize = false)
        protected int id;
        @Expose (deserialize = false)
        protected StatusOfTask status;
        @Expose (deserialize = false)
        protected Duration duration; //Продолжительность в минутах
        @Expose (deserialize = false)
        protected LocalDateTime startTime; //Время начала задачи
        @Expose (deserialize = false)
        protected LocalDateTime endTime;

        public TaskTest(String name, String description) {
            this.name = name;
            this.description = description;
            status = StatusOfTask.NEW; //Как только задача создана, она новая.
        }
        public TaskTest(String name, String description, long minutes, String startTime) {
            this.name = name;
            this.description = description;
           status = StatusOfTask.NEW; //Как только задача создана, она новая.
            duration = Duration.ofMinutes(minutes);
            this.startTime = LocalDateTime.parse(startTime, Constants.FORMATTER);
        }

    }

}
//JsonElement jsonElement = JsonParser.parseString(requestBody);
//Task taskAdding = new Task(object.get("name").getAsString(), object.get("des").getAsString());