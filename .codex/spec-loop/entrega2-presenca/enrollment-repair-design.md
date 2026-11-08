# Reparo limitado de inscrições — 08/11/2026

tdd-scope: required

Gap observado: `001_entrega2.sql` recusa as quatro combinações aluno/curso
duplicadas no banco remoto. Há 11 linhas nesses grupos, sete excedentes,
todas `enrolled`, zero pontos e sem presença. Não há FK apontando para
enrollments nem triggers próprios, segundo preflight do responsável.

Hipótese mínima: preservar integralmente as sete linhas excedentes e a
fotografia da linha canônica em schema privado, depois retirar apenas as
cópias da tabela ativa e criar o índice único na mesma transação. A canônica
é a linha mais antiga, com desempate por UUID. Nenhuma linha distinta ou
com evidência acadêmica pode ser consolidada automaticamente.

Invariantes: até sete excedentes/quatro grupos; schema confirmado de sete
colunas; nenhum trigger/FK inbound; validação de status/pontos/presença;
lock antes do inventário; arquivo conferido antes do DELETE; RLS e REVOKE
no arquivo; restore exato testado. Arquivo no mesmo banco não é backup
independente. Execução remota fica com o responsável, por navegador.

Rollout: reparo é separado de 001. Mantém grants de E1; índice passa a
recusar inscrição repetida. E1 retorna500 para repetição, mas inscrição
nova continua funcionando. 001 revoga INSERT e exige publicação de E2
coordenada. Restore recusa presença de funções E2 e canônica alterada.
Não remover proteção única em ambiente E2 ativo.

Aceite: conservação exata canônicas+arquivo; rerun sem mudança; falha
atômica em estado divergente; acesso negado a anon/authenticated; rollback
reconstitui todas as linhas; índice protege nova duplicata.

Fontes: PostgreSQL17 LOCK (SHARE ROW EXCLUSIVE impede escritores enquanto
permite leitores) e REVOKE, consultadas08/11/2026:
https://www.postgresql.org/docs/17/sql-lock.html
https://www.postgresql.org/docs/17/sql-revoke.html
