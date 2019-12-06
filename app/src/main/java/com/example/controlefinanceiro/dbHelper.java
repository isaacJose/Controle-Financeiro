package com.example.controlefinanceiro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    private static final String name = "banco";
    private static final int version = 5;

    public dbHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table receita(id integer primary key autoincrement," +
                "descricao texto not null," +
                "valor double not null," +
                "categoria texto not null," +
                "tipo texto not null)");

        db.execSQL("create table despesa(id integer primary key autoincrement," +
                "descricao texto not null," +
                "valor double not null," +
                "categoria texto not null," +
                "tipo texto not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS receita");
        db.execSQL("DROP TABLE IF EXISTS despesa");
        onCreate(db);
    }
}
