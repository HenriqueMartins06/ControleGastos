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
    Button btnSalvar, btnVoltar;
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
        btnVoltar = findViewById(R.id.btnVoltar);
        layoutUsuarios = findViewById(R.id.layoutUsuarios);

        atualizarLista();

        btnSalvar.setOnClickListener(v -> salvarOuAtualizar());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void salvarOuAtualizar() {
        String nome = edtNome.getText().toString().trim();

        if (nome.isEmpty()) {
            edtNome.setError("Digite um nome!");
            return;
        }

        if (idSelecionado.isEmpty()) {
            Usuario usuario = new Usuario(nome);

            db.collection("usuarios")
                    .add(usuario)
                    .addOnSuccessListener(documentReference -> {
                        limparCampos();
                        Toast.makeText(this, "Usuário salvo!", Toast.LENGTH_SHORT).show();
                        atualizarLista();
                    });

        } else {
            db.collection("usuarios")
                    .document(idSelecionado)
                    .update("nome", nome)
                    .addOnSuccessListener(unused -> {
                        limparCampos();
                        Toast.makeText(this, "Usuário atualizado!", Toast.LENGTH_SHORT).show();
                        atualizarLista();
                    });
        }
    }

    private void atualizarLista() {
        layoutUsuarios.removeAllViews();

        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Usuario u = doc.toObject(Usuario.class);
                        u.setId(doc.getId());

                        LinearLayout linha = new LinearLayout(this);
                        linha.setOrientation(LinearLayout.HORIZONTAL);
                        linha.setBackgroundResource(R.drawable.card_gasto);
                        linha.setPadding(20, 20, 20, 20);

                        LinearLayout.LayoutParams linhaParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                        linhaParams.setMargins(0, 0, 0, 16);
                        linha.setLayoutParams(linhaParams);

                        TextView txt = new TextView(this);
                        txt.setText(u.getNome());
                        txt.setTextSize(16);
                        txt.setTextColor(android.graphics.Color.BLACK);

                        LinearLayout.LayoutParams txtParams =
                                new LinearLayout.LayoutParams(
                                        0,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        1
                                );

                        txt.setLayoutParams(txtParams);

                        Button btnEditar = new Button(this);
                        btnEditar.setText("EDITAR");
                        btnEditar.setTextSize(11);
                        btnEditar.setPadding(20, 10, 20, 10);
                        btnEditar.setBackgroundResource(R.drawable.botao);
                        btnEditar.setTextColor(android.graphics.Color.WHITE);

                        LinearLayout.LayoutParams editarParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                        editarParams.setMargins(8, 0, 8, 0);
                        btnEditar.setLayoutParams(editarParams);

                        btnEditar.setOnClickListener(v -> {
                            idSelecionado = u.getId();
                            edtNome.setText(u.getNome());
                        });

                        Button btnExcluir = new Button(this);
                        btnExcluir.setText("EXCLUIR");
                        btnExcluir.setTextSize(11);
                        btnExcluir.setPadding(20, 10, 20, 10);
                        btnExcluir.setBackgroundResource(R.drawable.botao_excluir);
                        btnExcluir.setTextColor(android.graphics.Color.WHITE);

                        LinearLayout.LayoutParams excluirParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                        btnExcluir.setLayoutParams(excluirParams);

                        btnExcluir.setOnClickListener(v -> {
                            db.collection("usuarios")
                                    .document(u.getId())
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        limparCampos();
                                        Toast.makeText(this, "Usuário excluído!", Toast.LENGTH_SHORT).show();
                                        atualizarLista();
                                    });
                        });

                        linha.addView(txt);
                        linha.addView(btnEditar);
                        linha.addView(btnExcluir);

                        layoutUsuarios.addView(linha);
                    }
                });
    }

    private void limparCampos() {
        edtNome.setText("");
        idSelecionado = "";
    }
}