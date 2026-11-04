package com.loja.LidyVariedades.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "vendas")
public class Venda{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itensVenda = new ArrayList<>();

    private BigDecimal totalVenda = BigDecimal.ZERO;
    private String formaPagamento;
    private LocalDateTime dataVenda = LocalDateTime.now();


    public Venda(){}

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public Cliente getCliente(){return cliente;}
    public void setCliente(Cliente cliente){this.cliente = cliente;}

    public List<ItemVenda> getItensVenda(){return itensVenda;}
    public void setItensVenda(List<ItemVenda> itensVenda){this.itensVenda = itensVenda;}

    public BigDecimal getTotalVenda(){return totalVenda;}
    public void setTotalVenda(BigDecimal TotalVenda){this.totalVenda = TotalVenda;}

    public String getFormaPagamento(){return formaPagamento;}
    public void setFormaPagamento(String formaPagamento){this.formaPagamento = formaPagamento;}

    public LocalDateTime getDataVenda(){return dataVenda;}
    public void setDataVenda(LocalDateTime dataVenda){this.dataVenda = dataVenda;}



}