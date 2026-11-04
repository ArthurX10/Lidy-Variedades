package org.example.lidyvariedadesdesktop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private Long id;
    private Cliente cliente;
    private List<ItemVenda> itensVenda = new ArrayList<>();
    private BigDecimal totalVenda = BigDecimal.ZERO;
    private String formaPagamento;
    private LocalDateTime dataVenda = LocalDateTime.now();

    public Venda() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<ItemVenda> getItensVenda() { return itensVenda; }
    public void setItensVenda(List<ItemVenda> itensVenda) { this.itensVenda = itensVenda; }

    public BigDecimal getTotalVenda() { return totalVenda; }
    public void setTotalVenda(BigDecimal totalVenda) { this.totalVenda = totalVenda; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    // Métodos auxiliares para a Tabela de Vendas
    public String getClienteNome() {
        return (cliente != null && cliente.getNome() != null) ? cliente.getNome() : "Balcão / Não identificado";
    }

    public String getDataFormatada() {
        if (dataVenda == null) return "-";
        return dataVenda.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public BigDecimal getLucro() {
        if (itensVenda == null || itensVenda.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal custoTotal = BigDecimal.ZERO;
        for (ItemVenda item : itensVenda) {
            if (item.getProduto() != null && item.getProduto().getPrecoCusto() != null && item.getQuantidade() != null) {
                custoTotal = custoTotal.add(item.getProduto().getPrecoCusto().multiply(BigDecimal.valueOf(item.getQuantidade())));
            }
        }
        if (totalVenda != null) {
            return totalVenda.subtract(custoTotal);
        }
        return BigDecimal.ZERO;
    }
}
