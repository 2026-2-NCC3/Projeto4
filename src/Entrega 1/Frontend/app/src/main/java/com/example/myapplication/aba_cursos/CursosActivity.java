package com.example.myapplication.aba_cursos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AgendaActivity;
import com.example.myapplication.PaginaInicial;
import com.example.myapplication.PerfilActivity;
import com.example.myapplication.R;
import java.util.ArrayList;

public class CursosActivity extends AppCompatActivity {

    private RecyclerView rvCursos;
    private CursoAdapter adapter;
    private ArrayList listaDeCursos;
    LinearLayout layoutInicio, layoutCursos, layoutAgenda, layoutPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        layoutInicio = findViewById(R.id.layoutInicio);
        layoutCursos = findViewById(R.id.layoutCursos);
        layoutAgenda = findViewById(R.id.layoutAgenda);
        layoutPerfil = findViewById(R.id.layoutPerfil);

        // 1. Inicializar e configurar a RecyclerView
        rvCursos = findViewById(R.id.rvCursos);
        rvCursos.setLayoutManager(new LinearLayoutManager(this));

        // 2. Criar a estrutura de dados (ArrayList) com os cursos
        listaDeCursos = new ArrayList<>();
        // 1. Criar a lista de cursos com o construtor da sua classe Curso
        // 2. Criar a estrutura de dados com os cursos
        listaDeCursos = new ArrayList<>();

        listaDeCursos.add(new Curso("Preparatório ENEM & Redação Nota 1000", 40, "Inscrito"));
        listaDeCursos.add(new Curso("Workshop: Carreiras em Tecnologia e Programação", 16, "Disponível"));
        listaDeCursos.add(new Curso("Orientação Profissional e Mercado de Trabalho", 20, "Disponível"));
        listaDeCursos.add(new Curso("Imersão Universitária - FECAP & Parceiras", 10, "Concluído"));
        listaDeCursos.add(new Curso("Empreendedorismo Jovem e Liderança", 24, "Disponível"));

        // 3. Conectar a lista ao Adapter e o Adapter à RecyclerView
        adapter = new CursoAdapter(listaDeCursos);
        rvCursos.setAdapter(adapter);

        layoutInicio.setOnClickListener(v -> {
            Intent inicio = new Intent(CursosActivity.this, PaginaInicial.class);
            startActivity(inicio);
        });
        layoutAgenda.setOnClickListener(v -> {
            Intent agenda = new Intent(CursosActivity.this, AgendaActivity.class);
            startActivity(agenda);
        });
        layoutPerfil.setOnClickListener(v -> {
            Intent perfil = new Intent(CursosActivity.this, PerfilActivity.class);
            startActivity(perfil);
        });
    }
}