package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    ImageButton btnVoltar;
    Button btnCadastro, btnLogin;
    EditText editNome, editEmail, editCelular, editSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnCadastro = findViewById(R.id.btnCadastro);
        btnLogin = findViewById(R.id.btnLogin);
        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editCelular = findViewById(R.id.editCelular);
        editSenha = findViewById(R.id.editSenha);

        btnVoltar.setOnClickListener(view -> {
            Intent voltar = new Intent(CadastroActivity.this, MainActivity.class);;
            startActivity(voltar);
        });
        btnLogin.setOnClickListener(v -> {
            Intent login = new Intent(CadastroActivity.this, LoginActivity.class);
            startActivity(login);
        });
    }
}
