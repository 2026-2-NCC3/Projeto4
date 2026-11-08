# Evidência do autor — painel

Registro: 08/11/2026 18:08 UTC. Não é aprovação independente.

## Execução observada

- `node --test .codex/spec-loop/entrega2-painel/core.test.mjs`: RED observado
  com nove falhas de comportamento no scaffold (retornos undefined/rejeições ausentes).
  Após implementação: GREEN 9/9; nova execução após handlers também 9/9.
- `node --check src/Entrega2/Backend/public/admin/admin.mjs`: exit 0.
- HTMLParser + inspeção de IDs, referências e assets: 87 IDs únicos; todos os
  labels, seletores literais do JS e três assets estáticos locais resolvidos.
- Nenhum token persistido em browser storage. Texto de API usa textContent;
  QR aceita somente PNG data URL produzido pelo servidor. Cliente envia Bearer
  apenas para caminhos /api/ locais, sem credenciais/cookies ou redirects.

Cobertura unitária: janela QR, email normalizado/senha preservada, destino local,
refresh401 concorrente único, refresh inválido encerra, refresh503 preserva,
logout durante request/login impede resposta tardia, refresh troca de identidade
encerra sessão. DOM e handlers têm checagem estática, não foram chamados TDD.

## Handoff ao principal

Fixture backend local existente: `http://127.0.0.1:38766/admin/`.
O principal recebeu as credenciais sintéticas do autor backend. Não há credenciais
nem dados fictícios embutidos no painel produtivo.

1. Login errado e conta aluno: erro, nenhum dado privado. Admin: abre Encontros.
2. Criar/editar curso (data, descrição, local, vagas, encontros, carga horária);
   arquivar com confirmação; reativar pelo checkbox na edição.
3. Criar encontro associado a curso ativo, editar, selecionar, gerar QR15min.
   Verificar imagem real retornada pela API, timer e lista de presenças.
4. Trocar encontro: QR anterior some. Cancelar: histórico preservado e geração
   desabilitada. Regenerar QR limpa o anterior antes da resposta.
5. Publicar comunicado e responder à conversa. Validar payload/destinatário.
6. Logout: sessão e dados removidos. Refresh da página exige novo login.
7. Desktop e viewport estreita; navegação por teclado, foco, labels, ausência
   de overflow, estados vazio/erro/loading, screenshots e console.

Dados reais, schema Supabase, login real, câmera Android e Vercel permanecem fora
desta evidência local. O backend é responsável pelo privilégio e validade final QR.

## Limites/gates

Browser real será executado pelo principal. Nenhum PASS de interface é alegado
por este autor. Impeccable context script foi bloqueado pelo hook strict; não foi
contornado. Brief local e tokens E1 guiaram o design. Revisão do código-fonte
confirma integração, mas não substitui teste ponta a ponta com banco e aparelho.
