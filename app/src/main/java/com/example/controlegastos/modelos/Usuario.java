package com.example.controlegastos.modelos;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int Id;
    private String Nome;

    public Usuario(){

    }

    public Usuario(int id, String nome) {
        Id = id;
        Nome = nome;
    }

    public Usuario(String nome) {
        Nome = nome;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }
}