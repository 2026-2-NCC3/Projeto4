package com.example.myapplication.aba_cursos;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.AgendaItem;
import com.example.myapplication.AgendaManager;
import com.example.myapplication.R;
import java.util.Arrays;
import java.util.List;

public class CursosActivity extends AppCompatActivity {

    private CursoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        List<Curso> cursos = Arrays.asList(
                new Curso("Preparatório ENEM & Redação Nota 1000", 40, "Inscrito"),
                new Curso("Workshop: Carreiras em Tecnologia e Programação", 16, "Disponível"),
                new Curso("Orientação Profissional e Mercado de Trabalho", 20, "Disponível"),
                new Curso("Imersão Universitária - FECAP & Parceiras", 10, "Concluído"),
                new Curso("Empreendedorismo Jovem e Liderança", 24, "Disponível")
        );

        adapter = new CursoAdapter(cursos, (curso, position) -> {
            if ("Disponível".equalsIgnoreCase(curso.getStatus())) {
                curso.setStatus("Inscrito");
                adapter.notifyItemChanged(position);
                
                // Adiciona na agenda
                AgendaManager.getInstance().adicionarItem(new AgendaItem("2026-04-10", "19:00:00", "21:00:00", "Primeiro Encontro", "Aula 1 — " + curso.getNome(), "Sala Virtual 1", false));
                
                Toast.makeText(this, "Inscrição realizada com sucesso: " + curso.getNome(), Toast.LENGTH_SHORT).show();
            } else if ("Inscrito".equalsIgnoreCase(curso.getStatus())) {
                Toast.makeText(this, "Você já está inscrito neste curso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Este curso já foi concluído.", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView rvCursos = findViewById(R.id.rvCursos);
        rvCursos.setLayoutManager(new LinearLayoutManager(this));
        rvCursos.setAdapter(adapter);
    }
}