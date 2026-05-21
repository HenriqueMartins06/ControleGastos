package com.example.controlegastos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UsuarioActivity extends AppCompatActivity {

    EditText edtNome;
    Button btnSalvar, btnAtualizar, btnExcluir, btnVoltar;
    LinearLayout layoutUsuarios;

    FirebaseFirestore db;
    String idSelecionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        db = FirebaseFirestore.getInstance();

        edtNome = findViewById(R.id.edtNomeUsuario);
        btnSalvar = findViewById(R.id.btnSalvarUsuario);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnVoltar = findViewById(R.id.btnVoltar);
        layoutUsuarios = findViewById(R.id.layoutUsuarios);

        atualizarLista();

        btnSalvar.setOnClickListener(v -> salvarUsuario());

        btnAtualizar.setOnClickListener(v -> atualizarUsuario());

        btnExcluir.setOnClickListener(v -> excluirUsuario());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void salvarUsuario(){
        String nome = edtNome.getText().toString().trim();

        if(nome.isEmpty()){
            edtNome.setError("Digite um nome!");
            return;
        }

        Usuario usuario = new Usuario(nome);

        db.collection("usuarios")
                .add(usuario)
                .addOnSuccessListener(documentReference -> {
                    edtNome.setText("");
                    Toast.makeText(this, "Usuário salvo!", Toast.LENGTH_SHORT).show();
                    atualizarLista();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void atualizarUsuario(){
        String nome = edtNome.getText().toString().trim();

        if(idSelecionado.isEmpty()){
            Toast.makeText(this, "Selecione um usuário!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(nome.isEmpty()){
            edtNome.setError("Digite um nome!");
            return;
        }

        db.collection("usuarios")
                .document(idSelecionado)
                .update("nome", nome)
                .addOnSuccessListener(aVoid -> {
                    limparCampos();
                    Toast.makeText(this, "Usuário atualizado!", Toast.LENGTH_SHORT).show();
                    atualizarLista();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao atualizar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void excluirUsuario(){
        if(idSelecionado.isEmpty()){
            Toast.makeText(this, "Selecione um usuário!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("usuarios")
                .document(idSelecionado)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    limparCampos();
                    Toast.makeText(this, "Usuário excluído!", Toast.LENGTH_SHORT).show();
                    atualizarLista();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao excluir: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void atualizarLista(){
        layoutUsuarios.removeAllViews();

        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        Usuario u = doc.toObject(Usuario.class);
                        u.setId(doc.getId());

                        TextView txt = new TextView(this);
                        txt.setText(u.getNome());
                        txt.setTextSize(16);
                        txt.setPadding(10, 10, 10, 10);

                        txt.setOnClickListener(v -> {
                            idSelecionado = u.getId();
                            edtNome.setText(u.getNome());
                            Toast.makeText(this, "Usuário selecionado!", Toast.LENGTH_SHORT).show();
                        });

                        layoutUsuarios.addView(txt);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao listar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void limparCampos(){
        edtNome.setText("");
        idSelecionado = "";
    }
}