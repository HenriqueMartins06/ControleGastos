package com.example.controlegastos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.Gasto;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    LinearLayout layoutGastos;
    Button btnUsuario, btnCategoria, btnFormaPagamento, btnGasto;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        layoutGastos = findViewById(R.id.layoutGastos);
        btnUsuario = findViewById(R.id.btnUsuario);
        btnCategoria = findViewById(R.id.btnCategoria);
        btnFormaPagamento = findViewById(R.id.btnFormaPagamento);
        btnGasto = findViewById(R.id.btnGasto);

        atualizarLista();

        btnUsuario.setOnClickListener(v ->
                startActivity(new Intent(this, UsuarioActivity.class)));

        btnCategoria.setOnClickListener(v ->
                startActivity(new Intent(this, CategoriaActivity.class)));

        btnFormaPagamento.setOnClickListener(v ->
                startActivity(new Intent(this, FormaPagamentoActivity.class)));

        btnGasto.setOnClickListener(v ->
                startActivity(new Intent(this, GastoActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();
    }

    private void atualizarLista() {

        layoutGastos.removeAllViews();

        db.collection("gastos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (queryDocumentSnapshots.isEmpty()) {
                        TextView vazio = new TextView(this);
                        vazio.setText("Nenhum gasto cadastrado.");
                        vazio.setTextSize(16);
                        vazio.setTextColor(Color.BLACK);
                        layoutGastos.addView(vazio);
                        return;
                    }

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        Gasto g = doc.toObject(Gasto.class);
                        g.setId(doc.getId());

                        LinearLayout linha = new LinearLayout(this);
                        linha.setOrientation(LinearLayout.HORIZONTAL);
                        linha.setBackgroundResource(R.drawable.card_gasto);
                        linha.setPadding(20, 20, 20, 20);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        params.setMargins(0, 0, 0, 20);
                        linha.setLayoutParams(params);

                        TextView txt = new TextView(this);
                        txt.setText(
                                "Descrição: " + g.getDescricao() +
                                        "\nValor: R$ " + g.getValor()
                        );

                        txt.setTextSize(16);
                        txt.setTextColor(Color.BLACK);

                        txt.setLayoutParams(new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1
                        ));

                        Button btnExcluir = new Button(this);
                        btnExcluir.setText("Excluir");
                        btnExcluir.setBackgroundResource(R.drawable.botao_excluir);
                        btnExcluir.setTextColor(Color.WHITE);

                        btnExcluir.setOnClickListener(v -> {
                            db.collection("gastos")
                                    .document(g.getId())
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Gasto excluído!", Toast.LENGTH_SHORT).show();
                                        atualizarLista();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Erro ao excluir!", Toast.LENGTH_SHORT).show();
                                    });
                        });

                        linha.addView(txt);
                        linha.addView(btnExcluir);

                        layoutGastos.addView(linha);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao carregar gastos.", Toast.LENGTH_SHORT).show();
                });
    }
}