package com.example.controlegastos;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.Categoria;
import com.example.controlegastos.modelos.Gasto;
import com.example.controlegastos.modelos.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GastoActivity extends AppCompatActivity {

    Spinner spUsuario, spCategoria;
    EditText edtValor, edtDescricao;
    Button btnSalvar, btnVoltar;

    FirebaseFirestore db;

    List<Usuario> listaUsuarios = new ArrayList<>();
    List<Categoria> listaCategorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        db = FirebaseFirestore.getInstance();

        spUsuario = findViewById(R.id.spUsuario);
        spCategoria = findViewById(R.id.spCategoria);
        edtValor = findViewById(R.id.edtValor);
        edtDescricao = findViewById(R.id.edtDescricao);

        btnSalvar = findViewById(R.id.btnSalvarGasto);
        btnVoltar = findViewById(R.id.btnVoltar);

        carregarUsuarios();
        carregarCategorias();

        btnSalvar.setOnClickListener(v -> salvarGasto());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void carregarUsuarios(){
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaUsuarios.clear();

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item
                    );

                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        Usuario u = doc.toObject(Usuario.class);
                        u.setId(doc.getId());

                        listaUsuarios.add(u);
                        adapter.add(u.getNome());
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spUsuario.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao carregar usuários", Toast.LENGTH_SHORT).show();
                });
    }

    private void carregarCategorias(){
        db.collection("categorias")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaCategorias.clear();

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item
                    );

                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        Categoria c = doc.toObject(Categoria.class);
                        c.setId(doc.getId());

                        listaCategorias.add(c);
                        adapter.add(c.getNome());
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategoria.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao carregar categorias", Toast.LENGTH_SHORT).show();
                });
    }

    private void salvarGasto(){
        String valorTexto = edtValor.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();

        if(valorTexto.isEmpty()){
            edtValor.setError("Digite o valor!");
            return;
        }

        if(descricao.isEmpty()){
            edtDescricao.setError("Digite a descrição!");
            return;
        }

        if(listaUsuarios.isEmpty()){
            Toast.makeText(this, "Cadastre um usuário primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(listaCategorias.isEmpty()){
            Toast.makeText(this, "Cadastre uma categoria primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor = Double.parseDouble(valorTexto);

        Usuario usuario = listaUsuarios.get(spUsuario.getSelectedItemPosition());
        Categoria categoria = listaCategorias.get(spCategoria.getSelectedItemPosition());

        Gasto gasto = new Gasto(
                valor,
                descricao,
                usuario.getId(),
                categoria.getId()
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
}