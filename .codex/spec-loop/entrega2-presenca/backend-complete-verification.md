# Backend completo — evidência local, 08/11/2026

## Estado
Implementação local concluída pelo autor, revisão independente /root/revisao_backend em curso.
Não constitui aprovação própria, conclusão do produto completo ou permissão de apply/deploy.

## Execuções observadas
- npm test: exit0, 18/18 testes reais, sem fixture contada como teste (script test/*.test.js).
- 6 cenários PostgreSQL PGlite: autenticação, RLS/grants, QR/hash/vínculo/janela/dedup,
  inscrição/capacidade/inativo, atualização de agenda, certificado, suporte/notificações/quiz.
- 12 cenários HTTP reais com Supabase injetado fictício: autenticação/refresh/503,
  contrato de check-in, guards/admin, PNG QR, certificado, fluxo integrado/listas/CRUD,
  redirect admin e janela de mensagens.
- node --check src/app.js, src/server.js, scripts/demo-server.js: exit0.
- git diff --exit-code -- src/Entrega1: exit0 (nenhuma edição fonte E1).
- Demo local separado em scripts/demo-server.js, apenas loopback; browser QA pela principal.

## Proveniência TDD
Primeiro incremento HTTP observou rotas ausentes retornando404 antes da implementação;
SQL observou comportamento de autenticação ausente antes do guard. Casos adicionais SQL
foram escritos durante implementação; faltas de função/import não contam como RED válido.
Expansão final de integração/listas/SQL é test-after, sem alegar cobertura TDD integral.
Correções pequenas com RED comportamental observado e GREEN posterior:
- redirect /admin/ retornava302 (loop), esperado200; regex exata resolveu.
- indisponibilidade Auth retornava401, esperado503; classificador corrigido.
- mensagens 501: primeira retornada era0, esperado1; DESC+limit500+reverse corrigiu.
- certificado rota ausente404 antes do guard400 (RED de contrato HTTP).
Refactor: serviço puro inicial sem uso removido; testes finais passam, sem alteração
intencional de comportamento além dos gaps acima. Nenhuma assertion foi enfraquecida.

## Limites materiais
Fixture SQL contém subconjunto conhecido, não export real. PGlite não comprova corrida
multiconexão, Auth/PostgREST/Supabase hospedado ou rollback real. Banco remoto intacto.
Preflight de defaults/constraints, backup, admin e apply exigem revisão humana.
Migração restringe escrita E1; contrato/API Android E2 devem ser publicados juntos.
Demonstração em memória não é persistência real. Não há envio push externo nem paginação.
Lint não configurado. Doctor global inicialmente FAIL por contratos/runtime; não corrigido
ou ocultado nesta entrega. Evidence da primeira fatia foi marcada histórica e não é
contabilizada como cobertura atual.

## Iteration record
- schema_version: agent-chain.iteration/v1
- run_id: entrega2-backend-completa
- iteration: 2
- observation: E1 não tinha check-in seguro, APIs complementares nem módulo administrativo.
- diagnosis: SQL e contratos autenticados ausentes, dados extras sem storage.
- hypothesis: Express existente + RPCs transacionais e RLS atendem a entrega com arquitetura simples.
- action: contrato v1, app factory, migration aditiva preparada, testes SQL/HTTP e demo isolada.
- fresh_evidence: npm test18/18; sintaxe exit0; E1 diff exit0; casos RED descritos acima.
- evaluator_verdict: INCONCLUSIVE (revisor independente em curso, integração remota pendente).
- error_fingerprint: messages-window-oldest500; admin-trailing-slash-loop; auth-upstream401.
- delta: improved
- resolved_gaps: rotas, autenticação, autorização, presença e inscrição atômicas, contratos.
- remaining_gaps: preflight/apply, deploy, teste físico, avaliação independente final.
- next_decision: continue
- next_action: revisor independente examinar código e executar evidência fresca.
