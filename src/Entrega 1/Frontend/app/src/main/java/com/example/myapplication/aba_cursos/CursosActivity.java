package com.example.myapplication.aba_cursos;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.ArrayList;

public class CursosActivity extends AppCompatActivity {

    private RecyclerView rvCursos;
    private CursoAdapter adapter;
    private ArrayList listaDeCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        // 1. Inicializar e configurar a RecyclerView
        rvCursos = findViewById(R.id.rvCursos);
        rvCursos.setLayoutManager(new LinearLayoutManager(this));

        // 2. Criar a estrutura de dados (ArrayList) com os cursos
        listaDeCursos = new ArrayList<>();
        listaDeCursos.add(new Curso("Informática Básica", 40, "Inscrições Abertas"));
        listaDeCursos.add(new Curso("Inglês para Iniciantes", 60, "Em Andamento"));
        listaDeCursos.add(new Curso("Preparatório para o ENEM", 120, "Disponível"));
        listaDeCursos.add(new Curso("Orientação Profissional", 20, "Inscrições Abertas"));

        // 3. Conectar a lista ao Adapter e o Adapter à RecyclerView
        adapter = new CursoAdapter(listaDeCursos);
        rvCursos.setAdapter(adapter);
    }
}