package com.example.inventory.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/products";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<ProductRow> list() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL)).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(), new TypeReference<List<ProductRow>>() {});
    }

    public ProductRow create(ProductRow product) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(mapper.writeValueAsString(product)))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(), ProductRow.class);
    }

    public ProductRow update(Long id, ProductRow product) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(mapper.writeValueAsString(product)))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(), ProductRow.class);
    }

    public void delete(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        check(response);
    }

    private void check(HttpResponse<String> response) throws IOException {
        if (response.statusCode() >= 400) {
            throw new IOException("HTTP error " + response.statusCode() + ": " + response.body());
        }
    }
}
