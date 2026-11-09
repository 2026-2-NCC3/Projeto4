# Situação da Entrega 2 - 08/11/2026

Implementação local concluída na branch `Luiz-entrega2`. **Integração hospedada e
aceite completo pendentes.** Reparo de duplicidades e migração Supabase aplicados em08/11,
após confirmação do Luiz, pelo Chrome pessoal. A branch foi enviada ao GitHub e o código foi revisado por Luiz. Merge, deploy E2,
acesso administrativo e validação hospedada ainda precisam de confirmação. Prazo: 09/11 às 08h.

## Entregue localmente

- Android Java/XML com ConstraintLayout e Fragment real, autenticação/refresh,
  cursos, inscrição, agenda, QR, SQLite por usuário, sincronização, histórico,
  card, certificados, teste de interesses, comunicados, mensagens e notificações periódicas.
- API autenticada e SQL transacional com RLS, controle de vagas/duplicidade e QR por atividade.
- Painel administrativo `/admin/` no mesmo projeto Express/Vercel.
- Contrato HTTP, preflight e runbook, demonstração sintética, roteiro de testes.
- [APK debug HTTPS](<../../documentos/Entrega 2/Programação para Dispositivos Móveis/proxima-etapa-entrega2.apk>),
  UML PDF/drawio, guia POO, conferência estatística e boxplots finais em PDF/PNG, com base CSV.

## Verificações executadas

| Verificação | Resultado |
|---|---|
| Backend HTTP e SQL/PGlite, incluindo reparo |27/27 PASS; revisão independente do SQL PASS|
| Supabase remoto, conferência após aplicação |6 inscrições únicas,7 cópias arquivadas,0 duplicatas;13 registros preservados exatamente contra backup;7 tabelas E2 com RLS,9 funções|
| Painel núcleo sessão/QR |9/9 PASS|
| Chrome real, login/permissão/CRUD/QR/comunicação/logout |PASS em fixture local|
| Responsividade painel390px |Sem overflow observado, screenshots salvos|
| Android JVM, reexecutado em 08/11 |21/21 PASS, 0 falhas e 0 ignorados|
| Android instrumentação Pixel_10a |6/6 PASS (3SQLite+2Fragment+smoke)|
| Android build APK |PASS; atualizado com PDF de demonstração embarcado|
| Certificado demo embarcado, testes no emulador |4/4 PASS: opção offline/recriação, compartilhamento somente leitura, exportação e isolamento; leitor externo/seletor ainda exigem teste manual|
| Android lint, reexecutado em 08/11 |0 erros, 102 avisos; não suprimidos|
| Redmi Note 8, backend local |QR online, leitura offline e sincronização sem duplicação, leitor na vertical: confirmados por Luiz|
| Certificado no Redmi, backend local |Abertura e download do PDF de demonstração confirmados por Luiz|
| Código Entrega1 |Sem alterações|
| Padrões de segredos locais |Nenhuma ocorrência nos padrões examinados; Gitleaks ausente|
| Doctor/global product-final-eval strict |FAIL/BLOCKED; sem alegação ready-for-review|

Os relatórios detalhados das revisões e as capturas de teste permanecem locais,
fora do versionamento. O resumo dos resultados e limites está nesta página.

## Pendências para a demonstração integrada

1. Banco atualizado: `000a_repair_enrollments.sql` e `001_entrega2.sql` aplicados no
   projeto `hejcktjrlursifgcjdon`. Não reaplicar para testar. Conferência descrita no runbook.
2. Confirmar que a conta administrativa indicada por Luiz consegue entrar no painel
   publicado. Concessão do papel e teste remoto ainda não comprovados nesta revisão.
3. Confirmar merge e o responsável publicar na Vercel com Root Directory
   `src/Entrega2/Backend`, mantendo as variáveis Supabase corretas.
4. Testar com aluno/admin fictícios na URL hospedada: login, inscrição, QR repetido,
   perda de internet, histórico, mensagens e abertura de certificado real autorizado.
5. Testar recusa de permissão, fonte ampliada e legibilidade nativa. Câmera física e
   leitor na vertical já foram confirmados no teste local; repetir o fluxo na API publicada.

**Atenção à compatibilidade:** a migração restringe escrita direta em inscrições/presenças.
Retornar somente ao backend E1 não restaura essas operações. O runbook explica a janela
e o rollback; a mudança não deve ser aplicada isolada da publicação E2.

O acesso ao Supabase pelo Chrome pessoal foi restabelecido e usado no preflight,
backup e aplicação. A indisponibilidade de inscrições E1 até a publicação E2 foi
informada e aceita pelo Luiz antes do apply. Houve limitações no controle do emulador; os testes ópticos foram concluídos
por Luiz no celular físico, usando o backend local.

## Materiais acadêmicos

- Documento COM EMPRESA: ainda é modelo; Luiz ficou responsável por preencher dados e atividade real.
- Banner: o arquivo presente é modelo; falta o banner final do grupo.
- Dados: identificação atualizada com os integrantes do README; frase sobre presença por QR e referências corrigidas no DOCX. Cálculos e gráficos preservados.
- POO: PDF e draw.io mostram um recorte fiel das classes implementadas. O mapeamento das entidades do enunciado está no README; não há classes Java próprias Aluno/Curso/etc. A aceitação desse desenho pela professora ainda precisa ser confirmada.
- Geradores auxiliares e PDF de referência antigo removidos por decisão da equipe. Os arquivos finais permanecem disponíveis.

Nenhum resultado social, atividade com ONG, assinatura ou certificado final foi inventado.
O app inclui um PDF explicitamente demonstrativo, sem validade, para abertura e
salvamento offline. Não representa certificado final da ONG nem depende de deploy.

## Limites técnicos conhecidos

Fixture local não replica Auth/PostgREST/CDN hospedados. Concorrência multiconexão SQL,
refresh/worker com servidor real, push instantâneo e paginação não foram validados ou
implementados além do contrato descrito. WorkManager depende das restrições do Android;
QR offline pode expirar antes da sincronização. Certificados são documentos HTTPS
previamente emitidos, não uma emissão automática baseada em presença.
