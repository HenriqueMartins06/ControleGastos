package com.example.controlegastos.modelos;

import java.io.Serializable;

public class Gasto implements Serializable {

    private int Id;
    private double Valor;
    private String Descricao;
    private int UsuarioId;
    private int CategoriaId;

    public Gasto(){

    }

    public Gasto(int id, double valor, String descricao, int usuarioId, int categoriaId) {
        Id = id;
        Valor = valor;
        Descricao = descricao;
        UsuarioId = usuarioId;
        CategoriaId = categoriaId;
    }

    public Gasto(double valor, String descricao, int usuarioId, int categoriaId) {
        Valor = valor;
        Descricao = descricao;
        UsuarioId = usuarioId;
        CategoriaId = categoriaId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double valor) {
        Valor = valor;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public int getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        UsuarioId = usuarioId;
    }

    public int getCategoriaId() {
        return CategoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        CategoriaId = categoriaId;
    }
}