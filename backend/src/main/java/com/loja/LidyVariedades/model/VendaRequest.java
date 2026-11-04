package com.loja.LidyVariedades.model;

import java.util.List;

public class VendaRequest {
    private Long clienteId;
    private String formaPagamento;
    private List<ItemVendaRequest> itens;

    public Long getClienteId(){return clienteId;}
    public void setClienteId(Long clienteId){this.clienteId = clienteId;}
    public String getFormaPagamento(){return formaPagamento;}
    public void setFormaPagamento(String formaPagamento){this.formaPagamento = formaPagamento;}
    public List<ItemVendaRequest> getItens(){return itens;}
    public void setItens(List<ItemVendaRequest> itens){this.itens = itens;}

}
