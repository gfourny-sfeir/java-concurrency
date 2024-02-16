package fr.java.concurrency.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.java.concurrency.model.Beer;
import fr.java.concurrency.model.Preferences;
import fr.java.concurrency.model.Vodka;

public class Apis {

    private final static String BASE_URL = "http://localhost:8081";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public Apis() {
        this.httpClient = HttpClient.newBuilder().build();
        this.mapper = new ObjectMapper();
    }

    private static HttpRequest buildRequest(String requestUrl) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + requestUrl))
                .GET()
                .build();
    }

    public Preferences fetchPreferences() {
        final var request = buildRequest("/preferences");

        return sendHttpRequest(request, Preferences.class);
    }

    public Beer fetchBeer(Preferences pref) {

        final var request = buildRequest("/beer/" + pref.favoriteBeerType());

        return sendHttpRequest(request, Beer.class);
    }

    public Vodka fetchVodka() {

        final var request = buildRequest("/vodka");

        return sendHttpRequest(request, Vodka.class);
    }

    private <T> T sendHttpRequest(HttpRequest request, Class<T> clazz) {
        try {
            final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), clazz);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
