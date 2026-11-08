# Brief da interface

frontendMode: authorial
layoutFingerprint: student-bottom-navigation-and-agenda; admin-meeting-selector-and-qr-stage

Públicos: aluno no Android e equipe administrativa no navegador. Preservar identidade E1.
Revisão independente read-only: brief_ui, WARN de correlação QR e renovação de sessão,
encaminhados ao autor backend antes de construir consumidores.

## Tokens e assets
Fundo #FBFBFB; títulos/ações #00524B; seleção #0D7377; superfícies brancas.
Ícones e logo já presentes em E1; sem fontes/assets externos. Proveniência: repositório
fornecido pelo usuário, reutilização no mesmo projeto acadêmico; licença externa não presumida.
Texto Android normal 16sp/secundário14sp, alvo mínimo48dp, margens16dp.

## Android
Manter Início/Cursos/Agenda/Perfil. ConstraintLayout em telas e itens; RecyclerView para
listas, ScrollView para conteúdo extenso. Sem valores fixos de frequência/conclusão fictícios.
Início: próximo encontro e comunicados. Agenda: ação Registrar presença. Perfil: card,
certificados, teste orientativo, dúvidas, notificações e logout.
Estados: carregando, vazio explicativo, erro com tentar novamente, offline com dados salvos,
pendente/confirmado/rejeitado com motivo. Texto em sp, nomes longos com limites de largura.
Fila por userId; saída de conta não pode transmitir pendência com outra identidade.
Questionário mostra progresso e resultado sem alegação de diagnóstico psicológico.

## Painel
Mesmo Express, navegação Encontros/Comunicados/Dúvidas, gestão de cursos quando integrada.
Login separado, autorização real server-side. Seleção de encontro + grande área de QR
(elemento principal), data/local/validade e presenças atualizáveis. QR expirado claramente
desativado. Mobile empilha seleção/conteúdo. Formulários com labels, foco e feedback aria-live.
Sem métricas decorativas ou dados não consultados.

## Validação
Browser real desktop/mobile/teclado/empty/error/403/QR expirado; screenshots em
ScreenshotsToCloseLoop/runs/. Android build/test + emulador/aparelho, fonte ampliada,
scanner cancelado/negado, offline/retry/expiração/troca de conta. Review de fonte não é teste visual.
