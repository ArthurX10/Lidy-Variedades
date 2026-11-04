package org.example.lidyvariedadesdesktop;

public class ItemVendaRequest {
    private Long produtoId;
    private Integer quantidade;

    public ItemVendaRequest() {}

    public Long getProdutoId(){return produtoId;}
    public void setProdutoId(Long produtoId){this.produtoId = produtoId;}

    public Integer getQuantidade(){return quantidade;}
    public void setQuantidade(Integer quantidade){this.quantidade = quantidade;}
}
