package com.example.controlegastos;

import com.google.firebase.firestore.FirebaseFirestore;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;
    Button btnUsuario, btnCategoria, btnGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Componente
        txtResultado = findViewById(R.id.txtResultado);
        btnUsuario = findViewById(R.id.btnUsuario);
        btnCategoria = findViewById(R.id.btnCategoria);
        btnGasto = findViewById(R.id.btnGasto);

        // TESTE FIREBASE
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> teste = new HashMap<>();
        teste.put("mensagem", "firebase funcionando");

        db.collection("teste").add(teste);

        txtResultado.setText("Firebase funcionando!\nAgora vamos migrar os dados.");

        // pra navegacao
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
        // atualizarLista();
    }
}