package com.example.controlegastos;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.FormaPagamento;
import com.example.controlegastos.modelos.Receita;
import com.example.controlegastos.modelos.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReceitaActivity extends AppCompatActivity {

    Spinner spUsuario, spFormaPagamento;
    EditText edtValor, edtDescricao, edtOrigem;
    Button btnSalvar, btnVoltar;

    FirebaseFirestore db;

    List<Usuario> listaUsuarios = new ArrayList<>();
    List<FormaPagamento> listaFormas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        db = FirebaseFirestore.getInstance();

        spUsuario = findViewById(R.id.spUsuarioReceita);
        spFormaPagamento = findViewById(R.id.spFormaReceita);

        edtValor = findViewById(R.id.edtValorReceita);
        edtDescricao = findViewById(R.id.edtDescricaoReceita);
        edtOrigem = findViewById(R.id.edtOrigemReceita);

        btnSalvar = findViewById(R.id.btnSalvarReceita);
        btnVoltar = findViewById(R.id.btnVoltarReceita);

        carregarUsuarios();
        carregarFormsPagamento(); // Correção sutil no nome da chamada para bater com o método abaixo

        btnSalvar.setOnClickListener(v -> salvarReceita());

        btnVoltar.setOnClickListener(v -> finish());
    }

    // Puxa os usuários cadastrados e joga no Spinner
    private void carregarUsuarios() {
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    listaUsuarios.clear();

                    // Usa função lá de baixo para criar o adaptador com letra clara
                    ArrayAdapter<String> adapter = criarAdapterBranco();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Usuario u = doc.toObject(Usuario.class);
                        u.setId(doc.getId());

                        listaUsuarios.add(u);
                        adapter.add(u.getNome());
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spUsuario.setAdapter(adapter);
                });
    }

    // Puxa as formas de pagamento e joga no Spinner
    private void carregarFormsPagamento() {
        db.collection("formas_pagamento")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    listaFormas.clear();

                    // Usa função lá de baixo para criar o adaptador com letra clara
                    ArrayAdapter<String> adapter = criarAdapterBranco();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FormaPagamento f = doc.toObject(FormaPagamento.class);
                        f.setId(doc.getId());

                        listaFormas.add(f);
                        adapter.add(f.getNome());
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spFormaPagamento.setAdapter(adapter);
                });
    }

    // Valida os campos de entrada e salva os dados na coleção
    private void salvarReceita() {

        String valorTexto = edtValor.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();
        String origem = edtOrigem.getText().toString().trim();

        if (valorTexto.isEmpty()) {
            edtValor.setError("Digite o valor!");
            return;
        }

        if (descricao.isEmpty()) {
            edtDescricao.setError("Digite a descrição!");
            return;
        }

        if (origem.isEmpty()) {
            edtOrigem.setError("Digite a origem!");
            return;
        }

        double valor = Double.parseDouble(valorTexto);

        Usuario usuario = listaUsuarios.get(spUsuario.getSelectedItemPosition());
        FormaPagamento forma = listaFormas.get(spFormaPagamento.getSelectedItemPosition());

        Receita receita = new Receita(
                valor,
                descricao,
                origem,
                usuario.getId(),
                usuario.getNome(),
                forma.getId(),
                forma.getNome()
        );

        db.collection("receitas")
                .add(receita)
                .addOnSuccessListener(documentReference -> {
                    edtValor.setText("");
                    edtDescricao.setText("");
                    edtOrigem.setText("");

                    Toast.makeText(this, "Receita salva!", Toast.LENGTH_SHORT).show();
                });
    }

    // Cria o ArrayAdapter para forçar a cor branca
    private ArrayAdapter<String> criarAdapterBranco() {
        return new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item) {
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.view.View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(android.graphics.Color.WHITE); // Força a cor do texto para branco
                return v;
            }
        };
    }
}