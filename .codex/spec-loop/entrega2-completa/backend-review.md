# Revisão independente do backend E2

Data: 08/11/2026. Revisor: `project-reviewer` (`revisao_backend`).
Escopo: `src/Entrega2/Backend/src`, SQL, contratos, execução local e configuração Vercel.
`public/admin` e Android ficam fora desta revisão. Nenhuma escrita remota ou leitura de dados reais foi executada.

## Findings

Nenhum finding de código permanece aberto após a rodada de reparo.

1. **P2 corrigido — mensagens novas desapareciam depois de 500 registros.** `src/Entrega2/Backend/src/app.js:157` passou a selecionar em ordem decrescente, limitar e inverter a janela para apresentação cronológica. Regressão em `test/http.test.js:33` exercita 501 mensagens e confirma inclusão da mais recente. **Correção e GREEN reverificados independentemente.**
2. **P2 corrigido — instruções de entrega descreviam código removido.** README e `docs/check-in-contract.md` foram reescritos para a implementação RPC atual; incompatibilidade de escrita E1 e rollback condicionado aparecem no README/runbook. **Releitura independente concluída.**

Não foi identificado bypass confirmado de identidade, admin, vínculo QR/atividade, presença ou inscrição na implementação local inspecionada.

## Review mode

CODE_QUALITY, com avaliação de segurança, contrato HTTP e migration-safety.

## Open questions

- Apply remoto continua **BLOCK**: schema completo, versão/roles, preflight, backup, janela e autorização específica ainda não foram comprovados. O teste sintético não substitui esses itens.
- Rollback operacional preserva o schema E2; voltar apenas ao backend E1 deixa inscrições indisponíveis. Esta limitação precisa acompanhar o handoff da Vercel.
- Confirmar necessidade do endpoint administrativo de cadastro de certificado; é protegido, mas ainda não possui fluxo correspondente no painel.

## Test gaps

- Execução independente final: `npm test`, exit 0; **18/18 testes reais passaram**, 7,094 segundos. Script restringido a `test/*.test.js`, sem contar arquivo de fixture como teste.
- SQL realmente executado em PGlite: autoria da presença, janela expirada, duplicidade, proibição de DML direto, isolamento de mensagens/notificações/resultados e cálculo do quiz.
- HTTP local: autenticação/admin, erros sem vazamento, QR aleatório com hash, PNG, contrato agregado com fixture e redirecionamento local.
- Cobertura final adicional reverificada: inscrição repetida, capacidade cheia, curso inativo, proibição de INSERT direto/anon; QR vinculado à atividade, futuro e encontro cancelado; notificações apenas para alteração real; certificado protegido por admin, confirmação, HTTPS e inscrição, com leitura privada.
- Revogação de grants por coluna e detecção de grants herdados foram inspecionadas estaticamente; não foram exercitadas com fixture específica de herança nesta execução.
- PGlite usa uma conexão: não demonstra disputa multiconexão. Supabase real, JWT/RLS hospedados, CDN Vercel, câmera Android e instalação no aparelho não foram exercitados por este revisor.
- Não há limite de frequência específico das operações autenticadas de mensagens/quiz/QR; capacidade/abuso em exposição pública permanece risco operacional. Nenhum teste de carga ou scanner de dependências foi executado por este revisor.

## Summary

Código local sem findings pendentes, verificação **PASS 18/18**. Segurança/API **WARN** pelos limites operacionais e integração hospedada ainda não exercitada. Migração remota **BLOCK** até os gates do runbook. Não é aprovação de publicação ou de prontidão global.

SHA-256 no congelamento revisado: `src/app.js` = `d22dd1e193d77f393d1e44e14a71ef675f3c9b36cd2c8e5fc843221f99031b8f`; `sql/001_entrega2.sql` = `dc71982335070a2a825dbc0d3b6ae5f396a55ca1fed297164e62f91532c5d8d4`.

Fronteiras examinadas: clientes Android/browser → Express (getUser para JWT, validação, erros genéricos) → Supabase com token do usuário (RLS) → RPC SECURITY DEFINER (auth.uid, admin em user_roles, search_path vazio, validação transacional). Nenhuma service_role é necessária no código. Presença possui índice único e locks compartilhados; inscrição serializa pelo curso com FOR UPDATE e revoga caminhos diretos de escrita.

Fontes oficiais consultadas em 08/11/2026: [Supabase RLS](https://supabase.com/docs/guides/database/postgres/row-level-security), [PostgreSQL locks](https://www.postgresql.org/docs/current/explicit-locking.html), [Express na Vercel](https://vercel.com/docs/frameworks/backend/express). A configuração `public/**` e headers em `vercel.json` está coerente com a documentação Vercel; isso não demonstra um deploy real.
