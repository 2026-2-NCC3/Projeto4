package com.example.myapplication.aba_cursos;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.content.Intent;
import com.example.myapplication.AgendaActivity;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.List;

import com.example.myapplication.AgendaItem;
import com.example.myapplication.AgendaManager;

public class CursosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCursos;
    private CursoAdapter adapter;
    private List<Curso> listaDeCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        Button btnVerAgenda = findViewById(R.id.btnVerAgenda);
        if (btnVerAgenda != null) {
            btnVerAgenda.setOnClickListener(v -> {
                Intent intent = new Intent(CursosActivity.this, AgendaActivity.class);
                startActivity(intent);
            });
        }

        recyclerViewCursos = findViewById(R.id.rvCursos);
        recyclerViewCursos.setLayoutManager(new LinearLayoutManager(this));


        listaDeCursos = new ArrayList<>();
        listaDeCursos.add(new Curso("Preparatório ENEM & Redação Nota 1000", 40, "Inscrito"));
        listaDeCursos.add(new Curso("Workshop: Carreiras em Tecnologia e Programação", 16, "Disponível"));
        listaDeCursos.add(new Curso("Orientação Profissional e Mercado de Trabalho", 20, "Disponível"));
        listaDeCursos.add(new Curso("Imersão Universitária - FECAP & Parceiras", 10, "Concluído"));
        listaDeCursos.add(new Curso("Empreendedorismo Jovem e Liderança", 24, "Disponível"));


        adapter = new CursoAdapter(listaDeCursos, (curso, position) -> {
            if ("Disponível".equalsIgnoreCase(curso.getStatus())) {
                curso.setStatus("Inscrito");
                adapter.notifyItemChanged(position);
                
                // Gera dados fictícios e adiciona na agenda
                AgendaItem novoEncontro = new AgendaItem(
                        "2026-04-10", "19:00:00", "21:00:00",
                        "Primeiro Encontro", "Aula 1 — " + curso.getNome(),
                        "Sala Virtual 1", false
                );
                AgendaManager.getInstance().adicionarItem(novoEncontro);
                
                Toast.makeText(CursosActivity.this, "Inscrição realizada com sucesso: " + curso.getNome(), Toast.LENGTH_SHORT).show();
            } else if ("Inscrito".equalsIgnoreCase(curso.getStatus())) {
                Toast.makeText(CursosActivity.this, "Você já está inscrito neste curso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CursosActivity.this, "Este curso já foi concluído.", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewCursos.setAdapter(adapter);
    }
}