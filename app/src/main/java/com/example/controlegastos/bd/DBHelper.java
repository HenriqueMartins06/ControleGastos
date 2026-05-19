package com.example.controlegastos.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DBVersion = 1;

    public DBHelper(Context context){
        super(context, "ControleGastos", null, DBVersion);
    }

    public void onCreate(SQLiteDatabase db){

        db.execSQL("create table usuario (Id integer primary key autoincrement, Nome text not null)");

        db.execSQL("create table categoria (Id integer primary key autoincrement, Nome text not null)");

        db.execSQL("create table gasto (Id integer primary key autoincrement, Valor real not null, Descricao text, UsuarioId integer, CategoriaId integer)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w("Atualizacao de BD", "V.Antiga: " + oldVersion + " - N.Versao: " + newVersion);

        db.execSQL("drop table if exists gasto");
        db.execSQL("drop table if exists usuario");
        db.execSQL("drop table if exists categoria");

        onCreate(db);
    }
}