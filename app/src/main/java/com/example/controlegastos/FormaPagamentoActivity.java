package com.example.controlegastos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.FormaPagamento;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FormaPagamentoActivity extends AppCompatActivity {

    EditText edtNome;
    Button btnSalvar, btnVoltar;
    LinearLayout layoutFormas;

    FirebaseFirestore db;
    String idSelecionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma_pagamento);

        db = FirebaseFirestore.getInstance();

        edtNome = findViewById(R.id.edtNomeForma);
        btnSalvar = findViewById(R.id.btnSalvarForma);
        btnVoltar = findViewById(R.id.btnVoltarForma);
        layoutFormas = findViewById(R.id.layoutFormas);

        atualizarLista();

        btnSalvar.setOnClickListener(v -> salvarOuAtualizar());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void salvarOuAtualizar() {
        String nome = edtNome.getText().toString().trim();

        if (nome.isEmpty()) {
            edtNome.setError("Digite uma forma de pagamento!");
            return;
        }

        if (idSelecionado.isEmpty()) {
            FormaPagamento forma = new FormaPagamento(nome);

            db.collection("formas_pagamento")
                    .add(forma)
                    .addOnSuccessListener(documentReference -> {
                        limparCampos();
                        Toast.makeText(this, "Forma salva!", Toast.LENGTH_SHORT).show();
                        atualizarLista();
                    });

        } else {
            db.collection("formas_pagamento")
                    .document(idSelecionado)
                    .update("nome", nome)
                    .addOnSuccessListener(unused -> {
                        limparCampos();
                        Toast.makeText(this, "Forma atualizada!", Toast.LENGTH_SHORT).show();
                        atualizarLista();
                    });
        }
    }

    private void atualizarLista() {
        layoutFormas.removeAllViews();

        db.collection("formas_pagamento")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FormaPagamento f = doc.toObject(FormaPagamento.class);
                        f.setId(doc.getId());

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
                        txt.setText(f.getNome());
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
                            idSelecionado = f.getId();
                            edtNome.setText(f.getNome());
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

                        btnExcluir.setOnClickListener(v -> {
                            db.collection("formas_pagamento")
                                    .document(f.getId())
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        limparCampos();
                                        Toast.makeText(this, "Forma excluída!", Toast.LENGTH_SHORT).show();
                                        atualizarLista();
                                    });
                        });

                        linha.addView(txt);
                        linha.addView(btnEditar);
                        linha.addView(btnExcluir);

                        layoutFormas.addView(linha);
                    }
                });
    }

    private void limparCampos() {
        edtNome.setText("");
        idSelecionado = "";
    }
}