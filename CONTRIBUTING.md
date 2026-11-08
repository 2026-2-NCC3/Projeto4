# Política de commits

Trabalhar na branch `Luiz-entrega2` e registrar alterações em pequenos blocos coerentes.
Um commit deve resolver um assunto e incluir os testes/documentação diretamente ligados a ele.
Evitar juntar painel web, aplicativo e análise de dados no mesmo commit.

## Mensagens

Use `tipo(escopo): descrição curta`, em português, explicando o resultado.

- `feat(qr): valida presença e impede duplicidade`
- `fix(android): preserva sessão após girar a tela`
- `feat(web): permite editar encontros`
- `docs(uml): atualiza relações entre as classes`
- `test(backend): cobre QR expirado`
- `build(android): atualiza APK validado`

`feat` adiciona comportamento; `fix` corrige; `docs` documenta; `test` amplia testes;
`build` atualiza empacotamento; `chore` cuida da organização do repositório.

## Antes de registrar

1. Conferir o diff e adicionar somente os arquivos do assunto.
2. Rodar os testes adequados se houver mudança de comportamento; registrar limitações.
3. Não incluir `.env`, credenciais, caches, dependências baixadas ou dados reais de alunos.
4. Manter API e consumidores compatíveis; se uma alteração depende de outra, respeitar a ordem.
5. Commit local e push são ações diferentes: conferir a branch e combinar a publicação.

Os commits iniciais da E2 agrupam o trabalho que já estava pronto por módulo. Não
reconstituem artificialmente uma cronologia de desenvolvimento ou ciclos de TDD.
Nos próximos ajustes, registrar cada bloco concluído antes de iniciar outro assunto.
