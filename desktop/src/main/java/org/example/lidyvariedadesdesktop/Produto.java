package org.example.lidyvariedadesdesktop;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Produto {
    private Long id;
    private String nome;
    private String descricao;
    private String imagemUrl;
    private Marca marca;
    private Categoria categoria;
    private String codigoReferencia;
    private BigDecimal precoCusto = BigDecimal.ZERO;
    private BigDecimal precoVenda = BigDecimal.ZERO;
    private BigDecimal precoPromocional;
    private Integer quantidadeEstoque = 0;
    private Integer estoqueMinimo = 0;
    private LocalDateTime criadoEm;

    public Produto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getCodigoReferencia() { return codigoReferencia; }
    public void setCodigoReferencia(String codigoReferencia) { this.codigoReferencia = codigoReferencia; }

    public BigDecimal getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }

    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

    public BigDecimal getPrecoPromocional() { return precoPromocional; }
    public void setPrecoPromocional(BigDecimal precoPromocional) { this.precoPromocional = precoPromocional; }

    public BigDecimal getPrecoVigente() {
        return precoPromocional != null && precoVenda != null
                && precoPromocional.compareTo(precoVenda) < 0
                ? precoPromocional
                : precoVenda;
    }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public Integer getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public String getMarcaNome() {
        return (marca != null && marca.getNome() != null) ? marca.getNome() : "-";
    }

    public String getCategoriaNome() {
        return (categoria != null && categoria.getNome() != null) ? categoria.getNome() : "-";
    }

    @Override
    public String toString() {
        if (nome == null) return "";
        BigDecimal precoVigente = getPrecoVigente();
        String preco = (precoVigente != null) ? String.format(" (R$ %.2f)", precoVigente) : "";
        return nome + preco;
    }
}
