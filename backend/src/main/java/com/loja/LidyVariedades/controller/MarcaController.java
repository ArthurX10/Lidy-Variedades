package com.loja.LidyVariedades.controller;
import com.loja.LidyVariedades.model.Marca;
import com.loja.LidyVariedades.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/marcas", produces = "application/json")
public class MarcaController {
    
    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    public List<Marca> listarTodas(){
        return marcaRepository.findAll();
    }
    
}
