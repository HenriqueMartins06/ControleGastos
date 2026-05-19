package com.example.controlegastos;

import dao.GastoDAO;
import com.example.controlegastos.modelos.Gasto;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

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
           //manda imprimir, juntado no dao
            texto.append("ID: ").append(g.getId()).append("\n")
                    .append(g.getDescricao()).append("\n")
                    .append("Valor: R$ ").append(g.getValor()).append("\n")
                    .append("----------------------------\n");
        }

        txtResultado.setText(texto.toString());
    }
}