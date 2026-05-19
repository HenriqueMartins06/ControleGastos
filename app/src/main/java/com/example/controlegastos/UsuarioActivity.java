package com.example.controlegastos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast; // adicionei o toast pros avisos
import androidx.appcompat.app.AppCompatActivity;

import dao.UsuarioDAO;
import com.example.controlegastos.modelos.Usuario;

import java.util.List;

public class UsuarioActivity extends AppCompatActivity {

    EditText edtNome, edtIdExcluir;
    Button btnSalvar, btnVoltar, btnExcluir;
    TextView txtLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

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

            UsuarioDAO dao = new UsuarioDAO(this);
            dao.Abrir();
            dao.Inserir(new Usuario(nome));
            dao.Fechar();

            edtNome.setText("");
            atualizar();
        });

        // excluui o usuario com a trava de seguranca
        btnExcluir.setOnClickListener(v -> {
            String idString = edtIdExcluir.getText().toString();

            if(idString.isEmpty()){
                edtIdExcluir.setError("Digite um ID!");
                return;
            }

            int id = Integer.parseInt(idString);
            UsuarioDAO dao = new UsuarioDAO(this);

            // aqui a gente confere se o cara tem gasto antes de apagar
            if (dao.temGasto(id)) {
                Toast.makeText(this, "Não pode excluir! Esse usuário tem gastos.", Toast.LENGTH_SHORT).show();
            } else {
                // se nao tiver nada, apaga normal
                dao.Abrir();
                Usuario u = new Usuario();
                u.setId(id);
                dao.Excluir(u);
                dao.Fechar();
                Toast.makeText(this, "Usuário excluído!", Toast.LENGTH_SHORT).show();
            }

            edtIdExcluir.setText("");
            atualizar();
        });


        btnVoltar.setOnClickListener(v -> finish());
    }

    private void atualizar(){
        UsuarioDAO dao = new UsuarioDAO(this);
        dao.Abrir();
        List<Usuario> lista = dao.ListarTudo();
        dao.Fechar();

        StringBuilder texto = new StringBuilder("Lista de Usuários:\n\n");

        for(Usuario u : lista){
            texto.append("(ID:").append(u.getId()).append(") ")
                    .append(u.getNome()).append("\n");
        }

        txtLista.setText(texto.toString());
    }
}