package com.loja.LidyVariedades.controller;

import java.util.List;

import com.loja.LidyVariedades.model.ProdutoCatalogoResponse;
import com.loja.LidyVariedades.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/catalogo/produtos", produces = "application/json")
public class CatalogoProdutoController {

    private final ProdutoRepository produtoRepository;

    public CatalogoProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public List<ProdutoCatalogoResponse> listarCatalogo() {
        return produtoRepository.findAll()
            .stream()
            .map(ProdutoCatalogoResponse::from)
            .toList();
    }
}
