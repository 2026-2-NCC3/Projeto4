# Aplicação da migração E2 — gate humano pendente

Não foi aplicada no Supabase. `001_entrega2.sql` foi testada em PostgreSQL embedded PGlite
com fixture sintética do schema confirmado, não em uma cópia integral do projeto remoto.
PGlite usa uma conexão exclusiva: não prova corrida multiconexão nem configuração Supabase hospedada.

1. Confirmar ambiente/projeto correto e responsabilidade pelo apply. Salvar backup/export do schema,
   grants/policies e dados afetados por meio autorizado antes da janela de alteração.
2. Rodar `000_preflight.sql` (leitura) e comparar campos/defaults com o schema esperado.
   Resolver duplicidades sem excluir registros automaticamente. Conferir roles/grants herdados.
3. Confirmar que as tabelas e2_* não pertencem a outra aplicação. Conferir versão PostgreSQL,
   constraints, defaults adicionais obrigatórios e impacto de locks/índices em tabelas existentes.
4. Revisar e aprovar `001_entrega2.sql`. Executar como owner controlado no SQL Editor.
   Há transação, lock_timeout5s e statement_timeout30s: erro deve causar rollback total.
5. Conferir que alunos não conseguem gravar attendances/enrollments/user_roles via REST direto,
   nem listar códigos QR ou resultados de outros usuários. Validar role administrativa existente
   em user_roles; provisionamento de admin é separado e exige autorização.
6. Publicar backend e Android E2 juntos. Testar contas fictícias de aluno/admin, presença repetida,
   QR inválido e de outra atividade, agenda/histórico, perfil, quiz, suporte e certificados.

## Mudanças e compatibilidade

Adiciona e2_* e activities.is_cancelled, índices únicos em presença/inscrição, funções e políticas
com prefixo e2. Não apaga dados. Restrição única pode falhar se o banco tiver duplicidades: para,
não deduplica automaticamente. Novos índices podem bloquear por curta janela limitada por timeout.

Revoga INSERT/UPDATE/DELETE (incluindo grants por coluna) de attendances, enrollments e user_roles
para PUBLIC/anon/authenticated. Presença e inscrição passam somente pelas RPCs autenticadas.
Leitura E1 preservada; **backend E1 deixa de poder inserir inscrições diretamente após a migração**.
GET cursos E2 passa a exigir JWT: o APK E1 sem token nessa chamada não é compatível.

## Rollback

Antes de COMMIT, qualquer erro desfaz a migração inteira. Depois do apply, não executar DROP de
tabelas nem desfazer dados. Preferir corrigir/restaurar o deployment E2 conhecido e manter dados.
Se restaurar backend E1, comunicar indisponibilidade de inscrição até adaptação da rota para RPC.
Reabrir grants antigos reintroduz bypass de QR/vagas/roles e exige revisão e aprovação específica.
Backup/restore remoto não foi testado nem autorizado nesta sessão.

## Regras acadêmicas explícitas

Quiz é orientação exploratória de três perguntas, sem diagnóstico; empate usa ordem alfabética.
Certificado é URL de documento previamente emitido; administrador deve confirmar que os critérios
da organização foram atendidos. O sistema não inventa certificados nem determina regra oficial
de frequência. Novo cadastro sem profile ainda exibe nome da identidade/Aluno e escola vazia,
sem alegar que a ONG já cadastrou informações institucionais do participante.
