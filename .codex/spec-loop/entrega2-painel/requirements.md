# Painel administrativo da Entrega 2

tdd-scope: required
ui-scope: required

Gap: não existe módulo web no repositório. Hipótese: uma interface estática servida
pelo Express existente permite administrar encontros, QR, presenças e comunicação
sem outro serviço de hospedagem.

Aceite: login e checagem administrativa; tokens somente em memória; renovação
serializada; logout impede resposta tardia de repor dados; cursos/encontros com
criação/edição/arquivamento/cancelamento; QR PNG fornecido pela API com validade;
presenças, comunicados e suporte; navegação teclado e layout estreito.

Sem mutação remota/deploy, sem bibliotecas de CDN, sem dados fictícios na UI produtiva.
Testes TDD na sessão e expiração do QR. HTML/CSS e handlers terão verificação de sintaxe
e inspeção browser independente pela sessão principal; não alegar browser realizado.
