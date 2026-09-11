package com.loja.LidyVariedades.controller;

import com.loja.LidyVariedades.model.Categoria;
import com.loja.LidyVariedades.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/categorias", produces = "application/json")
public class CategoriaController {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> listarTodas(){
        return categoriaRepository.findAll();
    }
    
}