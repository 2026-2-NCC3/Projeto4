package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    ImageView avatarPerfil;
    TextView textNome, textEscola, btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        avatarPerfil = findViewById(R.id.avatarPerfil);
        textNome = findViewById(R.id.textNome);
        textEscola = findViewById(R.id.textEscola);
        btnSair = findViewById(R.id.btnSair);


    }

}

