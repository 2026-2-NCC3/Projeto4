# Design

public/admin/index.html, admin.css, admin.mjs e admin-core.mjs. Assets usam /admin/
para funcionar no CDN public da Vercel e no Express local. Marca E1 (#00524B, #0D7377,
#FBFBFB) e logo local. Navegação Encontros, Cursos, Comunicados, Dúvidas. Interface
central: seletor de encontro à esquerda e grande QR/presenças à direita; mobile empilha.
Formulários de edição inline, sem modal desnecessário; estados carregando/vazio/erro.

Browser -> Express autenticado -> RLS/RPC. Cliente não concede privilégio. JWT só na
memória fechada do módulo, sem localStorage/sessionStorage/cookies. Refresh único
por sessão; contador invalida respostas antigas. Toda saída textual usa textContent.
QR aceita somente data:image/png;base64. Logout remove imediatamente dados privados.

Contrato confirmado pelo autor backend: /admin/cursos CRUD (DELETE arquiva),
/admin/encontros CRUD (DELETE cancela), QR devolve qrDataUrl, APIs comunicação documentadas.
Rollback local: remover apenas public; E1 e backend são ownership de outros autores.

Impeccable context.mjs bloqueado pelo hook strict; não contornado. Brief existente e
tokens E1 lidos diretamente. Qualidade visual depende de browser, não deste relatório.
