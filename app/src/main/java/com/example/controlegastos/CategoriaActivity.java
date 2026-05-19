package com.example.controlegastos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast; // adicionei pra mostrar os avisos
import androidx.appcompat.app.AppCompatActivity;

import dao.CategoriaDAO;
import com.example.controlegastos.modelos.Categoria;

import java.util.List;

public class CategoriaActivity extends AppCompatActivity {

    EditText edtNome, edtIdExcluir;
    Button btnSalvar, btnVoltar, btnExcluir;
    TextView txtLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        // pra funcionar os btn
        edtNome = findViewById(R.id.edtNomeCategoria);
        edtIdExcluir = findViewById(R.id.edtIdExcluir);
        btnSalvar = findViewById(R.id.btnSalvarCategoria);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnVoltar = findViewById(R.id.btnVoltar);
        txtLista = findViewById(R.id.txtCategorias);

        atualizar();


        btnSalvar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString();

            if(nome.isEmpty()){
                edtNome.setError("Digite uma categoria!");
                return;
            }

            CategoriaDAO dao = new CategoriaDAO(this);
            dao.Abrir();
            dao.Inserir(new Categoria(nome));
            dao.Fechar();

            edtNome.setText("");
            atualizar();
        });

        // exxcluii com a trava de segurança
        btnExcluir.setOnClickListener(v -> {
            String idString = edtIdExcluir.getText().toString();

            if(idString.isEmpty()){
                edtIdExcluir.setError("Digite um ID!");
                return;
            }

            int id = Integer.parseInt(idString);
            CategoriaDAO dao = new CategoriaDAO(this);

            // verifica se a categoria tem  gasto
            if (dao.temGasto(id)) {
                Toast.makeText(this, "Não pode apagar! Tem gasto nessa categoria.", Toast.LENGTH_SHORT).show();
            } else {
                // se nao tiver nada preso, apaga
                dao.Abrir();
                Categoria c = new Categoria();
                c.setId(id);
                dao.Excluir(c);
                dao.Fechar();
                Toast.makeText(this, "Categoria excluída!", Toast.LENGTH_SHORT).show();
            }

            edtIdExcluir.setText("");
            atualizar();
        });

        //vlt
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void atualizar(){
        CategoriaDAO dao = new CategoriaDAO(this);
        dao.Abrir();
        List<Categoria> lista = dao.ListarTudo();
        dao.Fechar();

        StringBuilder texto = new StringBuilder("Categorias cadastradas:\n\n");

        for(Categoria c : lista){
            texto.append("(ID:").append(c.getId()).append(") ")
                    .append(c.getNome()).append("\n");
        }

        txtLista.setText(texto.toString());
    }
}