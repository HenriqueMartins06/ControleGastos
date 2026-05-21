package com.example.controlegastos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GastoActivity extends AppCompatActivity {

    Spinner spUsuario, spCategoria;
    EditText edtValor, edtDescricao, edtIdExcluir;
    Button btnSalvar, btnExcluir, btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        spUsuario = findViewById(R.id.spUsuario);
        spCategoria = findViewById(R.id.spCategoria);
        edtValor = findViewById(R.id.edtValor);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtIdExcluir = findViewById(R.id.edtIdExcluir);

        btnSalvar = findViewById(R.id.btnSalvarGasto);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnVoltar = findViewById(R.id.btnVoltar);

        btnSalvar.setOnClickListener(v -> {
            Toast.makeText(this, "Gastos vamos ligar no Firebase depois", Toast.LENGTH_SHORT).show();
        });

        btnExcluir.setOnClickListener(v -> {
            Toast.makeText(this, "Excluir gasto depois", Toast.LENGTH_SHORT).show();
        });

        btnVoltar.setOnClickListener(v -> finish());
    }
}