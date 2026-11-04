package org.example.lidyvariedadesdesktop.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.lidyvariedadesdesktop.Cliente;

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


public class ClienteService {

    private static final String BASE_URL="http://localhost:8080";
    private final HttpClient client;
    private final Gson gson;

    public ClienteService(){
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

    public List<Cliente> listarTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clientes"))
                .GET()
                .header("Accept", "application/json")
                .header("X-Desktop-Auth", ApiCredentials.headerValue())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if(response.statusCode() == 200) {
            Type listType = new TypeToken<List<Cliente>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else{
            throw new RuntimeException("Erro ao buscar o cliente. Status:" + response.statusCode());
        }
    }

    public List<Cliente> buscar(String nome) throws IOException, InterruptedException {
        String encodedNome = URLEncoder.encode(nome, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clientes/buscar?nome=" + encodedNome))
                .GET()
                .header("Accept", "application/json")
                .header("X-Desktop-Auth", ApiCredentials.headerValue())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200) {
            Type listType = new TypeToken<List<Cliente>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else{
            throw new RuntimeException("Erro ao buscar o cliente por nome. Status:" + response.statusCode());
        }
    }

    public Cliente cadastrar(Cliente cliente) throws IOException, InterruptedException {
        String jsonBody = gson.toJson(cliente);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clientes"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-Desktop-Auth", ApiCredentials.headerValue())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 201 || response.statusCode() == 200){
            return gson.fromJson(response.body(), Cliente.class);
        } else{
            throw new RuntimeException("Erro ao cadastrar o cliente. Status:" + response.statusCode());
        }
    }

    public boolean excluir(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clientes/" + id))
                .header("X-Desktop-Auth", ApiCredentials.headerValue())
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 204 || response.statusCode() == 200){   
            return true;
        } else{
            throw new RuntimeException("Erro ao excluir o cliente. Status:" + response.statusCode());
        }
    }

    public Cliente atualizar(Long id, Cliente cliente) throws IOException, InterruptedException {
        String jsonBody = gson.toJson(cliente);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clientes/" + id))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-Desktop-Auth", ApiCredentials.headerValue())
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){
            return gson.fromJson(response.body(), Cliente.class);
        } else{
            throw new RuntimeException("Erro ao atualizar o cliente. Status:" + response.statusCode());
        }
    }

}
