package org.example.lidyvariedadesdesktop.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.lidyvariedadesdesktop.Categoria;
import org.example.lidyvariedadesdesktop.Marca;
import org.example.lidyvariedadesdesktop.Produto;

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

public class ProdutoService {

    private static final String BASE_URL="http://localhost:8080";
    private final HttpClient client;
    private final Gson gson;


    public ProdutoService() {
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


    public List<Produto> listarTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/produtos"))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200) {
            Type listType = new TypeToken<List<Produto>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else{
            throw new RuntimeException("Erro ao buscar o produto. Status:" + response.statusCode());
        }

    }

    // buscar o nome do produto
    public List<Produto> buscarPorNome(String nome) throws IOException, InterruptedException{
       String encodedNome = URLEncoder.encode(nome,StandardCharsets.UTF_8);
       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(BASE_URL + "/produtos/buscar?nome=" + encodedNome))
               .GET()
               .header("Accept", "application/json")
               .build();

       HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

       if(response.statusCode() == 200){
           Type listType = new TypeToken<List<Produto>>() {}.getType();
           return gson.fromJson(response.body(), listType);
       } else{
           throw new RuntimeException("Erro ao buscar o produto. Status:" +  response.statusCode());
       }
    }


    // cadastrar novo usuario.
    public Produto cadastrar(Produto produto) throws IOException, InterruptedException{
        String jsonBody = gson.toJson(produto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/produtos"))
                .header("content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 201 || response.statusCode() == 200){
            return gson.fromJson(response.body(), Produto.class);
        } else {
            throw new RuntimeException("Erro ao cadastrar o Produto. Status: " + response.statusCode() + " - " + response.body());
        }
    }

    // Atualizar o produto

    public Produto atualizar(Long id, Produto produto) throws IOException, InterruptedException{
        String jsonBody = gson.toJson(produto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/produtos/" + id))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){
            return gson.fromJson(response.body(), Produto.class);
        } else{
            throw new RuntimeException("Erro ao atualizar o Produto. Status: " + response.statusCode() + " - " + response.body());
        }
    }

    // excluir o produto

    public boolean excluir(Long id) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/produtos/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    return response.statusCode() == 204 || response.statusCode() == 200;
    }


    public List<Categoria> listarCategorias(){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/categorias"))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200){
                Type listType = new TypeToken<List<Categoria>>() {}.getType();
                return gson.fromJson(response.body(), listType);
            }
        } catch(Exception e){
            System.err.println("Aviso: não foi possível carregar categorias: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Marca> listarMarcas(){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/marcas"))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200){
                Type listType = new TypeToken<List<Marca>>() {}.getType();
                return gson.fromJson(response.body(), listType);
            }
        } catch(Exception e){
            System.err.println("Aviso: Não foi possivel carregar marcas: " + e.getMessage());
        }
        return new ArrayList<>();
    }

}
