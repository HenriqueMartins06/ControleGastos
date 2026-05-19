package com.example.controlegastos;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlegastos.modelos.*;

import java.util.List;

import dao.CategoriaDAO;
import dao.GastoDAO;
import dao.UsuarioDAO;

public class GastoActivity extends AppCompatActivity {

    Spinner spUsuario, spCategoria;
    EditText edtValor, edtDescricao, edtIdExcluir;
    Button btnSalvar, btnVoltar, btnExcluir;

    List<Usuario> listaUsuarios;
    List<Categoria> listaCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        // mapear os componente
        spUsuario = findViewById(R.id.spUsuario);
        spCategoria = findViewById(R.id.spCategoria);
        edtValor = findViewById(R.id.edtValor);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtIdExcluir = findViewById(R.id.edtIdExcluir);

        btnSalvar = findViewById(R.id.btnSalvarGasto);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnVoltar = findViewById(R.id.btnVoltar);

        carregarSpinners();

        // salva
        btnSalvar.setOnClickListener(v -> {
            String valorStr = edtValor.getText().toString();

            if (valorStr.isEmpty()) {
                edtValor.setError("Informe o valor!");
                return;
            }

            // precisa ter alguém selecionado
            if (listaUsuarios.isEmpty() || listaCategorias.isEmpty()) {
                Toast.makeText(this, "Cadastre Usuários e Categorias primeiro!", Toast.LENGTH_LONG).show();
                return;
            }

            double valor = Double.parseDouble(valorStr);
            String descricao = edtDescricao.getText().toString();

            // Pega os IDs baseados na seleção do Spinner
            int uId = listaUsuarios.get(spUsuario.getSelectedItemPosition()).getId();
            int cId = listaCategorias.get(spCategoria.getSelectedItemPosition()).getId();

            GastoDAO dao = new GastoDAO(this);
            dao.Abrir();
            dao.Inserir(new Gasto(valor, descricao, uId, cId));
            dao.Fechar();

            // Limpa campos e avisa
            edtValor.setText("");
            edtDescricao.setText("");
            Toast.makeText(this, "Gasto registrado com sucesso!", Toast.LENGTH_SHORT).show();
        });

        // excluir
        btnExcluir.setOnClickListener(v -> {
            String idString = edtIdExcluir.getText().toString();

            if(idString.isEmpty()){
                edtIdExcluir.setError("Digite o ID!");
                return;
            }

            int id = Integer.parseInt(idString);

            GastoDAO dao = new GastoDAO(this);
            dao.Abrir();
            Gasto g = new Gasto();
            g.setId(id);
            dao.Excluir(g);
            dao.Fechar();

            edtIdExcluir.setText("");
            Toast.makeText(this, "Gasto (#" + id + ") removido!", Toast.LENGTH_SHORT).show();
        });

        // voltar
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void carregarSpinners() {
        // Busca usuario
        UsuarioDAO uDao = new UsuarioDAO(this);
        uDao.Abrir();
        listaUsuarios = uDao.ListarTudo();
        uDao.Fechar();

        // Busca as categoria
        CategoriaDAO cDao = new CategoriaDAO(this);
        cDao.Abrir();
        listaCategorias = cDao.ListarTudo();
        cDao.Fechar();

        // Preenche Spinner do Usuários
        ArrayAdapter<String> adapterU = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        for (Usuario u : listaUsuarios) { adapterU.add(u.getNome()); }
        adapterU.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUsuario.setAdapter(adapterU);

        // Preenche Spinner do Categorias
        ArrayAdapter<String> adapterC = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        for (Categoria c : listaCategorias) { adapterC.add(c.getNome()); }
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapterC);
    }
}