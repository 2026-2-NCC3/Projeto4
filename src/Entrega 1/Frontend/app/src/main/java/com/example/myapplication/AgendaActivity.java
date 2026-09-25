package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.aba_cursos.CursosActivity;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Tela de Agenda do aluno.
 *
 * Agora a tela usa dados reais vindos do backend via Retrofit.
 */
public class AgendaActivity extends AppCompatActivity {

    LinearLayout agendaContainer, layoutInicio, layoutCursos, layoutAgenda, layoutPerfil;
    TextView textVazio;

    @Override
    protected void onResume() {
        super.onResume();
        buscarAgendaDoBackend();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        agendaContainer = findViewById(R.id.agendaContainer);
        layoutInicio = findViewById(R.id.layoutInicio);
        layoutCursos = findViewById(R.id.layoutCursos);
        layoutAgenda = findViewById(R.id.layoutAgenda);
        layoutPerfil = findViewById(R.id.layoutPerfil);
        textVazio = findViewById(R.id.textVazio);

        layoutInicio.setOnClickListener(v -> {
            Intent inicio = new Intent(AgendaActivity.this, PaginaInicial.class);
            startActivity(inicio);
        });
        layoutCursos.setOnClickListener(v -> {
            Intent cursos = new Intent(AgendaActivity.this, CursosActivity.class);
            startActivity(cursos);
        });
        layoutPerfil.setOnClickListener(v -> {
            Intent perfil = new Intent(AgendaActivity.this, PerfilActivity.class);
            startActivity(perfil);
        });

        buscarAgendaDoBackend();
    }

    private void buscarAgendaDoBackend() {
        // Pega o token salvo no login
        String token = SessionManager.getToken(this);
        if (token == null) {
            Toast.makeText(this, "Usuário não logado!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Faz a requisição usando o ApiClient que já está configurado no projeto
        ApiClient.getApiService().getAgenda("Bearer " + token, null).enqueue(new Callback<List<AgendaItem>>() {
            @Override
            public void onResponse(Call<List<AgendaItem>> call, Response<List<AgendaItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AgendaItem> agenda = response.body();
                    exibirAgenda(agenda);
                } else {
                    Toast.makeText(AgendaActivity.this, "Erro ao carregar a agenda.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AgendaItem>> call, Throwable t) {
                Toast.makeText(AgendaActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API", "Erro na API da agenda: ", t);
            }
        });
    }

    /**
     * Monta um card por item da agenda dentro do agendaContainer.
     * Se a lista estiver vazia, mostra a mensagem de "nenhum encontro".
     */
    private void exibirAgenda(List<AgendaItem> agenda) {
        agendaContainer.removeAllViews();

        if (agenda.isEmpty()) {
            textVazio.setVisibility(View.VISIBLE);
            return;
        }

        textVazio.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);

        for (AgendaItem item : agenda) {
            View card = inflater.inflate(R.layout.item_agenda, agendaContainer, false);

            TextView textDataHora = card.findViewById(R.id.textDataHora);
            TextView textStatus = card.findViewById(R.id.textStatus);
            TextView textTituloEncontro = card.findViewById(R.id.textTituloEncontro);
            TextView textCurso = card.findViewById(R.id.textCurso);
            TextView textLocal = card.findViewById(R.id.textLocal);

            textDataHora.setText(formatarDataHora(item));
            textStatus.setText(item.attended ? "Presença confirmada" : "Aguardando encontro");
            textTituloEncontro.setText(item.title);
            textCurso.setText(item.courseTitle);
            textLocal.setText("📍 " + item.location);

            agendaContainer.addView(card);
        }
    }

    /**
     * Formata a data (YYYY-MM-DD) e o intervalo de horário (HH:MM:SS) do
     * encontro para exibição, ex.: "01/03 · 19:00 - 21:00".
     */
    private String formatarDataHora(AgendaItem item) {
        if (item.activityDate == null || item.startTime == null) return "";

        String[] partesData = item.activityDate.split("-");
        String dataFormatada = partesData.length == 3
                ? partesData[2] + "/" + partesData[1]
                : item.activityDate;

        String horaInicio = item.startTime.length() >= 5 ? item.startTime.substring(0, 5) : item.startTime;
        
        String horaFim = "";
        if (item.endTime != null && item.endTime.length() >= 5) {
            horaFim = " - " + item.endTime.substring(0, 5);
        }

        return dataFormatada + " · " + horaInicio + horaFim;
    }
}
