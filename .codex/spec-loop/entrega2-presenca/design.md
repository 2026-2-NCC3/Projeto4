# Design da primeira fatia

Copiar somente src/, package.json e package-lock.json da base Express. Não copiar secrets,
SQL legado, node_modules ou README que alegue publicação da E2.

## Contrato interno
createCheckInService({ repository, clock }) => async checkIn({ userId, qrToken }).
repository.findQr(token) retorna { activityId, opensAt, expiresAt } ou null.
repository.isEnrolled(userId, activityId) retorna boolean.
repository.recordOnce({ userId, qrToken, activityId, scannedAt }) retorna { attendance, created }.
recordOnce precisa revalidar QR/inscrição na transação, ser atômico e garantir UNIQUE(user_id, activity_id) no adaptador real.
O serviço não promete atomicidade via sequência SELECT/INSERT; esse contrato pertence ao banco.
O QR opaco deve ser emitido no servidor, guardado com controle de acesso e validado no banco.
Nenhum algoritmo criptográfico próprio ou credencial administrativa no Android.

Janela é [opensAt, expiresAt); relógio do servidor determina aceitação. Código expirado é
rejeitado mesmo em reenvio; operação de sincronização futura precisará apresentar rejeição.
O serviço não confia no horário enviado pelo aparelho.

Erros internos tipados com code/status: INVALID_INPUT 400, INVALID_QR 400,
QR_NOT_ACTIVE 409, QR_EXPIRED 410, NOT_ENROLLED 403. Dados inválidos do repositório
e falhas operacionais propagam erro para futura fronteira HTTP mapear 503 sem payload interno.

## Compatibilidade e rollback
Rotas existentes permanecem em src/server.js. Nova lógica ainda não é registrada no servidor.
Nenhuma nova dependência de runtime. Rollback: continuar publicando Entrega1.

## Fronteiras de confiança
Android/painel -> JWT validado pelo middleware -> serviço -> repositório/banco.
Esta fatia testa apenas serviço e interface de repositório; fake reside exclusivamente em test/.
