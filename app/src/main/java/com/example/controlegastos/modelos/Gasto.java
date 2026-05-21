package com.example.controlegastos.modelos;

import java.io.Serializable;

public class Gasto implements Serializable {

    private String Id;
    private double Valor;
    private String Descricao;

    private String UsuarioId;
    private String CategoriaId;

    public Gasto(){

    }

    public Gasto(String id,
                 double valor,
                 String descricao,
                 String usuarioId,
                 String categoriaId) {

        Id = id;
        Valor = valor;
        Descricao = descricao;
        UsuarioId = usuarioId;
        CategoriaId = categoriaId;
    }

    public Gasto(double valor,
                 String descricao,
                 String usuarioId,
                 String categoriaId) {

        Valor = valor;
        Descricao = descricao;
        UsuarioId = usuarioId;
        CategoriaId = categoriaId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
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

    public String getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        UsuarioId = usuarioId;
    }

    public String getCategoriaId() {
        return CategoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        CategoriaId = categoriaId;
    }
}