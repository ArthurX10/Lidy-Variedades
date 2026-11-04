package com.loja.LidyVariedades.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "item_venda")
public class ItemVenda{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;


    public ItemVenda() {}

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public Venda getVenda(){return venda;}
    public void setVenda(Venda venda){this.venda = venda;}

    public Produto getProduto(){return produto;}
    public void setProduto(Produto produto){this.produto = produto;}

    public Integer getQuantidade(){return quantidade;}
    public void setQuantidade(Integer quantidade){this.quantidade = quantidade;}

    public BigDecimal getPrecoUnitario(){return precoUnitario;}
    public void setPrecoUnitario(BigDecimal precoUnitario){this.precoUnitario = precoUnitario;}

    public BigDecimal getPrecoTotal(){return precoTotal;}
    public void setPrecoTotal(BigDecimal precoTotal){this.precoTotal = precoTotal;}
}