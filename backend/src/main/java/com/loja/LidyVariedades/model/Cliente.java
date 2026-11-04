package com.loja.LidyVariedades.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;


    private String telefone;
    private String endereco;
    
    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Cliente() {
    }

    public Cliente(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public String getNome(){return nome;}
    public void setNome(String nome){this.nome = nome;}

    public String getTelefone(){return telefone;}
    public void setTelefone(String telefone){this.telefone = telefone;}

    public String getEndereco(){return endereco;}
    public void setEndereco(String endereco){this.endereco = endereco;}


}
