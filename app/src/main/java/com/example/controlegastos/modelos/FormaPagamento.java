package com.example.controlegastos.modelos;

import java.io.Serializable;

public class FormaPagamento implements Serializable {

    private String Id;
    private String Nome;

    public FormaPagamento() {
    }

    public FormaPagamento(String nome) {
        this.Nome = nome;
    }

    public FormaPagamento(String id, String nome) {
        this.Id = id;
        this.Nome = nome;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }
}
