package com.example.controlefinanceiro.receita;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.controlefinanceiro.dbHelper;

import java.util.ArrayList;
import java.util.List;

public class ReceitaDAO {

    private dbHelper conexao;
    private SQLiteDatabase db;

    private float totalReceita = 0;

    public ReceitaDAO(Context context) {
        conexao = new dbHelper(context);
        db = conexao.getWritableDatabase();
    }

    public long inserir(Receita receita){
        return db.insert("receita",null,receita.getContentValues());
    }

    public List<Receita> obterTodos(){
        List<Receita> receitas = new ArrayList<>();
        Cursor cursor = db.query("receita",new String[]{"id","descricao","valor","categoria","tipo"},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            Receita receita = new Receita();
            receita.setId(cursor.getInt(0));
            receita.setDescricao(cursor.getString(1));
            receita.setValor(cursor.getDouble(2));
            receita.setCategoria(cursor.getString(3));
            receita.setTipo(cursor.getString(4));

            receitas.add(receita);
        }

        return receitas;
    }

    public void atualizar(Receita receita){
        db.update("receita",receita.getContentValues(),"id = ?", new String[]{receita.getId().toString()});
    }

    public void excluir(Receita receita){
        db.delete("receita", "id = ?", new String[]{receita.getId().toString()});
    }

   public float totalReceita(){

        Cursor cursor = db.query("receita",new String[]{"valor"},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            totalReceita += cursor.getDouble(0);
        }
        return totalReceita;
    }

}
