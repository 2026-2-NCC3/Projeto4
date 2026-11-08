# Registro histórico — primeira fatia, substituída

A expansão atual está em backend-complete-verification.md. O serviço puro e seus
25 testes foram removidos; os resultados abaixo não representam cobertura atual.

# Evidências locais e verificação independente

- Node v22.22.1; npm ci --ignore-scripts --no-audit --no-fund: exit 0, 80 packages.
- npm test: RED 23 falhas de comportamento (contrato vazio), GREEN 23/23.
- Segundo RED para contexto de revalidação transacional: 2 falhas; GREEN 23/23.
- Testes adicionais de regressão (test-after): reenvio expirado com presença prévia e relógio inválido.
- Regressão final: 25/25. node --check em todo src/: sem erro.
- Comparação byte a byte dos 13 JS herdados de Entrega1: iguais.
- Smoke local com URL e chave fictícias: GET /health 200 {status:ok}; processo encerrado.
- Nenhum teste acessou registros remotos. Nenhuma nova rota de presença foi publicada.

## Limitações
Doctor exit 1: runtime global com FAIL em contratos, steering coverage/drift; workspace
inicialmente sem .codex. Não instalado/alterado runtime para mascarar isso. Runner inicializado,
mas cadeia inteira não foi executada nem aprovada. Produto não é ready-for-review/produção.
Lint não configurado na base; sintaxe e testes foram executados, sem alegar lint.

## Verificação independente
/root/entrega2_base/backend_verify executou testes, sintaxe, comparação E1 e smoke: PASS
para fatia local. Encontrou design recordOnce desatualizado e ambiguidade na evidência de
refactor. Ambos corrigidos e confirmados em leitura fresca: PASS documental.
O refactor no-op posterior usa a suíte estável de 25 testes; os 2 testes posteriores
continuam identificados como test-after. Não há aprovação da integração ou entrega completa.
