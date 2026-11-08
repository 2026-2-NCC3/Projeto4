package com.example.myapplication;

import android.Manifest;
import android.app.DownloadManager;
import android.content.*;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.widget.*;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.*;
import com.example.myapplication.core.NextMeetingPolicy;
import com.example.myapplication.core.QrPayload;
import com.example.myapplication.data.AlunoRepository;
import com.example.myapplication.network.*;
import com.example.myapplication.sync.SyncScheduler;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.*;
import com.journeyapps.barcodescanner.*;
import java.text.DateFormat;
import java.util.*;

/** Student flows share an accessible shell; all domain values come from the authenticated API. */
public class HubActivity extends AppCompatActivity implements ResourceListFragment.AdapterHost {
  @Override
  public RecyclerView.Adapter<?> resourceAdapter() {
    return adapter;
  }

  private String owner, section = "inicio";
  private int generation;
  private boolean mutating;
  private TextView title, status;
  private Button primary, send;
  private EditText message;
  private final Rows adapter = new Rows();
  private JsonElement shown;
  private final Map<String, String> answers = new HashMap<>();
  private ActivityResultLauncher<ScanOptions> scanner;
  private ActivityResultLauncher<String> notificationPermission;

  protected String initialSection() {
    return "inicio";
  }

  @Override
  protected void onCreate(Bundle saved) {
    super.onCreate(saved);
    SessionManager.Session session = SessionManager.get(this);
    if (session == null) {
      login();
      return;
    }
    owner = session.userId;
    setContentView(R.layout.activity_hub);
    View root = findViewById(R.id.screenRoot);
    int horizontal = root.getPaddingLeft();
    ViewCompat.setOnApplyWindowInsetsListener(
        root,
        (v, insets) -> {
          androidx.core.graphics.Insets s =
              insets.getInsets(
                  WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
          v.setPadding(horizontal + s.left, s.top, horizontal + s.right, s.bottom);
          return insets;
        });
    title = findViewById(R.id.screenTitle);
    status = findViewById(R.id.screenStatus);
    primary = findViewById(R.id.primaryAction);
    message = findViewById(R.id.messageInput);
    send = findViewById(R.id.sendMessage);
    findViewById(R.id.navInicio).setOnClickListener(v -> show("inicio"));
    findViewById(R.id.navCursos).setOnClickListener(v -> show("cursos"));
    findViewById(R.id.navAgenda).setOnClickListener(v -> show("agenda"));
    findViewById(R.id.navPerfil).setOnClickListener(v -> show("perfil"));
    findViewById(R.id.refreshButton)
        .setOnClickListener(v -> {
          SyncScheduler.sync(this, owner);
          load();
        });
    send.setOnClickListener(v -> sendMessage());
    scanner =
        registerForActivityResult(
            new ScanContract(),
            result -> {
              if (result.getContents() != null) queueQr(result.getContents());
              else toast("Leitura cancelada. Você pode tentar novamente.");
            });
    notificationPermission =
        registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            granted -> {
              toast(
                  granted
                      ? "Avisos ativados. A atualização ocorre periodicamente."
                      : "Avisos do sistema desativados. Consulte a central no app.");
              if (granted) SyncScheduler.sync(this, owner);
            });
    getOnBackPressedDispatcher()
        .addCallback(
            this,
            new OnBackPressedCallback(true) {
              @Override
              public void handleOnBackPressed() {
                if (!"inicio".equals(section)) show("inicio");
                else {
                  setEnabled(false);
                  getOnBackPressedDispatcher().onBackPressed();
                }
              }
            });
    if (saved != null && saved.containsKey("answers")) {
      try {
        JsonObject values = JsonParser.parseString(saved.getString("answers")).getAsJsonObject();
        for (String k : values.keySet()) answers.put(k, values.get(k).getAsString());
      } catch (RuntimeException ignored) {
        answers.clear();
      }
    }
    SyncScheduler.start(this, owner);
    show(
        saved != null
            ? saved.getString("section", "inicio")
            : getIntent().getStringExtra("section") != null
                ? getIntent().getStringExtra("section")
                : initialSection());
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (owner != null && !SessionManager.isCurrent(this, owner)) login();
  }

  @Override
  protected void onSaveInstanceState(Bundle out) {
    super.onSaveInstanceState(out);
    out.putString("section", section);
    out.putString("answers", new Gson().toJson(answers));
  }

  private void login() {
    startActivity(
        new Intent(this, LoginActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    finish();
  }

  private boolean current(int request) {
    return !isFinishing()
        && !isDestroyed()
        && request == generation
        && SessionManager.isCurrent(this, owner);
  }

  private void show(String target) {
    section = target;
    int[] nav = {R.id.navInicio, R.id.navCursos, R.id.navAgenda, R.id.navPerfil};
    int[] icons = {
      R.drawable.home_unselected,
      R.drawable.courses_unselected,
      R.drawable.calendar_unselected,
      R.drawable.profile_unselected
    };
    String[] keys = {"inicio", "cursos", "agenda", "perfil"};
    for (int i = 0; i < nav.length; i++) {
      Button button = findViewById(nav[i]);
      boolean selected =
          keys[i].equals(target) || (i == 3 && !Arrays.asList(keys).contains(target));
      button.setTypeface(
          null, selected ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
      button.setSelected(selected);
      android.graphics.drawable.Drawable icon =
          androidx.core.content.ContextCompat.getDrawable(this, icons[i]);
      if (icon != null) {
        int size = (int) (22 * getResources().getDisplayMetrics().density);
        icon.setBounds(0, 0, size, size);
        button.setCompoundDrawables(null, icon, null, null);
      }
      button.setBackgroundTintList(
          android.content.res.ColorStateList.valueOf(
              android.graphics.Color.parseColor(selected ? "#DCEFEB" : "#FBFBFB")));
    }
    primary.setEnabled(!mutating);
    send.setEnabled(!mutating);
    shown = null;
    adapter.set(new ArrayList<>());
    title.setText(label(target));
    if ("perfil".equals(target) || "inicio".equals(target)) {
      JsonObject shell = new JsonObject();
      shell.addProperty("summaryUnavailable", true);
      render(shell, Collections.emptyList());
    }
    message.setVisibility("mensagens".equals(target) ? View.VISIBLE : View.GONE);
    send.setVisibility(message.getVisibility());
    primary.setVisibility(View.GONE);
    if ("agenda".equals(target) || "presencas".equals(target)) {
      primary.setVisibility(View.VISIBLE);
      primary.setText("Ler QR de presença");
      primary.setOnClickListener(v ->
          scanner.launch(
              new ScanOptions()
                  .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                  .setPrompt("Aponte para o QR do encontro")
                  .setBeepEnabled(false)
                  .setOrientationLocked(false)));
    }
    if ("teste-perfil".equals(target)) {
      primary.setVisibility(View.VISIBLE);
      primary.setText("Enviar minhas respostas");
      primary.setOnClickListener(v -> submitQuiz());
    }
    load();
  }

  private String path() {
    return "api/" + ("resultados".equals(section) ? "teste-perfil/resultados" : section);
  }

  private void load() {
    if (owner == null) return;
    final int request = ++generation;
    final String resource = section, path = path();
    status.setText("Carregando…");
    AlunoApplication.IO.execute(() -> {
      try (AlunoRepository db = new AlunoRepository(this)) {
        AlunoRepository.Cached cache = db.cache(owner, resource);
        List<AlunoRepository.Pending> pending =
            "presencas".equals(resource) ? db.checkins(owner, false) : Collections.emptyList();
        if (cache != null) {
          try {
            JsonElement data = JsonParser.parseString(cache.payload);
            runOnUiThread(() -> {
              if (current(request)) {
                if (render(data, pending)) status.setText("Dados salvos • atualizando…");
              }
            });
          } catch (RuntimeException ignored) {
            /* Reload corrupt cache from API. */
          }
        } else if ("presencas".equals(resource))
          runOnUiThread(() -> {
            if (current(request)) render(new JsonArray(), pending);
          });
        try {
          JsonElement data = ApiClient.request(this, owner, "GET", path, null);
          if ("inicio".equals(resource) && data.isJsonObject()) {
            try {
              data.getAsJsonObject()
                  .add("nextMeetings", ApiClient.request(this, owner, "GET", "api/agenda", null));
              data.getAsJsonObject()
                  .add(
                      "announcements",
                      ApiClient.request(this, owner, "GET", "api/comunicados", null));
            } catch (Exception optionalFailure) {
              data.getAsJsonObject().addProperty("summaryUnavailable", true);
            }
          }
          db.saveCache(owner, resource, data.toString());
          List<AlunoRepository.Pending> latest =
              "presencas".equals(resource) ? db.checkins(owner, false) : Collections.emptyList();
          runOnUiThread(() -> {
            if (current(request)) {
              if (render(data, latest))
                status.setText(
                    adapter.rows.isEmpty() ? emptyMessage(resource) : "Atualizado agora");
            }
          });
        } catch (Exception e) {
          runOnUiThread(() -> {
            if (!current(request)) {
              if (SessionManager.get(this) == null && !isFinishing()) login();
              return;
            }
            status.setText(
                error(e)
                    + (cache == null
                        ? " Toque em Atualizar para tentar de novo."
                        : " Exibindo dados salvos em "
                            + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                                .format(new Date(cache.updatedAt))
                            + "."));
          });
        }
      } catch (Exception e) {
        runOnUiThread(() -> {
          if (current(request))
            status.setText("Não foi possível ler os dados locais. Tente atualizar.");
        });
      }
    });
  }

  private String error(Exception e) {
    return e instanceof ApiClient.ApiException ? e.getMessage() : "Sem conexão com o serviço.";
  }

  private String emptyMessage(String resource) {
    return "agenda".equals(resource)
        ? "Nenhum encontro agendado. Inscreva-se em um curso."
        : "certificados".equals(resource)
            ? "Seus certificados aparecerão após concluir os cursos."
            : "presencas".equals(resource)
                ? "Nenhuma presença ainda. Leia o QR de um encontro."
                : "Nenhum registro por enquanto.";
  }

  private boolean render(JsonElement data, List<AlunoRepository.Pending> pending) {
    shown = data;
    List<Row> rows = new ArrayList<>();
    try {
      if ("inicio".equals(section)) {
        JsonObject o = data.getAsJsonObject();
        rows.add(
            new Row(
                s(o, "nome").isEmpty() ? "Boas-vindas" : "Olá, " + s(o, "nome"),
                s(o, "curso"),
                "Explorar cursos",
                () -> show("cursos")));
        JsonObject next =
            o.has("nextMeetings")
                ? NextMeetingPolicy.next(
                    o.getAsJsonArray("nextMeetings"), System.currentTimeMillis())
                : null;
        if (next != null) {
          menu(
              rows,
              "Próximo encontro: " + s(next, "title"),
              join(s(next, "activity_date") + " • " + s(next, "start_time"), s(next, "location")),
              "agenda");
        } else
          menu(
              rows,
              "Seus próximos encontros",
              o.has("summaryUnavailable")
                  ? "Resumo indisponível. Abra sua agenda para tentar novamente."
                  : "Nenhum encontro no momento. Explore os cursos disponíveis.",
              "agenda");
        if (o.has("announcements") && o.getAsJsonArray("announcements").size() > 0) {
          JsonObject latest = o.getAsJsonArray("announcements").get(0).getAsJsonObject();
          menu(rows, s(latest, "titulo"), s(latest, "conteudo"), "comunicados");
        }
        menu(rows, "Comunicados", "Novidades da equipe Próxima Etapa.", "comunicados");
        menu(rows, "Central de avisos", "Leia as atualizações da sua jornada.", "notificacoes");
      } else if ("perfil".equals(section)) {
        JsonObject o = data.getAsJsonObject();
        rows.add(
            new Row(
                s(o, "nome").isEmpty() ? "Sua conta" : s(o, "nome"), s(o, "escola"), null, null));
        menu(rows, "Minha carteirinha", "Seus dados de identificação.", "card");
        menu(rows, "Histórico de presenças", "Confirmadas, pendentes e recusadas.", "presencas");
        menu(rows, "Certificados", "Abra ou salve os certificados disponíveis.", "certificados");
        menu(
            rows,
            "Meu perfil profissional",
            "Responda às perguntas de interesses.",
            "teste-perfil");
        menu(rows, "Resultados do perfil", "Consulte suas respostas anteriores.", "resultados");
        menu(rows, "Fale com a equipe", "Envie uma dúvida e acompanhe a resposta.", "mensagens");
        menu(rows, "Comunicados", "Informações publicadas pela equipe.", "comunicados");
        menu(rows, "Notificações", "Central de avisos do aluno.", "notificacoes");
        rows.add(
            new Row(
                "Avisos no celular",
                "Atualizações periódicas, a partir de 15 minutos; dependem de conexão e das"
                    + " restrições do Android.",
                "Ativar avisos",
                this::enableNotifications));
        rows.add(
            new Row(
                "Sua conta",
                "Seus registros ficam separados por conta neste aparelho.",
                "Sair",
                () ->
                    new MaterialAlertDialogBuilder(this)
                        .setTitle("Sair da conta?")
                        .setMessage(
                            "Presenças pendentes serão sincronizadas quando você entrar novamente"
                                + " nesta conta.")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Sair", (d, w) -> logout())
                        .show()));
      } else if ("card".equals(section)) {
        JsonObject o = data.getAsJsonObject();
        rows.add(
            new Row(
                s(o, "nome"),
                join(
                    s(o, "escola"),
                    "Número: " + s(o, "numero"),
                    "Carteirinha de demonstração acadêmica. Não substitui documento oficial."),
                null,
                null));
      } else if ("teste-perfil".equals(section)) {
        JsonObject o = data.getAsJsonObject();
        rows.add(
            new Row(
                s(o, "titulo"),
                join(
                    s(o, "descricao"),
                    answers.size()
                        + " de "
                        + o.getAsJsonArray("perguntas").size()
                        + " perguntas respondidas",
                    "Questionário orientativo de interesses; não é diagnóstico psicológico."),
                null,
                null));
        for (JsonElement e : o.getAsJsonArray("perguntas")) {
          JsonObject q = e.getAsJsonObject();
          String id = s(q, "id");
          String selected = "Escolha uma alternativa";
          for (JsonElement a : q.getAsJsonArray("alternativas"))
            if (s(a.getAsJsonObject(), "id").equals(answers.get(id)))
              selected = s(a.getAsJsonObject(), "texto");
          rows.add(new Row(s(q, "enunciado"), selected, "Responder", () -> choose(q)));
        }
      } else {
        Set<String> historyIds = new HashSet<>();
        for (JsonElement element : data.getAsJsonArray()) {
          JsonObject o = element.getAsJsonObject();
          String id = s(o, "id");
          switch (section) {
            case "cursos":
              String state = s(o, "status");
              rows.add(
                  new Row(
                      s(o, "title"),
                      join(
                          s(o, "description"),
                          s(o, "location"),
                          "Carga: "
                              + s(o, "workload")
                              + "h • "
                              + s(o, "meetings_count")
                              + " encontros",
                          s(o, "course_date"),
                          "Situação: " + state),
                      "Disponível".equals(state) ? "Inscrever-me" : "Ver agenda",
                      () -> {
                        if ("Disponível".equals(state))
                          new MaterialAlertDialogBuilder(this)
                              .setTitle("Inscrever-se?")
                              .setMessage(s(o, "title"))
                              .setNegativeButton("Cancelar", null)
                              .setPositiveButton(
                                  "Confirmar",
                                  (d, w) ->
                                      mutate(
                                          "POST",
                                          "api/cursos/" + id + "/inscricao",
                                          new JsonObject(),
                                          "Inscrição realizada.",
                                          null))
                              .show();
                        else show("agenda");
                      }));
              break;
            case "agenda":
              rows.add(
                  new Row(
                      s(o, "title"),
                      join(
                          s(o, "course_title"),
                          s(o, "activity_date")
                              + " • "
                              + s(o, "start_time")
                              + "–"
                              + s(o, "end_time"),
                          s(o, "location"),
                          bool(o, "attended")
                              ? "Presença confirmada"
                              : "Presença ainda não registrada"),
                      "Histórico",
                      () -> show("presencas")));
              break;
            case "presencas":
              historyIds.add(s(o, "activity_id"));
              rows.add(
                  new Row(
                      s(o, "title"),
                      join(s(o, "course_title"), "Confirmada • " + s(o, "scanned_at")),
                      null,
                      null));
              break;
            case "certificados":
              rows.add(
                  new Row(
                      s(o, "title"),
                      join("Carga horária: " + s(o, "workload") + "h", s(o, "issued_at")),
                      "Abrir certificado",
                      () -> certificate(s(o, "certificate_url"))));
              break;
            case "resultados":
              rows.add(
                  new Row(
                      s(o, "classificacao"), join(s(o, "resumo"), s(o, "created_at")), null, null));
              break;
            case "comunicados":
              rows.add(
                  new Row(s(o, "titulo"), join(s(o, "conteudo"), s(o, "created_at")), null, null));
              break;
            case "mensagens":
              rows.add(
                  new Row(
                      "equipe".equals(s(o, "remetente")) ? "Equipe Próxima Etapa" : "Você",
                      join(s(o, "conteudo"), s(o, "created_at")),
                      null,
                      null));
              break;
            case "notificacoes":
              rows.add(
                  new Row(
                      s(o, "titulo") + (bool(o, "lida") ? "" : " • Nova"),
                      join(s(o, "conteudo"), s(o, "created_at")),
                      bool(o, "lida") ? null : "Marcar como lida",
                      () ->
                          mutate(
                              "PATCH",
                              "api/notificacoes/" + id + "/lida",
                              new JsonObject(),
                              "Aviso marcado como lido.",
                              null)));
              break;
            default:
              break;
          }
        }
        if ("presencas".equals(section))
          for (AlunoRepository.Pending p : pending)
            if (!historyIds.contains(p.activityId)) {
              String state =
                  "confirmed".equals(p.state)
                      ? "Confirmada"
                      : "rejected".equals(p.state) ? "Recusada" : "Pendente de sincronização";
              rows.add(
                  new Row(
                      state,
                      join(
                          "Leitura de presença",
                          p.message,
                          DateFormat.getDateTimeInstance().format(new Date(p.createdAt))),
                      "pending".equals(p.state) ? "Sincronizar" : null,
                      () -> {
                        SyncScheduler.sync(this, owner);
                        status.setText(
                            "Sincronização solicitada. Toque em Atualizar em alguns instantes.");
                      }));
            }
      }
      adapter.set(rows);
      return true;
    } catch (RuntimeException e) {
      status.setText("O serviço retornou dados incompletos. Tente atualizar.");
      return false;
    }
  }

  private void menu(List<Row> rows, String name, String detail, String target) {
    rows.add(new Row(name, detail, "Abrir", () -> show(target)));
  }

  private static String s(JsonObject o, String k) {
    JsonElement v = o.get(k);
    return v == null || v.isJsonNull() ? "" : v.getAsString();
  }

  private static boolean bool(JsonObject o, String k) {
    return o.has(k) && !o.get(k).isJsonNull() && o.get(k).getAsBoolean();
  }

  private static String join(String... lines) {
    StringBuilder b = new StringBuilder();
    for (String l : lines)
      if (l != null && !l.isEmpty()) {
        if (b.length() > 0) b.append('\n');
        b.append(l);
      }
    return b.toString();
  }

  private void mutate(String method, String path, JsonObject body, String success, Runnable after) {
    if (mutating) return;
    mutating = true;
    final int request = generation;
    status.setText("Enviando…");
    primary.setEnabled(false);
    send.setEnabled(false);
    AlunoApplication.IO.execute(() -> {
      try {
        ApiClient.request(this, owner, method, path, body);
        runOnUiThread(() -> {
          mutating = false;
          if (isFinishing() || isDestroyed()) return;
          primary.setEnabled(true);
          send.setEnabled(true);
          if (!current(request)) return;
          toast(success);
          if (after != null) after.run();
          if (request == generation) load();
        });
      } catch (Exception e) {
        runOnUiThread(() -> {
          mutating = false;
          if (isFinishing() || isDestroyed()) return;
          primary.setEnabled(true);
          send.setEnabled(true);
          if (!current(request)) {
            if (SessionManager.get(this) == null) login();
            return;
          }
          status.setText(error(e));
        });
      }
    });
  }

  private void sendMessage() {
    String text = message.getText().toString().trim();
    if (text.isEmpty()) {
      message.setError("Escreva sua dúvida");
      return;
    }
    if (text.length() > 2000) {
      message.setError("Use até 2.000 caracteres");
      return;
    }
    JsonObject b = new JsonObject();
    b.addProperty("conteudo", text);
    mutate("POST", "api/mensagens", b, "Mensagem enviada.", () -> message.setText(""));
  }

  private void choose(JsonObject q) {
    JsonArray options = q.getAsJsonArray("alternativas");
    String[] labels = new String[options.size()];
    int checked = -1;
    for (int i = 0; i < labels.length; i++) {
      JsonObject a = options.get(i).getAsJsonObject();
      labels[i] = s(a, "texto");
      if (s(a, "id").equals(answers.get(s(q, "id")))) checked = i;
    }
    new MaterialAlertDialogBuilder(this)
        .setTitle(s(q, "enunciado"))
        .setSingleChoiceItems(
            labels,
            checked,
            (d, w) -> {
              answers.put(s(q, "id"), s(options.get(w).getAsJsonObject(), "id"));
              d.dismiss();
              render(shown, Collections.emptyList());
            })
        .setNegativeButton("Cancelar", null)
        .show();
  }

  private void submitQuiz() {
    if (shown == null || !shown.isJsonObject()) return;
    JsonArray submitted = new JsonArray();
    for (JsonElement e : shown.getAsJsonObject().getAsJsonArray("perguntas")) {
      String id = s(e.getAsJsonObject(), "id");
      if (!answers.containsKey(id)) {
        toast("Responda todas as perguntas antes de enviar.");
        return;
      }
      JsonObject a = new JsonObject();
      a.addProperty("questionId", id);
      a.addProperty("alternativeId", answers.get(id));
      submitted.add(a);
    }
    JsonObject b = new JsonObject();
    b.add("answers", submitted);
    mutate(
        "POST",
        "api/teste-perfil/resultados",
        b,
        "Resultado registrado.",
        () -> {
          answers.clear();
          show("resultados");
        });
  }

  private void queueQr(String raw) {
    final QrPayload qr;
    try {
      qr = QrPayload.parse(raw);
    } catch (IllegalArgumentException e) {
      toast(e.getMessage());
      return;
    }
    AlunoApplication.IO.execute(() -> {
      try (AlunoRepository db = new AlunoRepository(this)) {
        if (!SessionManager.isCurrent(this, owner)) return;
        db.enqueue(owner, qr);
        SyncScheduler.sync(this, owner);
        runOnUiThread(() -> {
          if (!isFinishing() && SessionManager.isCurrent(this, owner)) {
            show("presencas");
            toast("Leitura salva. A presença será validada pelo servidor.");
          }
        });
      } catch (Exception e) {
        runOnUiThread(() -> toast("Não foi possível salvar a leitura. Tente novamente."));
      }
    });
  }

  private void certificate(String value) {
    Uri uri = Uri.parse(value);
    if (!"https".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null) {
      toast("Certificado sem endereço HTTPS válido.");
      return;
    }
    new MaterialAlertDialogBuilder(this)
        .setTitle("Certificado")
        .setItems(
            new String[] {"Abrir no navegador", "Baixar PDF"},
            (d, w) -> {
              try {
                if (w == 0) startActivity(new Intent(Intent.ACTION_VIEW, uri));
                else {
                  DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                  DownloadManager.Request r =
                      new DownloadManager.Request(uri)
                          .setTitle("Certificado Próxima Etapa")
                          .setNotificationVisibility(
                              DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                          .setDestinationInExternalFilesDir(
                              this,
                              Environment.DIRECTORY_DOWNLOADS,
                              "certificado-" + System.currentTimeMillis() + ".pdf");
                  manager.enqueue(r);
                  toast("Download iniciado. Acompanhe nas notificações do Android.");
                }
              } catch (RuntimeException e) {
                toast("Não foi possível abrir o certificado neste aparelho.");
              }
            })
        .show();
  }

  private void enableNotifications() {
    if (Build.VERSION.SDK_INT >= 33)
      notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
    else {
      SyncScheduler.sync(this, owner);
      toast("Consulte as permissões de notificações do aplicativo nas configurações do Android.");
    }
  }

  private void logout() {
    SyncScheduler.stop(this);
    SessionManager.clearToken(this);
    ((android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
    login();
  }

  private void toast(String text) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
  }

  private static String label(String s) {
    switch (s) {
      case "inicio":
        return "Sua próxima etapa";
      case "cursos":
        return "Cursos";
      case "agenda":
        return "Agenda";
      case "perfil":
        return "Meu perfil";
      case "presencas":
        return "Presenças";
      case "card":
        return "Carteirinha";
      case "certificados":
        return "Certificados";
      case "teste-perfil":
        return "Meu perfil profissional";
      case "resultados":
        return "Meus resultados";
      case "comunicados":
        return "Comunicados";
      case "mensagens":
        return "Fale com a equipe";
      case "notificacoes":
        return "Notificações";
      default:
        return "Próxima Etapa";
    }
  }

  private static final class Row {
    final String title, detail, action;
    final Runnable click;

    Row(String t, String d, String a, Runnable c) {
      title = t;
      detail = d;
      action = a;
      click = c;
    }
  }

  private static final class Holder extends RecyclerView.ViewHolder {
    final TextView title, detail;
    final Button action;

    Holder(View v) {
      super(v);
      title = v.findViewById(R.id.rowTitle);
      detail = v.findViewById(R.id.rowDetail);
      action = v.findViewById(R.id.rowAction);
    }
  }

  private static final class Rows extends RecyclerView.Adapter<Holder> {
    List<Row> rows = new ArrayList<>();

    void set(List<Row> values) {
      rows = values;
      notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup p, int t) {
      return new Holder(
          LayoutInflater.from(p.getContext()).inflate(R.layout.item_resource, p, false));
    }

    @Override
    public void onBindViewHolder(Holder h, int p) {
      Row r = rows.get(p);
      h.title.setText(r.title);
      h.detail.setText(r.detail);
      h.action.setVisibility(r.action == null ? View.GONE : View.VISIBLE);
      h.action.setText(r.action);
      h.action.setOnClickListener(v -> {
        if (r.click != null) r.click.run();
      });
    }

    @Override
    public int getItemCount() {
      return rows.size();
    }
  }
}
