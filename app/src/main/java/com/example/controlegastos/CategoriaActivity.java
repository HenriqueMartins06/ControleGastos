package com.example.controlegastos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.Categoria;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CategoriaActivity extends AppCompatActivity {

    EditText edtNome;
    Button btnSalvar, btnAtualizar, btnExcluir, btnVoltar;
    LinearLayout layoutCategorias;

    FirebaseFirestore db;
    String idSelecionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        db = FirebaseFirestore.getInstance();

        edtNome = findViewById(R.id.edtNomeCategoria);
        btnSalvar = findViewById(R.id.btnSalvarCategoria);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnVoltar = findViewById(R.id.btnVoltar);
        layoutCategorias = findViewById(R.id.layoutCategorias);

        atualizarLista();

        btnSalvar.setOnClickListener(v -> salvarCategoria());

        btnAtualizar.setOnClickListener(v -> atualizarCategoria());

        btnExcluir.setOnClickListener(v -> excluirCategoria());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void salvarCategoria(){
        String nome = edtNome.getText().toString().trim();

        if(nome.isEmpty()){
            edtNome.setError("Digite uma categoria!");
            return;
        }

        Categoria categoria = new Categoria(nome);

        db.collection("categorias")
                .add(categoria)
                .addOnSuccessListener(documentReference -> {
                    edtNome.setText("");
                    Toast.makeText(this, "Categoria salva!", Toast.LENGTH_SHORT).show();
                    atualizarLista();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void atualizarCategoria(){
        String nome = edtNome.getText().toString().trim();

        if(idSelecionado.isEmpty()){
            Toast.makeText(this, "Selecione uma categoria!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(nome.isEmpty()){
            edtNome.setError("Digite uma categoria!");
            return;
        }

        db.collection("categorias")
                .document(idSelecionado)
                .update("nome", nome)
                .addOnSuccessListener(aVoid -> {
                    limparCampos();
                    Toast.makeText(this, "Categoria atualizada!", Toast.LENGTH_SHORT).show();
                    atualizarLista();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao atualizar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void excluirCategoria(){
        if(idSelecionado.isEmpty()){
            Toast.makeText(this, "Selecione uma categoria!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("categorias")
                .document(idSelecionado)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    limparCampos();
                    Toast.makeText(this, "Categoria excluída!", Toast.LENGTH_SHORT).show();
                    atualizarLista();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao excluir: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void atualizarLista(){
        layoutCategorias.removeAllViews();

        db.collection("categorias")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        Categoria c = doc.toObject(Categoria.class);
                        c.setId(doc.getId());

                        TextView txt = new TextView(this);
                        txt.setText(c.getNome());
                        txt.setTextSize(16);
                        txt.setPadding(10, 10, 10, 10);

                        txt.setOnClickListener(v -> {
                            idSelecionado = c.getId();
                            edtNome.setText(c.getNome());
                            Toast.makeText(this, "Categoria selecionada!", Toast.LENGTH_SHORT).show();
                        });

                        layoutCategorias.addView(txt);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao listar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void limparCampos(){
        edtNome.setText("");
        idSelecionado = "";
    }
}