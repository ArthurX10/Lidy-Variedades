package com.loja.LidyVariedades.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProdutoCatalogoResponseTest {

    @Test
    void includesAvailableStockInCatalogResponse() {
        Produto produto = new Produto();
        produto.setId(7L);
        produto.setNome("Perfume");
        produto.setQuantidadeEstoque(4);

        ProdutoCatalogoResponse response = ProdutoCatalogoResponse.from(produto);

        assertEquals(4, response.quantidadeEstoque());
    }
}
