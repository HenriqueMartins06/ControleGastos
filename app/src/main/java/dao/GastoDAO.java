package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.controlegastos.bd.DBHelper;
import com.example.controlegastos.modelos.Gasto;

import java.util.ArrayList;
import java.util.List;

public class GastoDAO {

    private SQLiteDatabase db;
    private DBHelper helper;

    public GastoDAO(Context context){
        helper = new DBHelper(context);
    }

    public void Abrir(){
        db = helper.getWritableDatabase();
    }

    public void Fechar(){
        helper.close();
    }

    public Long Inserir(Gasto g){

        ContentValues dados = new ContentValues();

        dados.put("Valor", g.getValor());
        dados.put("Descricao", g.getDescricao());
        dados.put("UsuarioId", g.getUsuarioId());
        dados.put("CategoriaId", g.getCategoriaId());

        this.Abrir();

        Long id = db.insert("gasto", null, dados);

        this.Fechar();

        return id;
    }

    public void Excluir(Gasto g){

        if (g.getId() == null || g.getId().isEmpty()){
            return;
        }

        this.Abrir();

        db.delete("gasto", "Id=?", new String[]{g.getId()});

        this.Fechar();
    }

    public void Atualizar(Gasto g){

        if (g.getId() == null || g.getId().isEmpty()){
            return;
        }

        ContentValues dados = new ContentValues();

        dados.put("Valor", g.getValor());
        dados.put("Descricao", g.getDescricao());
        dados.put("UsuarioId", g.getUsuarioId());
        dados.put("CategoriaId", g.getCategoriaId());

        this.Abrir();

        db.update("gasto", dados, "Id=?", new String[]{g.getId()});

        this.Fechar();
    }

    public List<Gasto> ListarTudo(){

        List<Gasto> lista = new ArrayList<>();

        this.Abrir();

        String campos[] = new String[]{
                "Id",
                "Valor",
                "Descricao",
                "UsuarioId",
                "CategoriaId"
        };

        Cursor dados = db.query(
                "gasto",
                campos,
                null,
                null,
                null,
                null,
                "Id DESC"
        );

        if (dados.moveToFirst()){

            while (!dados.isAfterLast()){

                Gasto g = new Gasto(
                        dados.getString(0),
                        dados.getDouble(1),
                        dados.getString(2),
                        dados.getString(3),
                        "",
                        dados.getString(4),
                        "",
                        "",
                        ""
                );

                lista.add(g);

                dados.moveToNext();
            }
        }

        dados.close();

        this.Fechar();

        return lista;
    }
}