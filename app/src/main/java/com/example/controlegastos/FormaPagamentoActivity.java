package com.example.controlegastos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.FormaPagamento;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FormaPagamentoActivity extends AppCompatActivity {

    EditText edtNome;
    Button btnSalvar, btnAtualizar, btnExcluir, btnVoltar;
    TextView txtLista;

    FirebaseFirestore db;
    String formaSelecionadaId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma_pagamento);

        db = FirebaseFirestore.getInstance();

        edtNome = findViewById(R.id.edtNomeForma);
        btnSalvar = findViewById(R.id.btnSalvarForma);
        btnAtualizar = findViewById(R.id.btnAtualizarForma);
        btnExcluir = findViewById(R.id.btnExcluirForma);
        btnVoltar = findViewById(R.id.btnVoltarForma);
        txtLista = findViewById(R.id.txtFormas);

        atualizar();

        btnSalvar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString();

            if (nome.isEmpty()) {
                edtNome.setError("Digite uma forma de pagamento!");
                return;
            }

            FormaPagamento forma = new FormaPagamento(nome);

            db.collection("formas_pagamento")
                    .add(forma)
                    .addOnSuccessListener(documentReference -> {
                        edtNome.setText("");
                        Toast.makeText(this, "Salvo!", Toast.LENGTH_SHORT).show();
                        atualizar();
                    });
        });

        btnAtualizar.setOnClickListener(v -> {
            if (formaSelecionadaId == null) {
                Toast.makeText(this, "Selecione uma forma primeiro!", Toast.LENGTH_SHORT).show();
                return;
            }

            String nome = edtNome.getText().toString();

            if (nome.isEmpty()) {
                edtNome.setError("Digite um nome!");
                return;
            }

            db.collection("formas_pagamento")
                    .document(formaSelecionadaId)
                    .update("nome", nome);
        });

        btnExcluir.setOnClickListener(v -> {
            if (formaSelecionadaId == null) {
                Toast.makeText(this, "Selecione uma forma primeiro!", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("formas_pagamento")
                    .document(formaSelecionadaId)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        edtNome.setText("");
                        formaSelecionadaId = null;
                        atualizar();
                    });
        });

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void atualizar() {

        db.collection("formas_pagamento")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    StringBuilder texto = new StringBuilder("Formas cadastradas:\n\n");

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FormaPagamento f = doc.toObject(FormaPagamento.class);
                        f.setId(doc.getId());

                        texto.append(f.getNome()).append("\n");
                    }

                    txtLista.setText(texto.toString());
                });
    }
}