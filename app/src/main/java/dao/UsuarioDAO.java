package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.controlegastos.bd.DBHelper;
import com.example.controlegastos.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private SQLiteDatabase db;
    private DBHelper helper;

    public UsuarioDAO(Context context){
        helper = new DBHelper(context);
    }

    public void Abrir(){
        db = helper.getWritableDatabase();
    }

    public void Fechar(){
        helper.close();
    }

    // funcao pra ver se tem gasto registrado
    public boolean temGasto(int idUsuario) {
        this.Abrir();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM gasto WHERE UsuarioId = " + idUsuario,
                null
        );

        int total = cursor.getCount();
        cursor.close();
        this.Fechar();

        return total > 0;
    }

    public Long Inserir(Usuario u){
        ContentValues dados = new ContentValues();
        dados.put("Nome", u.getNome());

        this.Abrir();
        Long id = db.insert("usuario", null, dados);
        this.Fechar();

        return id;
    }

    public void Excluir(Usuario u){
        if (u.getId() == null || u.getId().isEmpty()){
            return;
        }

        this.Abrir();
        db.delete("usuario", "Id=?", new String[]{u.getId()});
        this.Fechar();
    }

    public void Atualizar(Usuario u){
        if (u.getId() == null || u.getId().isEmpty()){
            return;
        }

        ContentValues dados = new ContentValues();
        dados.put("Nome", u.getNome());

        this.Abrir();
        db.update("usuario", dados, "Id=?", new String[]{u.getId()});
        this.Fechar();
    }

    public List<Usuario> ListarTudo(){
        List<Usuario> lista = new ArrayList<>();
        this.Abrir();

        String campos[] = new String[]{"Id", "Nome"};
        Cursor dados = db.query("usuario", campos, null, null, null, null, "Nome");

        if (dados.moveToFirst()) {
            while (!dados.isAfterLast()) {

                Usuario u = new Usuario(
                        dados.getString(0),
                        dados.getString(1)
                );

                lista.add(u);
                dados.moveToNext();
            }
        }

        dados.close();
        this.Fechar();

        return lista;
    }
}