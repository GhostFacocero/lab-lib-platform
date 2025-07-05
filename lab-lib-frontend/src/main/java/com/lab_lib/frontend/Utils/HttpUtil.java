package com.lab_lib.frontend.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab_lib.frontend.Exceptions.ApiException;

import io.github.cdimascio.dotenv.Dotenv;

import com.fasterxml.jackson.core.type.TypeReference;

public class HttpUtil {
    private final HttpClient client;
    private final String baseUrl;
    private final ObjectMapper objectMapper;

    public HttpUtil() {
        Dotenv dotenv = Dotenv.load();
        String apiDomain = dotenv.get("API_DOMAIN");

        this.client = HttpClient.newHttpClient();
        this.baseUrl = apiDomain != null ? apiDomain : "http://localhost:8080"; // Default to localhost if not set
        this.objectMapper = new ObjectMapper();
    }

    public <T> T get(String endpoint, TypeReference<T> typeReference) {
        try {
            HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .GET()
                    .build(),
                HttpResponse.BodyHandlers.ofString()
            );
            return objectMapper.readValue(response.body(), typeReference);
        } catch (IOException | InterruptedException e) {
            throw new ApiException("HTTP request failed: " + endpoint, e);
        }
    }

    public <T> T post(String endpoint, Object body, TypeReference<T> typeReference) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), typeReference);
        } catch (IOException | InterruptedException e) {
            throw new ApiException("HTTP POST failed: " + endpoint, e);
        }
    }

    // Optional: Add a convenience method for non-generic POST responses
    public <T> T post(String endpoint, Object body, Class<T> responseType) {
        return post(endpoint, body, new TypeReference<T>() {});
    }
}