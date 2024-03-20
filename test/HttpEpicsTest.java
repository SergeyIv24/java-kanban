import httpserver.HttpTaskServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class HttpEpicsTest {
    static URI uri;
    static HttpClient client;

    @BeforeEach
    public void createClient() {
        uri = URI.create("http://localhost:8080/epics");
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @BeforeEach
    public void startServer() throws IOException {
        HttpTaskServer.file = File.createTempFile("File.csv", null);
        HttpTaskServer.startServer();
    }

    @AfterEach
    public void stopServer() {
        HttpTaskServer.stopServer();
    }

    @Test
    public void shouldCreateEpic() throws IOException, InterruptedException {
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"TestEpic\",\"description\": \"TEST\"}"))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestPost, handlerPost);
        assertEquals(201, response.statusCode());
    }

    @Test
    public void shouldGetEpic() throws IOException, InterruptedException {
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"TestEpic\",\"description\": \"TEST\"}"))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        client.send(requestPost, handlerPost);

        URI uriWithQuary = URI.create("http://localhost:8080/epics?id=1");
        HttpRequest requestGet = HttpRequest.newBuilder()
                .GET().uri(uriWithQuary)
                .build();
        HttpResponse.BodyHandler<String> handlerGet = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> responseGet = client.send(requestGet, handlerGet);
        assertEquals(200, responseGet.statusCode());
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"TestEpic\",\"description\": \"TEST\"}"))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        client.send(requestPost, handlerPost);

        URI uriWithQuary = URI.create("http://localhost:8080/epics?id=1");
        HttpRequest requestDelete = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriWithQuary)
                .build();
        HttpResponse.BodyHandler<String> handlerDelete = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> responseDelete = client.send(requestDelete, handlerDelete);
        assertEquals(200, responseDelete.statusCode());
    }


}
