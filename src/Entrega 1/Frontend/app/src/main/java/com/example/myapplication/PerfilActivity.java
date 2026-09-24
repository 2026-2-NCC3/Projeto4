package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.aba_cursos.CursosActivity;

public class PerfilActivity extends AppCompatActivity {

    ImageView avatarPerfil;
    TextView textNome, textEscola, btnSair;
    LinearLayout layoutPresenca, layoutCertificados, layoutConfiguracoes, layoutAjuda, layoutSobre, layoutInicio, layoutCursos, layoutAgenda, layoutPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        avatarPerfil = findViewById(R.id.avatarPerfil);
        textNome = findViewById(R.id.textNome);
        textEscola = findViewById(R.id.textEscola);
        btnSair = findViewById(R.id.btnSair);
        layoutPresenca = findViewById(R.id.layoutPresenca);
        layoutCertificados = findViewById(R.id.layoutCertificados);
        layoutConfiguracoes = findViewById(R.id.layoutConfiguracoes);
        layoutAjuda = findViewById(R.id.layoutAjuda);
        layoutSobre = findViewById(R.id.layoutSobre);
        layoutInicio = findViewById(R.id.layoutInicio);
        layoutCursos = findViewById(R.id.layoutCursos);
        layoutAgenda = findViewById(R.id.layoutAgenda);
        layoutPerfil = findViewById(R.id.layoutPerfil);

        layoutInicio.setOnClickListener(v -> {
            Intent inicio = new Intent(PerfilActivity.this, PaginaInicial.class);
            startActivity(inicio);
        });
        layoutCursos.setOnClickListener(v -> {
            Intent cursos = new Intent(PerfilActivity.this, CursosActivity.class);
            startActivity(cursos);
        });
        layoutAgenda.setOnClickListener(v -> {
            Intent agenda = new Intent(PerfilActivity.this, AgendaActivity.class);
            startActivity(agenda);
        });
    }

}

