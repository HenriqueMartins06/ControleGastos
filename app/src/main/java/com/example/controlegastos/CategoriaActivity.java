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
    Button btnSalvar, btnVoltar;
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
        btnVoltar = findViewById(R.id.btnVoltar);
        layoutCategorias = findViewById(R.id.layoutCategorias);

        atualizarLista();

        btnSalvar.setOnClickListener(v -> salvarOuAtualizar());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void salvarOuAtualizar() {
        String nome = edtNome.getText().toString().trim();

        if (nome.isEmpty()) {
            edtNome.setError("Digite uma categoria!");
            return;
        }

        if (idSelecionado.isEmpty()) {
            Categoria categoria = new Categoria(nome);

            db.collection("categorias")
                    .add(categoria)
                    .addOnSuccessListener(documentReference -> {
                        limparCampos();
                        Toast.makeText(this, "Categoria salva!", Toast.LENGTH_SHORT).show();
                        atualizarLista();
                    });

        } else {
            db.collection("categorias")
                    .document(idSelecionado)
                    .update("nome", nome)
                    .addOnSuccessListener(unused -> {
                        limparCampos();
                        Toast.makeText(this, "Categoria atualizada!", Toast.LENGTH_SHORT).show();
                        atualizarLista();
                    });
        }
    }

    private void excluirCategoriaComVerificacao(Categoria c) {
        db.collection("gastos")
                .whereEqualTo("categoriaId", c.getId())
                .get()
                .addOnSuccessListener(gastos -> {

                    if (!gastos.isEmpty()) {
                        Toast.makeText(this, "Não é possível excluir. Categoria possui gasto vinculado!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    db.collection("categorias")
                            .document(c.getId())
                            .delete()
                            .addOnSuccessListener(unused -> {
                                limparCampos();
                                Toast.makeText(this, "Categoria excluída!", Toast.LENGTH_SHORT).show();
                                atualizarLista();
                            });
                });
    }

    private void atualizarLista() {
        layoutCategorias.removeAllViews();

        db.collection("categorias")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Categoria c = doc.toObject(Categoria.class);
                        c.setId(doc.getId());

                        LinearLayout linha = new LinearLayout(this);
                        linha.setOrientation(LinearLayout.HORIZONTAL);
                        linha.setBackgroundResource(R.drawable.card_gasto);
                        linha.setPadding(20, 20, 20, 20);

                        LinearLayout.LayoutParams linhaParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                        linhaParams.setMargins(0, 0, 0, 16);
                        linha.setLayoutParams(linhaParams);

                        TextView txt = new TextView(this);
                        txt.setText(c.getNome());
                        txt.setTextSize(16);
                        txt.setTextColor(android.graphics.Color.BLACK);

                        LinearLayout.LayoutParams txtParams =
                                new LinearLayout.LayoutParams(
                                        0,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        1
                                );

                        txt.setLayoutParams(txtParams);

                        Button btnEditar = new Button(this);
                        btnEditar.setText("EDITAR");
                        btnEditar.setTextSize(11);
                        btnEditar.setPadding(20, 10, 20, 10);
                        btnEditar.setBackgroundResource(R.drawable.botao);
                        btnEditar.setTextColor(android.graphics.Color.WHITE);

                        LinearLayout.LayoutParams editarParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                        editarParams.setMargins(8, 0, 8, 0);
                        btnEditar.setLayoutParams(editarParams);

                        btnEditar.setOnClickListener(v -> {
                            idSelecionado = c.getId();
                            edtNome.setText(c.getNome());
                        });

                        Button btnExcluir = new Button(this);
                        btnExcluir.setText("EXCLUIR");
                        btnExcluir.setTextSize(11);
                        btnExcluir.setPadding(20, 10, 20, 10);
                        btnExcluir.setBackgroundResource(R.drawable.botao_excluir);
                        btnExcluir.setTextColor(android.graphics.Color.WHITE);

                        LinearLayout.LayoutParams excluirParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                        btnExcluir.setLayoutParams(excluirParams);

                        btnExcluir.setOnClickListener(v -> excluirCategoriaComVerificacao(c));

                        linha.addView(txt);
                        linha.addView(btnEditar);
                        linha.addView(btnExcluir);

                        layoutCategorias.addView(linha);
                    }
                });
    }

    private void limparCampos() {
        edtNome.setText("");
        idSelecionado = "";
    }
}