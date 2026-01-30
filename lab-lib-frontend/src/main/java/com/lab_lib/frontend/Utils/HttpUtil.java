package com.lab_lib.frontend.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab_lib.frontend.Exceptions.ApiException;

import io.github.cdimascio.dotenv.Dotenv;
import com.google.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;

public class HttpUtil {
    private final HttpClient client;
    private final String baseUrl;
    private final ObjectMapper objectMapper;
    private final UserSession userSession;

    // Iniettare UserSession per intercettare autenticazione
    @Inject
    public HttpUtil(UserSession userSession) {
        Dotenv dotenv = Dotenv.load();
        String apiDomain = dotenv.get("API_DOMAIN");

        this.client = HttpClient.newHttpClient();
        this.baseUrl = apiDomain != null ? apiDomain : "http://localhost:8080"; // Default to localhost if not set
        this.objectMapper = new ObjectMapper();
        this.userSession = userSession;
    }

    public <T> T get(String endpoint, TypeReference<T> typeReference) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .GET();
            String auth = userSession.getAuthHeader();
            if (auth != null) builder.header("Authorization", auth);

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ApiException("HTTP " + response.statusCode() + " GET fallito: " + endpoint + " - corpo: " + response.body());
            }
            return objectMapper.readValue(response.body(), typeReference);
        } catch (IOException | InterruptedException e) {
            throw new ApiException("HTTP request failed: " + endpoint, e);
        }
    }

    public <T> T post(String endpoint, Object body, TypeReference<T> typeReference) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));
            String auth = userSession.getAuthHeader();
            if (auth != null) builder.header("Authorization", auth);
            HttpRequest request = builder.build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ApiException("HTTP " + response.statusCode() + " POST fallito: " + endpoint + " - corpo: " + response.body());
            }
            String contentType = response.headers().firstValue("Content-Type").orElse("");
            if (contentType.startsWith("text/plain") || contentType.startsWith("text/")) {
                // Return raw body for plain text responses
                return (T) response.body();
            }
            return objectMapper.readValue(response.body(), typeReference);
        } catch (IOException | InterruptedException e) {
            throw new ApiException("HTTP POST failed: " + endpoint, e);
        }
    }

    // For endpoints that return no JSON content (e.g., plain "Success")
    public void postVoid(String endpoint, Object body) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));
            String auth = userSession.getAuthHeader();
            if (auth != null) builder.header("Authorization", auth);
            HttpRequest request = builder.build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ApiException("HTTP " + response.statusCode() + " POST fallito: " + endpoint + " - corpo: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new ApiException("HTTP POST failed: " + endpoint, e);
        }
    }

    // Optional: Add a convenience method for non-generic POST responses
    public <T> T post(String endpoint, Object body, Class<T> responseType) {
        return post(endpoint, body, new TypeReference<T>() {});
    }

    public void deleteVoid(String endpoint) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .DELETE();
            String auth = userSession.getAuthHeader();
            if (auth != null) builder.header("Authorization", auth);
            HttpRequest request = builder.build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ApiException("HTTP " + response.statusCode() + " DELETE fallito: " + endpoint + " - corpo: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new ApiException("HTTP DELETE failed: " + endpoint, e);
        }
    }

    // Espone UserSession per gestire login/logout dal resto dell'app
    public UserSession getUserSession() {
        return userSession;
    }
}