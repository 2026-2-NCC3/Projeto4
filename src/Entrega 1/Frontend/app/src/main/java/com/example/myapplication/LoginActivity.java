package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    ImageButton btnVoltar;
    Button btnLogin, btnCadastro;
    EditText LoginEmail, LoginSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnLogin = findViewById(R.id.btnLogin);
        btnCadastro = findViewById(R.id.btnCadastro);
        LoginEmail = findViewById(R.id.LoginEmail);
        LoginSenha = findViewById(R.id.LoginSenha);

        btnVoltar.setOnClickListener(v -> {
            Intent voltar = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(voltar);
        });
        btnCadastro.setOnClickListener(v -> {
            Intent cadastro = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(cadastro);
        });
    }
}