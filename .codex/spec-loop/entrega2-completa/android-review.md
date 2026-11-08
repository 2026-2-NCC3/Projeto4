# Revisão independente Android E2

Data: 08/11/2026. Escopo: `src/Entrega2/Frontend`, contrato HTTP E2 e callers do backend. Nenhuma escrita em código, banco ou ambiente hospedado pelo reviewer.

## Findings

1. **P1 — resposta de login antiga pode sobrescrever a conta atual.** No código inicialmente revisado, `AuthActivity.java:84–88` salva a sessão na thread IO antes de verificar `isFinishing/isDestroyed` no callback UI. Como o executor possui três threads, login A demorado, recriação da Activity e login B mais rápido permitem que a resposta A tardia substitua B no Keystore. A guarda existente impede somente navegação, não a gravação. Correção pedida: tentativa/geração ativa e commit apenas no callback ainda válido, com invalidação por lifecycle e teste de respostas fora de ordem. **Autor informado; correção em andamento.**
2. **P2 — cadastro instrui mínimo de senha incompatível com o backend.** `AuthActivity.java:62–64` inicialmente aceita seis caracteres e informa mínimo seis; `Backend/src/app.js` exige oito em `/api/auth/cadastro`. Senha de seis/sete chega à API e recebe erro genérico, sem orientar a correção. Alinhar regra e mensagem a oito, preservando senha sem trim. **Autor informado.**
3. **P2 — resumo chama encontro passado de próximo.** `HubActivity.java`, ramo `inicio` de `render`, inicialmente usa primeiro elemento de `nextMeetings`. `GET /api/agenda` retorna todos os encontros em ordem crescente quando não há `from`, e o cliente não aplica filtro. Quem já participou de encontros antigos continuará vendo o mais antigo como próximo. Selecionar o primeiro futuro pelo horário de São Paulo, sem eliminar histórico da agenda. **Primeiro patch usou UTC indevidamente; devolvido ao autor com cenário 09/11 08:00 SP versus agora 10:00Z. Backend devolve campos locais SP; contrato textual genérico UTC precisa explicitar exceção.**
4. **Gap de rubrica — Fragment ausente.** Por pedido do principal, busca Java/XML confirmou ausência de `Fragment`. Há Activities/Intents reais em entrada/autenticação; wrappers Agenda/Perfil/Cursos herdam Hub, mas as abas usam `show()` na mesma Activity. Se a exigência de Fragment do PDF p7 permanece na E2, essa parte está `missing`, não coberta pelos wrappers. Principal informado para decidir fatia de implementação.

## Review mode

CODE_QUALITY. Skills: code-review e cybersecurity-engineering, incluindo security-engineering-contract. O runner de consumo de contrato não existe neste checkout; fallback nos requisitos/design/contrato locais. `contractVersion`: não disponível; `contractSource`: `.codex/spec-loop/entrega2-completa/requirements.md` e `Backend/docs/http-contract.md`; `appliedLenses`: identidade/lifecycle, contrato, persistência, sincronização, transporte e privacidade. `commandsConsidered`: inspeção source/config/manifest mesclado/lint/testes; `requiredEvidence`: regressões dos três findings, build/testes frescos e limites de UI explicitados.

## Open questions

- Nenhuma pergunta de produto impede as correções locais.
- Implantação Supabase/Vercel e permissões de administrador continuam fora desta revisão local.

## Test gaps

- Não executei interação Android nativa ou câmera. Resultado instrumentado de quatro testes no Pixel_10a foi comunicado pelo principal, não executado por este reviewer.
- Testes existentes cobrem parser QR, política HTTP e SQLite, mas não exercitam o `ApiClient` real com refresh simultâneo, troca de conta durante refresh ou `SyncWorker` com resposta perdida seguida de QR expirado. A lógica foi inspecionada; não é evidência de execução.
- Fonte ampliada, teclado aberto, permissão de câmera negada e download real de certificado permanecem validações manuais.

## Summary

**FAIL_REPAIRABLE no estado inicialmente revisado**, devido ao P1 de troca de sessão. Reavaliação pendente dos patches do autor.

Controles observados: RLS é responsabilidade backend; cliente envia bearer, rejeita troca de user.id, serializa refresh e não inclui chave Supabase; sessão usa Android Keystore; banco/cache/fila particionados por user_id; backups de banco/preferências excluídos; release exige TLS; QR limita tamanho/formato e confirmação vem do servidor; estados terminais apagam token QR. Manifest mesclado contém câmera vinda do ZXing. Lint inspecionado: zero erros; 83 warnings (33 recursos legados, 18 localização de ícones, 13 concatenação de textos e avisos de versões/performance); nenhum aviso de segurança confirmado nessa lista.

Referência consultada para lifecycle: [Android Developers — Activity lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle), 08/11/2026. Recriação por rotação exige proteger callbacks associados à Activity antiga.

## Reavaliação dos patches

Inspeção após correções:

| Item | Correção observada | Estado |
|---|---|---|
| P1 login tardio | `AuthAttemptGate` global invalida substituição/destruição, consome callback; `SessionManager.save` só dentro do callback UI validado | Corrigido por inspeção; regressão independente pendente |
| P2 senha | `AuthInputPolicy` mínimo oito e mensagem alinhada; valor sem trim | Corrigido por inspeção |
| P2 próximo encontro | `NextMeetingPolicy` seleciona menor futuro, parser America/Sao_Paulo; teste de fronteira SP/UTC adicionado | Corrigido após segunda iteração |
| Fragment | Container no shell instancia `ResourceListFragment`; adapter do Hub por interface `AdapterHost`; view/adapter limpos no `onDestroyView` | Gap implementado; instrumentação pendente |

O reviewer executou `testDebugUnitTest assembleDebug lintDebug` antes do último patch: BUILD SUCCESSFUL, porém testes estavam UP-TO-DATE. Isso confirma configuração/build naquele ponto, não conta como reexecução fresca dos casos. Nova execução forçada será registrada após congelamento.

## Verificação final independente

Após congelamento, este reviewer executou:

```sh
env JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' \
  ANDROID_HOME=/Users/luizrs/Library/Android/sdk \
  bash gradlew --offline :app:testDebugUnitTest :app:assembleDebug \
  :app:lintDebug :app:connectedDebugAndroidTest --rerun-tasks
```

Resultado: **BUILD SUCCESSFUL em 23s; 79 tarefas executadas**. JVM: **16/16**, dos quais quinze são regressões/core e um smoke legado. Instrumentação Pixel_10a Android17: **6/6**, dos quais dois Fragment/recriação, três SQLite e um smoke legado. Lint final: **0 erros, 89 warnings** (acréscimo de dependências de teste/recursos; não suprimidos).

SHA256 do APK debug HTTPS produzido nessa execução: `a45b3ad1fc913d289d6edef6c6b30cd20ac3cca1fb727684a968c8cbed9dc245`.

**Parecer final: PASS das correções locais revisadas, com gaps de homologação explícitos.** P1 login, P2 senha e P2 próximo encontro encerrados pela inspeção dos callers mais testes reais. Gap Fragment encerrado por implementação ativa no shell e testes instrumentados de instanciação/recriação. Nenhum finding de código permanece aberto nesta revisão. Isso não declara produção ou aceite visual final: câmera/celular físico, backend remoto, download de certificado e concorrência real de refresh/worker continuam sem evidência específica. O reviewer não alterou implementação.
