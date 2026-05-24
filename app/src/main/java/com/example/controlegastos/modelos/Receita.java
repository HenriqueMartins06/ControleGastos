package com.example.controlegastos.modelos;

import java.io.Serializable;

public class Receita implements Serializable {

    private String Id;
    private double Valor;
    private String Descricao;
    private String Origem;

    private String UsuarioId;
    private String UsuarioNome;

    private String FormaPagamentoId;
    private String FormaPagamentoNome;

    public Receita() {

    }

    public Receita(double valor,
                   String descricao,
                   String origem,
                   String usuarioId,
                   String usuarioNome,
                   String formaPagamentoId,
                   String formaPagamentoNome) {

        Valor = valor;
        Descricao = descricao;
        Origem = origem;
        UsuarioId = usuarioId;
        UsuarioNome = usuarioNome;
        FormaPagamentoId = formaPagamentoId;
        FormaPagamentoNome = formaPagamentoNome;
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

    public String getOrigem() {
        return Origem;
    }

    public void setOrigem(String origem) {
        Origem = origem;
    }

    public String getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        UsuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return UsuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        UsuarioNome = usuarioNome;
    }

    public String getFormaPagamentoId() {
        return FormaPagamentoId;
    }

    public void setFormaPagamentoId(String formaPagamentoId) {
        FormaPagamentoId = formaPagamentoId;
    }

    public String getFormaPagamentoNome() {
        return FormaPagamentoNome;
    }

    public void setFormaPagamentoNome(String formaPagamentoNome) {
        FormaPagamentoNome = formaPagamentoNome;
    }
}