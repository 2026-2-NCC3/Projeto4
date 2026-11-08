# Situação da Entrega 2 - 08/11/2026

Implementação local concluída na branch `Luiz-entrega2`. **Integração hospedada e
aceite completo pendentes.** Reparo de duplicidades e migração Supabase aplicados em08/11,
após confirmação do Luiz, pelo Chrome pessoal. Publicação Vercel e concessão de admin
continuam pendentes; Luiz validará a branch e fará o merge. Prazo:09/11 às08h.

## Entregue localmente

- Android Java/XML com ConstraintLayout e Fragment real, autenticação/refresh,
  cursos, inscrição, agenda, QR, SQLite por usuário, sincronização, histórico,
  card, certificados, teste de interesses, comunicados, mensagens e notificações periódicas.
- API autenticada e SQL transacional com RLS, controle de vagas/duplicidade e QR por atividade.
- Painel administrativo `/admin/` no mesmo projeto Express/Vercel.
- Contrato HTTP, preflight e runbook, demonstração sintética, roteiro de testes.
- [APK debug HTTPS](<../../documentos/Entrega 2/Disciplina 3/proxima-etapa-entrega2.apk>),
  UML PDF/drawio, guia POO, conferência estatística e boxplots reproduzíveis.

## Verificações executadas

| Verificação | Resultado |
|---|---|
| Backend HTTP e SQL/PGlite, incluindo reparo |26/26 PASS; revisão independente do SQL PASS|
| Supabase remoto, conferência após aplicação |6 inscrições únicas,7 cópias arquivadas,0 duplicatas;13 registros preservados exatamente contra backup;7 tabelas E2 com RLS,9 funções|
| Painel núcleo sessão/QR |9/9 PASS|
| Chrome real, login/permissão/CRUD/QR/comunicação/logout |PASS em fixture local|
| Responsividade painel390px |Sem overflow observado, screenshots salvos|
| Android JVM, execução independente forçada |16/16 PASS (15 comportamentais+smoke)|
| Android instrumentação Pixel_10a |6/6 PASS (3SQLite+2Fragment+smoke)|
| Android build APK |PASS|
| Android lint |0 erros,89 avisos; não suprimidos|
| Código Entrega1 |Sem alterações|
| Padrões de segredos locais |Nenhuma ocorrência nos padrões examinados; Gitleaks ausente|
| Doctor/global product-final-eval strict |FAIL/BLOCKED; sem alegação ready-for-review|

[Revisão backend](../../.codex/spec-loop/entrega2-completa/backend-review.md),
[revisão Android](../../.codex/spec-loop/entrega2-completa/android-review.md),
[teste do painel](../../.codex/spec-loop/entrega2-completa/browser-verification.md),
[contexto do gate global](../../.codex/spec-loop/entrega2-completa/readiness-context.md).

## Pendências para a demonstração integrada

1. Banco atualizado: `000a_repair_enrollments.sql` e `001_entrega2.sql` aplicados no
   projeto `hejcktjrlursifgcjdon`. Não reaplicar para testar. Conferência descrita no runbook.
2. Definir uma conta administrativa e autorizar concessão do papel. Não há admin
   automático, cadastro público de equipe ou chave privilegiada no aplicativo.
3. Autorizar envio da branch e o responsável publicar na Vercel com Root Directory
   `src/Entrega2/Backend`, mantendo as variáveis Supabase corretas.
4. Testar com aluno/admin fictícios na URL hospedada: login, inscrição, QR repetido,
   perda de internet, histórico, mensagens e abertura de certificado real autorizado.
5. Testar câmera física, recusa de permissão, rotação/fonte ampliada e legibilidade nativa.

**Atenção à compatibilidade:** a migração restringe escrita direta em inscrições/presenças.
Retornar somente ao backend E1 não restaura essas operações. O runbook explica a janela
e o rollback; a mudança não deve ser aplicada isolada da publicação E2.

O acesso ao Supabase pelo Chrome pessoal foi restabelecido e usado no preflight,
backup e aplicação. A indisponibilidade de inscrições E1 até a publicação E2 foi
informada e aceita pelo Luiz antes do apply. Controle nativo do Android Studio
estava indisponível na validação anterior; não foi contornado.

## Materiais acadêmicos

Luiz preencherá o documento COM EMPRESA depois. O relatório de Dados recebido conserva
identificação editorial incompleta e uma frase a corrigir, detalhadas no README da disciplina.
Banner final e aprovação não foram fornecidos; roteiro de apresentação está no guia POO.
O PDF original da equipe foi preservado ao lado do UML novo. Nenhum resultado social,
atividade com ONG ou assinatura foi inventado.

## Limites técnicos conhecidos

Fixture local não replica Auth/PostgREST/CDN hospedados. Concorrência multiconexão SQL,
refresh/worker com servidor real, push instantâneo e paginação não foram validados ou
implementados além do contrato descrito. WorkManager depende das restrições do Android;
QR offline pode expirar antes da sincronização. Certificados são documentos HTTPS
previamente emitidos, não uma emissão automática baseada em presença.
