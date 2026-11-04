package org.example.lidyvariedadesdesktop.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.lidyvariedadesdesktop.Venda;
import org.example.lidyvariedadesdesktop.VendaRequest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class VendaService {
    private static final String BASE_URL="http://localhost:8080";
    private final HttpClient client;
    private final Gson gson;

    public VendaService() {
        this.client = HttpClient.newHttpClient();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json,type, ctx) -> {
            try {
                return LocalDateTime.parse(json.getAsString());
            } catch (Exception e) {
                return null;
            }
        })
         .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, type, ctx) ->
                new JsonPrimitive(src != null ? src.toString() : ""))
                .create();
    }

     public boolean finalizarVenda(VendaRequest request) throws IOException, InterruptedException {
        String jsonBody = gson.toJson(request);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/vendas"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-Desktop-Auth", ApiCredentials.headerValue())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201 || response.statusCode() == 200) {
            return true;
        } else {
            throw new RuntimeException("Erro ao finalizar a venda. Status: " + response.statusCode() + " - " + response.body());
        }
    }

    public List<Venda> listarTodas() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/vendas"))
                .GET()
                .header("Accept", "application/json")
                .header("X-Desktop-Auth", ApiCredentials.headerValue())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type listType = new TypeToken<List<Venda>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Erro ao buscar histórico de vendas. Status: " + response.statusCode());
        }
    }
}
