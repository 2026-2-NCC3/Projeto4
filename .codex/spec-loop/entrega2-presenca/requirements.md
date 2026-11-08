# Entrega 2 — primeira fatia de backend

tdd-scope: required
ui-scope: not-applicable

## Gap e hipótese
Entrega1 possui consultas de presença, mas não registra check-in. Entrega2 está vazia.
Hipótese: preservar a base Express e introduzir um serviço de check-in testável permite
validar regras antes de modificar o banco remoto ou conectar a câmera.

## Aceite desta fatia
- Entrega1 permanece intacta; backend E2 mantém rotas existentes e health check.
- Serviço recebe identidade autenticada pelo chamador e QR opaco, nunca user_id do corpo HTTP.
- Rejeita código ausente/inválido, janela expirada/futura e aluno não inscrito.
- Persistência exige operação atômica por (user_id, activity_id); reenvio durante a validade do QR retorna a mesma presença. Após expiração, rejeita e a sincronização futura deve reconciliar pelo histórico.
- Falhas do repositório não se tornam sucesso; clock injetável torna limites reproduzíveis.
- Testes nativos Node observados falhando antes da implementação e passando depois.

## Limites
Não habilitar rota HTTP de presença sem adaptador seguro, verificação JWT e integração real.
Não aplicar SQL, fazer deploy/push, acessar registros pessoais ou criar credenciais.
Esta fatia não satisfaz a Entrega2 completa. Android, painel, banco local, sincronização,
certificados, perfil, mensagens e demais entregáveis permanecem no backlog.

## Fontes
Pedido do usuário em 08/11/2026; PDF PI páginas 6–7; código src/Entrega1/Backend;
esquema do Supabase observado em modo leitura pela sessão principal.
