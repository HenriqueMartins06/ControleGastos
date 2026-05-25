package com.example.controlegastos;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.Gasto;
import com.example.controlegastos.modelos.Receita;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    LinearLayout layoutGastos;
    Button btnUsuario, btnCategoria, btnFormaPagamento, btnReceita, btnGasto;

    FirebaseFirestore db;
    int totalMovimentacoes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        layoutGastos = findViewById(R.id.layoutGastos);
        btnUsuario = findViewById(R.id.btnUsuario);
        btnCategoria = findViewById(R.id.btnCategoria);
        btnFormaPagamento = findViewById(R.id.btnFormaPagamento);
        btnReceita = findViewById(R.id.btnReceita);
        btnGasto = findViewById(R.id.btnGasto);

        btnUsuario.setOnClickListener(v ->
                startActivity(new Intent(this, UsuarioActivity.class)));

        btnCategoria.setOnClickListener(v ->
                startActivity(new Intent(this, CategoriaActivity.class)));

        btnFormaPagamento.setOnClickListener(v ->
                startActivity(new Intent(this, FormaPagamentoActivity.class)));

        btnReceita.setOnClickListener(v ->
                startActivity(new Intent(this, ReceitaActivity.class)));

        btnGasto.setOnClickListener(v ->
                startActivity(new Intent(this, GastoActivity.class)));

        atualizarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();
    }

    private void atualizarLista() {
        layoutGastos.removeAllViews();
        totalMovimentacoes = 0;

        carregarGastos();
        carregarReceitas();
    }

    private void carregarGastos() {
        db.collection("gastos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        totalMovimentacoes++;

                        Gasto g = doc.toObject(Gasto.class);
                        g.setId(doc.getId());

                        String usuario = g.getUsuarioNome() != null ? g.getUsuarioNome() : "Não informado";
                        String categoria = g.getCategoriaNome() != null ? g.getCategoriaNome() : "Não informado";
                        String forma = g.getFormaPagamentoNome() != null ? g.getFormaPagamentoNome() : "Não informado";

                        String texto =
                                "Descrição: " + g.getDescricao() +
                                        "\nValor: R$ " + g.getValor() +
                                        "\nUsuário: " + usuario +
                                        "\nCategoria: " + categoria +
                                        "\nPagamento: " + forma;

                        criarCard(texto, true, g.getId());
                    }

                    verificarListaVazia();
                });
    }

    private void carregarReceitas() {
        db.collection("receitas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        totalMovimentacoes++;

                        Receita r = doc.toObject(Receita.class);
                        r.setId(doc.getId());

                        String usuario = r.getUsuarioNome() != null ? r.getUsuarioNome() : "Não informado";
                        String forma = r.getFormaPagamentoNome() != null ? r.getFormaPagamentoNome() : "Não informado";

                        String texto =
                                "Descrição: " + r.getDescricao() +
                                        "\nOrigem: " + r.getOrigem() +
                                        "\nValor: R$ " + r.getValor() +
                                        "\nUsuário: " + usuario +
                                        "\nPagamento: " + forma;

                        criarCard(texto, false, r.getId());
                    }

                    verificarListaVazia();
                });
    }

    private void criarCard(String texto, boolean gasto, String id) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(30, 25, 30, 25);

        GradientDrawable fundo = new GradientDrawable();
        fundo.setCornerRadius(28);

        if (gasto) {
            fundo.setColor(Color.parseColor("#2A1515"));
            fundo.setStroke(3, Color.parseColor("#EF5350"));
        } else {
            fundo.setColor(Color.parseColor("#132A18"));
            fundo.setStroke(3, Color.parseColor("#66BB6A"));
        }

        card.setBackground(fundo);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);

        TextView titulo = new TextView(this);
        titulo.setText(gasto ? "🔴 GASTO" : "🟢 RECEITA");
        titulo.setTextSize(16);
        titulo.setTypeface(null, Typeface.BOLD);
        titulo.setTextColor(Color.WHITE);

        TextView txt = new TextView(this);
        txt.setText(texto);
        txt.setTextSize(14);
        txt.setTextColor(Color.parseColor("#E0E0E0"));
        txt.setPadding(0, 12, 0, 20);

        Button btnExcluir = new Button(this);
        btnExcluir.setText("Excluir");
        btnExcluir.setTextSize(11);
        btnExcluir.setBackgroundResource(R.drawable.botao_excluir);
        btnExcluir.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                280,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        btnParams.gravity = Gravity.CENTER_HORIZONTAL;
        btnExcluir.setLayoutParams(btnParams);

        btnExcluir.setOnClickListener(v -> {

            String colecao = gasto ? "gastos" : "receitas";

            db.collection(colecao)
                    .document(id)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Excluído!", Toast.LENGTH_SHORT).show();
                        atualizarLista();
                    });
        });

        card.addView(titulo);
        card.addView(txt);
        card.addView(btnExcluir);

        layoutGastos.addView(card);
    }

    private void verificarListaVazia() {
        if (totalMovimentacoes == 0) {

            TextView vazio = new TextView(this);
            vazio.setText("Nenhuma movimentação cadastrada.");
            vazio.setTextSize(16);
            vazio.setTextColor(Color.WHITE);
            vazio.setGravity(Gravity.CENTER);

            layoutGastos.addView(vazio);
        }
    }
}