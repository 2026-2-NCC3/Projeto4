package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.aba_cursos.CursosActivity;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.SessionManager;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Tela de Agenda do aluno.
 *
 * Os encontros vêm de GET /api/agenda (backend em src/Entrega 1/Backend),
 * chamado via Retrofit (ver com.example.myapplication.network). A
 * requisição exige o header Authorization com o JWT do usuário logado,
 * lido de SessionManager — que é preenchido pela tela de Login após um
 * login bem-sucedido.
 */
public class AgendaActivity extends AppCompatActivity {

    LinearLayout agendaContainer, layoutInicio, layoutCursos, layoutAgenda, layoutPerfil;
    TextView textVazio;

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega a agenda sempre que a tela volta a aparecer (ex.: após
        // o aluno se inscrever em um novo curso).
        carregarAgenda();
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

        carregarAgenda();

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
    }

    /**
     * Busca a agenda do aluno logado no backend (GET /api/agenda) e
     * atualiza a tela com o resultado. Se não houver um token de sessão
     * (usuário não logado) ou a chamada falhar, mostra a mensagem de
     * "nenhum encontro" e avisa o motivo por um Toast.
     */
    private void carregarAgenda() {
        String token = SessionManager.getToken(this);

        if (token == null) {
            exibirAgenda(Collections.emptyList());
            Toast.makeText(this, "Faça login para ver sua agenda.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getApiService()
                .getAgenda("Bearer " + token, null)
                .enqueue(new Callback<List<AgendaItem>>() {
                    @Override
                    public void onResponse(Call<List<AgendaItem>> call, Response<List<AgendaItem>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            exibirAgenda(response.body());
                        } else {
                            exibirAgenda(Collections.emptyList());
                            Toast.makeText(
                                    AgendaActivity.this,
                                    "Não foi possível carregar sua agenda (erro " + response.code() + ").",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AgendaItem>> call, Throwable t) {
                        exibirAgenda(Collections.emptyList());
                        Toast.makeText(
                                AgendaActivity.this,
                                "Falha de conexão com o servidor.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    /**
     * Monta um card por item da agenda dentro do agendaContainer.
     * Se a lista estiver vazia, mostra a mensagem de "nenhum encontro".
     */
    private void exibirAgenda(List<AgendaItem> agenda) {
        agendaContainer.removeAllViews();

        if (agenda == null || agenda.isEmpty()) {
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
            textLocal.setText(item.location != null ? "📍 " + item.location : "");

            agendaContainer.addView(card);
        }
    }

    /**
     * Formata a data (YYYY-MM-DD) e o horário do encontro para exibição,
     * ex.: "01/03 · 19:00 - 21:00" ou, quando não há horário de término
     * cadastrado (endTime nulo), apenas "01/03 · 19:00".
     */
    private String formatarDataHora(AgendaItem item) {
        String dataFormatada = item.activityDate;
        if (item.activityDate != null) {
            String[] partesData = item.activityDate.split("-");
            dataFormatada = partesData.length == 3
                    ? partesData[2] + "/" + partesData[1]
                    : item.activityDate;
        }

        String horaInicio = formatarHora(item.startTime);
        String horaFim = formatarHora(item.endTime);

        if (horaFim == null) {
            return dataFormatada + " · " + horaInicio;
        }
        return dataFormatada + " · " + horaInicio + " - " + horaFim;
    }

    private String formatarHora(String hora) {
        if (hora == null) {
            return null;
        }
        return hora.length() >= 5 ? hora.substring(0, 5) : hora;
    }
}
