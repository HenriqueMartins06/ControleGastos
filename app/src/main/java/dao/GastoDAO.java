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

        Long id = db.insert("gasto", null, dados);
        return id;
    }

    public void Excluir(Gasto g){
        if (g.getId() <= 0){
            return;
        }

        this.Abrir();
        db.delete("gasto", "Id=?", new String[]{String.valueOf(g.getId())});
        this.Fechar();
    }

    public void Atualizar(Gasto g){
        if (g.getId() <= 0){
            return;
        }

        ContentValues dados = new ContentValues();
        dados.put("Valor", g.getValor());
        dados.put("Descricao", g.getDescricao());
        dados.put("UsuarioId", g.getUsuarioId());
        dados.put("CategoriaId", g.getCategoriaId());

        this.Abrir();
        db.update("gasto", dados, "Id=?", new String[]{String.valueOf(g.getId())});
        this.Fechar();
    }

    public List<Gasto> ListarTudo() {
        List<Gasto> lista = new ArrayList<>();
        this.Abrir();

        // junta o gasto, categoria e usuario, cada um é sua inicial
        String sql = "SELECT g.Id, g.Valor, g.Descricao, g.UsuarioId, g.CategoriaId, c.Nome, u.Nome " +
                "FROM gasto g " +
                "INNER JOIN categoria c ON g.CategoriaId = c.Id " +
                "INNER JOIN usuario u ON g.UsuarioId = u.Id " +
                "ORDER BY g.Id DESC";

        Cursor dados = db.rawQuery(sql, null);

        if (dados.moveToFirst()) {
            while (!dados.isAfterLast()) {
                // Pegamos o Nome do Usuário (índice 6) e o Nome da Categoria (índice 5)
                String nomeCategoria = dados.getString(5);
                String nomeUsuario = dados.getString(6);

                // dado completo ja
                String infoCompleta = "Cliente: " + nomeUsuario +
                        "\nDesc: " + dados.getString(2) +
                        "\nCat: " + nomeCategoria;

                Gasto g = new Gasto(
                        dados.getInt(0),
                        dados.getDouble(1),
                        infoCompleta, // com tudo ja
                        dados.getInt(3),
                        dados.getInt(4)
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