package com.example.controlegastos.modelos;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String Id;
    private String Nome;

    public Usuario(){

    }

    public Usuario(String id, String nome) {
        Id = id;
        Nome = nome;
    }

    public Usuario(String nome) {
        Nome = nome;
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