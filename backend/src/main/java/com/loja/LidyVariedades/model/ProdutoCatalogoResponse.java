package com.loja.LidyVariedades.model;

import java.math.BigDecimal;

public record ProdutoCatalogoResponse(
    Long id,
    String nome,
    String codigoReferencia,
    Integer quantidadeEstoque,
    String descricao,
    String imagemUrl,
    MarcaResumo marca,
    CategoriaResumo categoria,
    BigDecimal precoVenda,
    BigDecimal precoPromocional
) {
    public static ProdutoCatalogoResponse from(Produto produto) {
        MarcaResumo marca = produto.getMarca() == null
            ? null
            : new MarcaResumo(produto.getMarca().getId(), produto.getMarca().getNome());
        CategoriaResumo categoria = produto.getCategoria() == null
            ? null
            : new CategoriaResumo(produto.getCategoria().getId(), produto.getCategoria().getNome());

        return new ProdutoCatalogoResponse(
            produto.getId(),
            produto.getNome(),
            produto.getCodigoReferencia(),
            produto.getQuantidadeEstoque(),
            produto.getDescricao(),
            produto.getImagemUrl(),
            marca,
            categoria,
            produto.getPrecoVenda(),
            produto.getPrecoPromocional()
        );
    }

    public record MarcaResumo(Long id, String nome) {}

    public record CategoriaResumo(Long id, String nome) {}
}
