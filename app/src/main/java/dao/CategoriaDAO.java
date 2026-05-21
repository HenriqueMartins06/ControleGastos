package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.controlegastos.bd.DBHelper;
import com.example.controlegastos.modelos.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private SQLiteDatabase db;
    private DBHelper helper;

    public CategoriaDAO(Context context){
        helper = new DBHelper(context);
    }

    public void Abrir(){
        db = helper.getWritableDatabase();
    }

    public void Fechar(){
        helper.close();
    }

    public Long Inserir(Categoria c){
        ContentValues dados = new ContentValues();
        dados.put("Nome", c.getNome());

        this.Abrir();
        Long id = db.insert("categoria", null, dados);
        this.Fechar();

        return id;
    }

    public void Excluir(Categoria c){

        if (c.getId() == null || c.getId().isEmpty()){
            return;
        }

        this.Abrir();
        db.delete("categoria", "Id=?", new String[]{c.getId()});
        this.Fechar();
    }

    public void Atualizar(Categoria c){

        if (c.getId() == null || c.getId().isEmpty()){
            return;
        }

        ContentValues dados = new ContentValues();
        dados.put("Nome", c.getNome());

        this.Abrir();
        db.update("categoria", dados, "Id=?", new String[]{c.getId()});
        this.Fechar();
    }

    public List<Categoria> ListarTudo(){

        List<Categoria> lista = new ArrayList<>();

        this.Abrir();

        String campos[] = new String[]{"Id", "Nome"};

        Cursor dados = db.query(
                "categoria",
                campos,
                null,
                null,
                null,
                null,
                "Nome"
        );

        if (dados.moveToFirst()){

            while (!dados.isAfterLast()){

                Categoria c = new Categoria(
                        dados.getString(0),
                        dados.getString(1)
                );

                lista.add(c);

                dados.moveToNext();
            }
        }

        dados.close();

        this.Fechar();

        return lista;
    }
}