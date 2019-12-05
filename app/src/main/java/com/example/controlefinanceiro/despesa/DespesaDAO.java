package com.example.controlefinanceiro.despesa;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.controlefinanceiro.dbHelper;
import com.example.controlefinanceiro.receita.Receita;

import java.util.ArrayList;
import java.util.List;

public class DespesaDAO {

    private dbHelper conexao;
    private SQLiteDatabase db;

    private float totalDespesa = 0;

    public DespesaDAO(Context context) {
        conexao = new dbHelper(context);
        db = conexao.getWritableDatabase();
    }

    public long inserir(Despesa despesa){
        return db.insert("despesa",null,despesa.getContentValues());
    }

    public List<Despesa> obterTodos() {
        List<Despesa> despesas = new ArrayList<>();
        Cursor cursor = db.query("despesa",new String[]{"id","descricao","valor","categoria","tipo"},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            Despesa despesa = new Despesa();
            despesa.setId(cursor.getInt(0));
            despesa.setDescricao(cursor.getString(1));
            despesa.setValor(cursor.getFloat(2));
            despesa.setCategoria(cursor.getString(3));
            despesa.setTipo(cursor.getString(4));

            despesas.add(despesa);
        }

        return despesas;
    }

    public void atualizar(Despesa despesa){
        db.update("despesa",despesa.getContentValues(),"id = ?", new String[]{despesa.getId().toString()});
    }

    public void excluir(Despesa despesa){
        db.delete("despesa", "id = ?", new String[]{despesa.getId().toString()});
    }

    public float totalDespesa(){

        Cursor cursor = db.query("despesa",new String[]{"valor"},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            totalDespesa += cursor.getFloat(0);
        }
        return totalDespesa;
    }

}
