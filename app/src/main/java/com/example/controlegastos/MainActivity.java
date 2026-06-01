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

    // Conexão com o banco de dado
    FirebaseFirestore db;

    // Contador geral para somar  gastos e receitas
    int totalMovimentacoes = 0;

    // Elas servem para  saber quando as duas buscas terminaram e impede erro.
    boolean gastosCarregados = false;
    boolean receitasCarregadas = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa  do banco de dados
        db = FirebaseFirestore.getInstance();

        // variáveis com os componentes do arquivo XML
        layoutGastos = findViewById(R.id.layoutGastos);
        btnUsuario = findViewById(R.id.btnUsuario);
        btnCategoria = findViewById(R.id.btnCategoria);
        btnFormaPagamento = findViewById(R.id.btnFormaPagamento);
        btnReceita = findViewById(R.id.btnReceita);
        btnGasto = findViewById(R.id.btnGasto);

        // Configuração dos cliques dos botões para abrir as outras telas
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

        // Primeira carga dos dados assim
        atualizarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega a lista automaticamente
        atualizarLista();
    }

    // Função que limpa a tela e reinicia o processo de busca
    private void atualizarLista() {
        layoutGastos.removeAllViews();
        totalMovimentacoes = 0;
        gastosCarregados = false;
        receitasCarregadas = false;

        // buscas nas duas coleções
        carregarGastos();
        carregarReceitas();
    }

    // Busca todos os documentos da coleção gastos
    private void carregarGastos() {
        db.collection("gastos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    // Percorre cada documento  que veio do banco
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        totalMovimentacoes++;

                        // Converte o documento do Firebase para o objeto
                        Gasto g = doc.toObject(Gasto.class);
                        g.setId(doc.getId()); //  ID para usar na exclusão

                        // Validações caso algum campo tenh nulo do banco
                        String usuario = g.getUsuarioNome() != null ? g.getUsuarioNome() : "Não informado";
                        String categoria = g.getCategoriaNome() != null ? g.getCategoriaNome() : "Não informado";
                        String forma = g.getFormaPagamentoNome() != null ? g.getFormaPagamentoNome() : "Não informado";

                        //texto que vai aparecer dentro do card
                        String texto =
                                "Descrição: " + g.getDescricao() +
                                        "\nValor: R$ " + g.getValor() +
                                        "\nUsuário: " + usuario +
                                        "\nCategoria: " + categoria +
                                        "\nPagamento: " + forma;

                        criarCard(texto, true, g.getId());
                    }

                    // avisa o app que a busca de gastos terminou
                    gastosCarregados = true;
                    verificarListaVazia();
                });
    }

    // Busca todos os documentos da coleção
    private void carregarReceitas() {
        db.collection("receitas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        totalMovimentacoes++;

                        // converte o documento para o objeto
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

                    //  avisa o app que a busca de receitas terminou
                    receitasCarregadas = true;
                    verificarListaVazia();
                });
    }

    // Função que cria toda a interface do card
    private void criarCard(String texto, boolean gasto, String id) {

        // Cria o container principal
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(30, 25, 30, 25);

        // Define o fundo do card
        GradientDrawable fundo = new GradientDrawable();
        fundo.setCornerRadius(28); // Cantos arredondados

        // escolhe as cores dependendo se e gasto o receita
        if (gasto) {
            fundo.setColor(Color.parseColor("#2A1515"));
            fundo.setStroke(3, Color.parseColor("#EF5350"));
        } else {
            fundo.setColor(Color.parseColor("#132A18"));
            fundo.setStroke(3, Color.parseColor("#66BB6A"));
        }

        card.setBackground(fundo);

        // Configura o tamanho
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, 0, 0, 24); // Margem embaixo do card para ele não grudar no próximo
        card.setLayoutParams(params);

        // Cria o TextView do título do card
        TextView titulo = new TextView(this);
        titulo.setText(gasto ? "🔴 GASTO" : "🟢 RECEITA");
        titulo.setTextSize(16);
        titulo.setTypeface(null, Typeface.BOLD); // Deixa em negrito
        titulo.setTextColor(Color.WHITE);
        // resto dos dados
        TextView txt = new TextView(this);
        txt.setText(texto);
        txt.setTextSize(14);
        txt.setTextColor(Color.parseColor("#E0E0E0"));
        txt.setPadding(0, 12, 0, 20);

        // Cria o botão de excluir
        Button btnExcluir = new Button(this);
        btnExcluir.setText("Excluir");
        btnExcluir.setTextSize(11);
        btnExcluir.setBackgroundResource(R.drawable.botao_excluir); // Pega o XML de estilo do botão
        btnExcluir.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                280,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        btnParams.gravity = Gravity.CENTER_HORIZONTAL;
        btnExcluir.setLayoutParams(btnParams);

        btnExcluir.setOnClickListener(v -> {
            // Descobre em qual coleção o documento está com base no tipo do card
            String colecao = gasto ? "gastos" : "receitas";

            // Deleta o documento direto do Firebase usando o ID
            db.collection(colecao)
                    .document(id)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Excluído!", Toast.LENGTH_SHORT).show();
                        atualizarLista(); // Recarrega a tela para sumir com o card deletado
                    });
        });

        card.addView(titulo);
        card.addView(txt);
        card.addView(btnExcluir);

        layoutGastos.addView(card);
    }
    private void verificarListaVazia() {
        // validação se as duas coleções já tiverem respondido
        if (gastosCarregados && receitasCarregadas) {

            if (totalMovimentacoes == 0) {
                // Cria o aviso na tela
                TextView vazio = new TextView(this);
                vazio.setText("Nenhuma movimentação cadastrada.");
                vazio.setTextSize(16);
                vazio.setTextColor(Color.WHITE);
                vazio.setGravity(Gravity.CENTER);

                layoutGastos.addView(vazio);
            }
        }
    }
}