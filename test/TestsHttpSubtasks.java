import httpserver.HttpTaskServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


public class TestsHttpSubtasks {
    static URI uri;
    static HttpClient client;
    HttpTaskServer server = new HttpTaskServer();

    @BeforeAll
    public static void createClient() {
        uri = URI.create("http://localhost:8080/subtasks");
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
    public void shouldCreateSubTask() throws IOException, InterruptedException {
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"TestTask\",\"description\": \"TEST\"}"))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestPost, handlerPost);
        assertEquals(201, response.statusCode());
    }

    @Test
    public void shouldGetSubtask() throws IOException, InterruptedException {
        System.out.println();
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name1\": \"TestTask\",\"description\": \"TEST\"}"))
                .uri(uri)
                .header("X-epicId", "1")
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> responsePost = client.send(requestPost, handlerPost);

        URI uriWithQuare = URI.create("http://localhost:8080/subtasks?id=2");
        HttpRequest getSubtask = HttpRequest.newBuilder()
                .GET()
                .uri(uriWithQuare)
                .build();

        HttpResponse.BodyHandler<String> handlerGet = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> responseGet = client.send(getSubtask, handlerGet);
        assertEquals(404, responseGet.statusCode()); //Эпик не создан, задача не может существовать

    }

}