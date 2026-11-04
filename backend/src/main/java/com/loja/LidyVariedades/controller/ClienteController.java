package com.loja.LidyVariedades.controller;
import com.loja.LidyVariedades.model.Cliente;
import com.loja.LidyVariedades.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(value = "/clientes", produces= "application/json")

public class ClienteController {
    @Autowired private ClienteRepository clienteRepository;
    @GetMapping 
    public List<Cliente> listarTodos(){
        return clienteRepository.findAll();
    }

    @GetMapping("/buscar")
    public List<Cliente> buscarNome(@RequestParam String nome){
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id){
        return clienteRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(clienteRepository.save(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente){
        return clienteRepository.findById(id)
            .map(clienteExistente -> {
                clienteExistente.setNome(cliente.getNome());
                clienteExistente.setTelefone(cliente.getTelefone());
                clienteExistente.setEndereco(cliente.getEndereco());
                return ResponseEntity.ok(clienteRepository.save(clienteExistente));
            })
            .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        if(!clienteRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
