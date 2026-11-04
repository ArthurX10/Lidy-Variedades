package org.example.lidyvariedadesdesktop;

import java.math.BigDecimal;

public class ItemCarrinho {
    private Produto produto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;


    public ItemCarrinho() {}

    public ItemCarrinho(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = (produto != null && produto.getPrecoVigente() != null)
                ? produto.getPrecoVigente()
                : BigDecimal.ZERO;
        this.subtotal = this.precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }


    public Produto getProduto() {return produto; }
    public void setProduto(Produto produto) {this.produto = produto; }
    public Integer getQuantidade() {return quantidade; }
    public void setQuantidade(Integer quantidade) {this.quantidade = quantidade; }
    public BigDecimal getPrecoUnitario() {return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) {this.precoUnitario = precoUnitario; }
    public BigDecimal getSubtotal() {return subtotal; }
    public void setSubtotal(BigDecimal subtotal) {this.subtotal = subtotal; }

    public String getProdutoNome() {
        return produto != null ? produto.getNome() : "";
    }


}
