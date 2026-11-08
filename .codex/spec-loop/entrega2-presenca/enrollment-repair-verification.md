# Evidência do reparo de inscrições

Registro:08/11/2026. Escopo local, fixtures sintéticas. Sem alterações remotas pelo autor.

## RED/GREEN observado

- RED: `node --test test/enrollment-repair.test.js`, exit1, toolchunk6798e2.
  Sem SQL implementado, primeiro teste observou12 inscrições em vez de5; testes de
  conflito e alteração da canônica observaram `Missing expected rejection`.
  O teste de arquivo falhou por tabela ausente. O happy path de restore passou
  antes da implementação porque o reparo também era no-op: esse caso é **test-after**,
  não prova TDD. Não se alega que todos os oito testes tenham cicloRED próprio.
- GREEN: mesmo comando após implementar SQL, exit0, toolchunke61a52,5/5.
- Regressão: `npm test` no backend, exit0, toolchunks4d2bdf/34b32f,26/26.
  Oito casos novos incluem subcenários adicionais de esquema/FK/escopo, aplicação001
  após reparo e INSERT E1. Nenhuma assertion anterior foi enfraquecida.
- Nenhuma refatoração executável foi necessária apósGREEN; documentação foi atualizada.
  Os horários individuaisRED/GREEN não foram capturados; as referências são os outputs
  reais das ferramentas, sem criar timestamps retroativos nem alegar gateTDD estritoPASS.
- Gate executado: `bash /Users/luizrs/.codex/scripts/codex-flow-tdd-evidence-gate.sh --target . --spec entrega2-presenca --strict`, exit0/PASS.
  Cobriu somente o cartãoJSON preexistente `backend-base`; não valida formalmente este reparo.

## Revisão independente

O orquestrador comunicou review `migration_review` sem bloqueadores,6/6 testes existentes
de banco e8/8 novos executados separadamente; também comunicou regressão26/26 fresca.
O relatório próprio do reviewer é a autoridade para seu parecer; autor não se aprova.

## Recuperação e implantação

O orquestrador confirmou backup independente via UI, arquivo local ignorado/permissão600:
13inscrições,21políticas,476grants de tabela,20colunas e4constraints. O autor não abriu
esse arquivo nem usou identificadores reais. Estado remoto comunicado antes doapply:
13inscrições,nenhuma tabelaE2,nenhumadmin. SQL apenas preparado; aplicação continua
dependente da confirmação exata de mudança de permissões/consolidação e janelaE1→E2.

Limitações: PGlite não cobre concorrência multiconexão/PostgREST/Auth hospedado. Schema
privado de recuperação não substitui backup independente. Restore recusa E2 instalado;
não é rollback completo de001. Merge/main/publicação ficam com usuário/equipe.

## Iteration Record

- schema_version:agent-chain.iteration/v1; run_id:entrega2-enrollment-repair; iteration:1.
- observation:quatro grupos/sete cópias impediam criação do índiceúnico.
- diagnosis:legado permitia INSERT repetido semunicidade;001para corretamente.
- hypothesis:arquivo privado verificado e consolidação limitada, com índice na mesmaTX,
  resolvem a pré-condição sem descartar os valores originais.
- action:dois SQLs versionados,testes,runbook; nenhuma operação remota.
- fresh_evidence:RED12!=5;GREEN5/5;regressão26/26;reviewindependente informado.
- evaluator_verdict:PASS local comunicado peloorquestrador; applyremoto pendente.
- error_fingerprint:REPAIR_SCOPE_CHANGED / REPAIR_CONFLICTING_ENROLLMENT protegem divergência.
- delta:improved; resolved_gaps:reparo recuperável/índice/compatibilidade testada.
- remaining_gaps:confirmação browser,apply,publicaçãoE2,teste hospedado.
- next_decision:continue; next_action:orquestrador apresentar SQL concreto e aplicar após gate.
