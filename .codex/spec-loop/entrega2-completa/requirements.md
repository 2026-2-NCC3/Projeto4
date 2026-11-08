# Entrega 2 completa

tdd-scope: required
ui-scope: required

Autorização: usuário pediu concluir implementação local em modo automático em 08/11/2026.
Prazo informado: 09/11/2026 08:00 America/Sao_Paulo. Branch: Luiz-entrega2.
Preservar src/Entrega1 e arquivos da primeira entrega. Não publicar, aplicar SQL remoto,
criar credenciais ou conceder admin sem revisão e aprovação da ação exata.

## Aceite rastreável ao enunciado
- Android Java XML com ConstraintLayout; login/logout, cursos e inscrições, agenda com IDs.
- Ler QR no celular, registrar presença, impedir duplicidade e exibir histórico.
- SQLite/Room: cache individual e fila persistente; perda de conexão deve produzir estado
  pendente, não sucesso fictício. Retomar sem misturar contas e reconciliar respostas perdidas.
- Card, certificados (abrir/download), questionário com resultado orientativo persistido,
  comunicados, canal de dúvidas e notificações.
- Painel web administrativo protegido, servido no mesmo Express: cursos/encontros, QR,
  presenças e comunicação. Uma única publicação Vercel.
- Backend documentado, validação/auth/RLS; migração revisável e dados de demonstração fictícios.
- APK instalável, instruções Android Studio/aparelho e roteiro de demonstração.
- Documentos Dados/POO recebidos devem ser preservados e acompanhados de validação;
  UML final deve representar implementação. Índices, API, arquitetura e explicação de estruturas.
- Extensão COM EMPRESA e banner: verificar conteúdo existente; não inventar assinatura,
  interação com ONG, aprovação do professor ou resultados sociais.

## Evidência e limites
Baseline E1 PASS. Na primeira fatia, serviço local tinha25 testes; foi substituído pela RPC
e removido. Evidência vigente: backend18, painel9, AndroidJVM16 e instrumentação6 PASS.
Integração remota permanece pendente.
Banco observado pelo Chrome: 17 tabelas e RLS ativa; esquema completo/constraints não exportados.
Doctor global FAIL em contratos/steering: não corrigir runtime ou alegar gates não executados.
Testes locais/sintéticos não equivalem à homologação com Supabase hospedado e celular real.

## Fontes
PDF PI_3CCOMP_202602_Proxima_Etapa_FINAL.pdf p6–7 e cronograma p9 (prazo corrigido pelo usuário).
Documentação API antiga é referência, não contrato vigente. Código E1 e schema atual prevalecem.
UML E2 e relatório de Dados enviados; números do relatório conferidos com CSV E1.
