package com.example.controlegastos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.Gasto;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;
    Button btnUsuario, btnCategoria, btnGasto;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        txtResultado = findViewById(R.id.txtResultado);
        btnUsuario = findViewById(R.id.btnUsuario);
        btnCategoria = findViewById(R.id.btnCategoria);
        btnGasto = findViewById(R.id.btnGasto);

        atualizarLista();

        btnUsuario.setOnClickListener(v -> {
            startActivity(new Intent(this, UsuarioActivity.class));
        });

        btnCategoria.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoriaActivity.class));
        });

        btnGasto.setOnClickListener(v -> {
            startActivity(new Intent(this, GastoActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();
    }

    private void atualizarLista(){

        db.collection("gastos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    StringBuilder texto = new StringBuilder();

                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){

                        Gasto g = doc.toObject(Gasto.class);
                        g.setId(doc.getId());

                        texto.append(g.getDescricao()).append("\n")
                                .append("Valor: R$ ").append(g.getValor()).append("\n")
                                .append("------------------------\n");
                    }

                    if(texto.length() == 0){
                        txtResultado.setText("Nenhum gasto cadastrado.");
                    } else {
                        txtResultado.setText(texto.toString());
                    }
                })
                .addOnFailureListener(e -> {
                    txtResultado.setText("Erro ao carregar gastos.");
                });
    }
}