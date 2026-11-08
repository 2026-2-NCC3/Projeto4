# Expansão backend E2 autorizada em 08/11/2026

tdd-scope: required
ui-scope: not-applicable

Escopo: APIs necessárias ao Android e painel, migração SQL preparada localmente, sem apply remoto.
Contrato HTTP: src/Entrega2/Backend/docs/http-contract.md. Branch Luiz-entrega2 não renomear.

## Schema observado
PostgreSQL/Supabase versão exata remota pendente. Principal confirmou 17 tabelas, RLS ativa.
profiles(id,full_name,school), courses(id,title,description,location,course_date,workload,meetings_count,...),
activities(id,course_id,title,activity_date), enrollments(id,user_id,course_id,status,...),
attendances(id,user_id,activity_id,scanned_at), certificates(id,user_id,course_id,certificate_url,issued_at),
user_roles(id,user_id,role app_role). Evitar depender de test_results não confirmado.

## Decisões
- Fonte de autorização admin: user_roles protegido, role::text='admin'. e2_is_admin SECURITY DEFINER
  com search_path fixo e auth.uid(), sem parâmetros arbitrários de identidade.
- QR: 32 bytes aleatórios gerados Node crypto, hash SHA256 persistido; token nunca fica listável.
- Check-in: RPC transacional auth.uid(), valida QR/janela/inscrição, unique(user_id,activity_id),
  ON CONFLICT DO NOTHING + leitura própria. Remover INSERT direto authenticated/anon de attendances
  para fechar bypass QR; isso restringe escrita, sem perder compatibilidade de leitura da E1.
- Tabelas e2_* para QR, quiz, resultados, comunicados, notificações, leituras e mensagens.
- Quiz acadêmico sem IA, alternativas/pontuação no banco; RPC calcula resultado, aluno não escreve score.
- Notificações persistidas globais/de usuário + recibos próprios; trigger de curso/certificado/comunicado.
- Mensagens assíncronas aluno/equipe; suporte não promete chat em tempo real.
- Express permanece monólito; createApp com injeção controlada de clientes facilita HTTP tests sem rede.
- Migração additiva dentro BEGIN com lock_timeout 5s/statement_timeout 30s e preflight relações/duplicidades.
  Políticas novas com prefixo e2; rollback operacional prioriza deployment E2 conhecido, mantendo dados. E1 perde escrita direta de inscrição; consultar migration-runbook.md.

## Riscos e testes
Negativos: anon, aluno tentando admin, acesso cruzado, QR inválido/expirado, repetição e respostas falsas.
Banco embedded PGlite dev-only para validar SQL com fixture mínima de schema confirmado; não equivale
ao Supabase implantado nem prova concorrência multiconexão. Docker local indisponível.
Gate apply permanece bloqueado até preflight real, backup e aprovação. Não publicar a migração automaticamente.
