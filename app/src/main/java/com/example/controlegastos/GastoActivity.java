package com.example.controlegastos;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.Categoria;
import com.example.controlegastos.modelos.FormaPagamento;
import com.example.controlegastos.modelos.Gasto;
import com.example.controlegastos.modelos.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GastoActivity extends AppCompatActivity {

    Spinner spUsuario, spCategoria, spFormaPagamento;
    EditText edtValor, edtDescricao;
    Button btnSalvar, btnVoltar;

    FirebaseFirestore db;

    List<Usuario> listaUsuarios = new ArrayList<>();
    List<Categoria> listaCategorias = new ArrayList<>();
    List<FormaPagamento> listaFormas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        db = FirebaseFirestore.getInstance();

        spUsuario = findViewById(R.id.spUsuario);
        spCategoria = findViewById(R.id.spCategoria);
        spFormaPagamento = findViewById(R.id.spFormaPagamento);

        edtValor = findViewById(R.id.edtValor);
        edtDescricao = findViewById(R.id.edtDescricao);

        btnSalvar = findViewById(R.id.btnSalvarGasto);
        btnVoltar = findViewById(R.id.btnVoltar);

        carregarUsuarios();
        carregarCategorias();
        carregarFormasPagamento();

        btnSalvar.setOnClickListener(v -> salvarGasto());

        btnVoltar.setOnClickListener(v -> finish());
    }

    // Puxa os usuários do banco e joga no Spinner com o texto branco
    private void carregarUsuarios() {
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaUsuarios.clear();

                    // criar o adaptador com letra clara
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

    // Puxa as categorias do banco e joga no Spinner com o texto branco
    private void carregarCategorias() {
        db.collection("categorias")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaCategorias.clear();

                    // criar o adaptador com letra clara
                    ArrayAdapter<String> adapter = criarAdapterBranco();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Categoria c = doc.toObject(Categoria.class);
                        c.setId(doc.getId());

                        listaCategorias.add(c);
                        adapter.add(c.getNome());
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategoria.setAdapter(adapter);
                });
    }

    // Puxa as formas de pagamento do banco e joga no Spinner com o texto branco
    private void carregarFormasPagamento() {
        db.collection("formas_pagamento")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaFormas.clear();

                    //  criar o adaptador com letra clara
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

    // Pega as informações digitadas e salva a despesa na coleção
    private void salvarGasto() {
        String valorTexto = edtValor.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();

        if (valorTexto.isEmpty()) {
            edtValor.setError("Digite o valor!");
            return;
        }

        if (descricao.isEmpty()) {
            edtDescricao.setError("Digite a descrição!");
            return;
        }

        double valor = Double.parseDouble(valorTexto);

        Usuario usuario = listaUsuarios.get(spUsuario.getSelectedItemPosition());
        Categoria categoria = listaCategorias.get(spCategoria.getSelectedItemPosition());
        FormaPagamento forma = listaFormas.get(spFormaPagamento.getSelectedItemPosition());

        Gasto gasto = new Gasto(
                valor,
                descricao,
                usuario.getId(),
                usuario.getNome(),
                categoria.getId(),
                categoria.getNome(),
                forma.getId(),
                forma.getNome()
        );

        db.collection("gastos")
                .add(gasto)
                .addOnSuccessListener(documentReference -> {
                    edtValor.setText("");
                    edtDescricao.setText("");

                    Toast.makeText(this, "Gasto salvo!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // Cria um ArrayAdapter pra pintar a letra de branco
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