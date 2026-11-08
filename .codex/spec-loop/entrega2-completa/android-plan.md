# Plano Android revisto

Revisão independente brief_ui (read-only) orienta implementação sequencial:

1. Sessão: user.id, access/refresh/expiry persistidos juntos; não trim senha; refresh
   separado sem recursão, serializado. Callback não pode misturar conta anterior com atual.
2. SQLiteOpenHelper: cache por (user_id,recurso), fila por (user_id,activity_id), IDs de
   notificações vistas por usuário. Executor fora main thread. Upgrade aditivo, nunca DROP.
3. WorkManager: fila persistente imediata com rede; retentar backoff limitado; 401 pede login,
   5xx/429/rede mantém pendência, falhas definitivas rejeitam após reconciliar histórico.
4. QR: ZXing Embedded, payload versionado com activityId+qrToken validado no banco.
5. Telas: quatro abas existentes; agenda RecyclerView; card/certificados/teste/comunicados/
   dúvidas/notificações acessíveis por perfil/início. Estado erro não vira vazio.
6. Notificações: canal e POST_NOTIFICATIONS contextual, polling WorkManager mínimo15min;
   botão Atualizar para demo. Não prometer push instantâneo.
7. Converter todos LinearLayout XML próprios; preservar LinearLayoutManager. Constraints,
   texto sp, alvos48dp. Excluir sessão/banco de backup. URL configurável debug/release.

Testes: parser QR e política retry JVM, SQLite instrumentado isolamento/reabertura/dedup,
refresh/conta trocada, worker resposta perdida; câmera real fica teste conjunto com usuário.
Fontes oficiais revisadas: developer.android.com/training/data-storage/sqlite;
developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work;
github.com/journeyapps/zxing-android-embedded.
