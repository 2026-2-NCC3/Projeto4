# Estratégia da entrega

Manter Android Java/Retrofit e Express/Supabase. Evitar migração de framework ou segundo deploy.
Implementação sequencial por domínio para evitar divergência de contrato.

1. Backend e scripts: contratos HTTP versionados, migrações aditivas e testes negativos.
2. Android: copiar fontes/config necessárias E1 para E2 sem caches/segredos; adaptar UI e
   rede ao contrato; SQLite cache/fila e QR; build e testes.
3. Painel web: HTML/CSS/JS acessível servido Express, login JWT, autorização no servidor.
4. Integração local, browser real, APK, documentação e revisão independente.

Presença: QR opaco emitido administrativamente; RPC transacional valida auth.uid(),
inscrição e janela no banco e garante UNIQUE(user_id,activity_id). Nunca autorizar só pela UI.
Offline: captura fica pendente, confirmação somente após servidor; expiração deve ser
mostrada e presença prévia reconciliada por histórico, sem confiar no relógio do aparelho.
Admin não pode vir de user_metadata editável pelo aluno. Reutilizar user_roles protegido.
Dados locais devem ser particionados por usuário e removidos/inacessíveis no logout.

Rollback: E1 permanece utilizável. SQL remoto só após revisão, backup/preflight e aprovação.
Não alegar publicação automática: responsável Vercel muda Root Directory e faz redeploy.
