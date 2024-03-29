package com.example.controlefinanceiro.receita;

import android.content.ContentValues;

import java.io.Serializable;

public class Receita implements Serializable {

    private Integer id;
    private String descricao;
    private double valor;
    private String tipo;
    private String categoria;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put("descricao",getDescricao());
        values.put("valor",getValor());
        values.put("categoria",getCategoria());
        values.put("tipo",getTipo());


        return values;
    }
}
