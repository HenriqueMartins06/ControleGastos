package com.example.controlegastos;

import com.google.firebase.firestore.FirebaseFirestore;
import dao.GastoDAO;
import com.example.controlegastos.modelos.Gasto;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;
    Button btnUsuario, btnCategoria, btnGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // TESTE FIREBASE
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> teste = new HashMap<>();
        teste.put("mensagem", "firebase funcionando");

        db.collection("teste").add(teste);

        // Componente
        txtResultado = findViewById(R.id.txtResultado);
        btnUsuario = findViewById(R.id.btnUsuario);
        btnCategoria = findViewById(R.id.btnCategoria);
        btnGasto = findViewById(R.id.btnGasto);

        atualizarLista();

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
        atualizarLista();
    }

    private void atualizarLista() {
        GastoDAO dao = new GastoDAO(this);
        dao.Abrir();
        List<Gasto> lista = dao.ListarTudo();
        dao.Fechar();

        StringBuilder texto = new StringBuilder();

        for (Gasto g : lista) {
            // manda imprimir, juntado no dao
            texto.append("ID: ").append(g.getId()).append("\n")
                    .append(g.getDescricao()).append("\n")
                    .append("Valor: R$ ").append(g.getValor()).append("\n")
                    .append("----------------------------\n");
        }

        txtResultado.setText(texto.toString());
    }
}