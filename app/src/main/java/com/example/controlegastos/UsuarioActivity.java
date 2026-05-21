package com.example.controlegastos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UsuarioActivity extends AppCompatActivity {

    EditText edtNome, edtIdExcluir;
    Button btnSalvar, btnVoltar, btnExcluir;
    TextView txtLista;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        db = FirebaseFirestore.getInstance();

        // vincula os componente
        edtNome = findViewById(R.id.edtNomeUsuario);
        edtIdExcluir = findViewById(R.id.edtIdExcluir);
        btnSalvar = findViewById(R.id.btnSalvarUsuario);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnVoltar = findViewById(R.id.btnVoltar);
        txtLista = findViewById(R.id.txtUsuarios);

        atualizar();

        // salvar o usuario
        btnSalvar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString();

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
                        atualizar();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        // exclui o usuario pelo id do firebase
        btnExcluir.setOnClickListener(v -> {
            String id = edtIdExcluir.getText().toString();

            if(id.isEmpty()){
                edtIdExcluir.setError("Digite um ID!");
                return;
            }

            db.collection("usuarios")
                    .document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        edtIdExcluir.setText("");
                        Toast.makeText(this, "Usuário excluído!", Toast.LENGTH_SHORT).show();
                        atualizar();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao excluir!", Toast.LENGTH_SHORT).show();
                    });
        });

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void atualizar(){

        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    StringBuilder texto = new StringBuilder("Lista de Usuários:\n\n");

                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        Usuario u = doc.toObject(Usuario.class);
                        u.setId(doc.getId());

                        texto.append("(ID: ").append(u.getId()).append(") ")
                                .append(u.getNome()).append("\n");
                    }

                    txtLista.setText(texto.toString());
                })
                .addOnFailureListener(e -> {
                    txtLista.setText("Erro ao listar: " + e.getMessage());
                });
    }
}