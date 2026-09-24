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
    }
}