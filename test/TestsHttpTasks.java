import httpServer.HttpTaskServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


public class TestsHttpTasks {
    static URI uri;
    static HttpClient client;
    HttpTaskServer server = new HttpTaskServer();
    @BeforeAll
    public static void createClient() {
        uri = URI.create("http://localhost:8080/tasks");
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    @BeforeEach
    public void startServer() throws IOException {
        HttpTaskServer.startServer();
    }

    @AfterEach
    public void stopServer() throws IOException {
        HttpTaskServer.stopServer();
    }

    @Test
    public void shouldCreateTask() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"TestTask\",\"description\": \"TEST\"}"))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(201, response.statusCode());
    }

    @Test
    public void shouldGetTask() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"TestTask\",\"description\": \"TEST\"}"))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> responsePost = client.send(request, handlerPost);

        HttpRequest getAllTask = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handlerGet = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> responseGet = client.send(getAllTask, handlerGet);
        assertEquals(200, responseGet.statusCode());

        URI uriwithQuary = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest getTask = HttpRequest.newBuilder()
                .GET()
                .uri(uriwithQuary)
                .build();
        HttpResponse.BodyHandler<String> handlerGetOneTask = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> responseGetOneTask = client.send(getTask, handlerGetOneTask);
        assertEquals(200, responseGetOneTask.statusCode());
    }

    @Test
    public void shouldDeleteTasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"TestTask\",\"description\": \"TEST\"}"))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> responsePost = client.send(request, handlerPost);

        HttpRequest requestDelete = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerDelete = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> responseDelete = client.send(requestDelete, handlerDelete);
        assertEquals(200, responseDelete.statusCode());
    }
}






























