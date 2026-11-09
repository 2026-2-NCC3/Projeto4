# Programação Orientada a Objetos e Estrutura de Dados - Entrega 2

[Diagrama em PDF](UML_Entrega2.pdf) · [Arquivo editável](UML_Entrega2.drawio) · [Guia de POO](GUIA_POO.md)

O desenho mostra classes e interfaces que existem no Android da Entrega 2, com seus principais atributos, métodos e relações. Mantém caixas simples com três compartimentos. Não foram adicionados getters/setters que não existem no código.

## Organização do desenho

Uma página, no estilo da referência da equipe: **HubActivity no centro**, caixas de três compartimentos, poucos atributos e métodos, com relações ao redor. O recorte mostra ApiService, ApiClient, SessionManager, HubActivity, AlunoRepository, QrPayload, SyncScheduler, AlunoDbHelper e SyncWorker.

`+` público; `-` privado; `...` abrevia parâmetros; `{static}` indica membro de classe e `{final}` impede reatribuição. Caixas com compartimento vazio não possuem atributos selecionados. Setas tracejadas mostram dependência de uso; linha contínua representa a referência `helper` mantida pelo repositório. Relações e membros secundários foram omitidos para legibilidade.

Heranças reais: HubActivity herda AppCompatActivity; SyncWorker herda Worker; AlunoDbHelper herda SQLiteOpenHelper. AlunoRepository implementa AutoCloseable. HubActivity implementa ResourceListFragment.AdapterHost. Essas informações ficam no guia, evitando caixas extras no desenho.

O diagrama é um **recorte das classes principais**, não um inventário completo. A autenticação, o fragmento e as classes internas de apresentação estão documentados no guia e continuam no código. O formato resumido não muda o limite de atendimento descrito abaixo.

## Correspondência com os conceitos do enunciado

O enunciado pede diagrama completo e código integrado e cita as entidades abaixo, quando aplicáveis. **Na implementação atual, elas não são classes Java próprias**: o Android recebe JSON e o backend Express consulta tabelas e funções SQL. Por isso elas não aparecem como classes com getters/setters neste diagrama.

| Conceito | Representação implementada | Uso no aplicativo |
|---|---|---|
| Aluno | `profiles` e identidade autenticada; `/api/perfil` | `SessionManager` e `HubActivity` |
| Curso | `courses`; `/api/cursos` | `HubActivity.load/render` |
| Atividade | `activities`; `/api/agenda` | `HubActivity` e `NextMeetingPolicy` |
| Inscrição | `enrollments`; `/api/cursos/:id/inscricao` | `HubActivity.mutate` |
| Presença | `attendances`; `/api/presencas` e `/check-in` | `QrPayload`, `AlunoRepository.Pending` e `SyncWorker` |
| Certificado | `certificates`; `/api/certificados` | `HubActivity.certificate` |
| Card | Projeção de perfil; `/api/card` | `HubActivity.render` |
| TestePerfil | Objeto agregado de `/api/teste-perfil` | `HubActivity.choose/submitQuiz` |
| Pergunta | `e2_profile_questions` | JSON consumido por `HubActivity.choose` |
| Alternativa | JSON `alternatives` de cada pergunta | `HubActivity.answers` guarda a escolha |
| ResultadoPerfil | `e2_profile_results` | `HubActivity.submitQuiz/render` |
| Mensagem | `e2_messages`; `/api/mensagens` | `HubActivity.sendMessage` |
| Notificação | `e2_notifications` e `e2_notification_reads` | `SyncWorker` e `AlunoRepository` |

**Limite de atendimento:** este ajuste torna o diagrama fiel ao código, mas não cria as classes de domínio citadas no enunciado. Se a avaliação exigir essas entidades implementadas como classes, essa exigência continua pendente de implementação; documentação não substitui código. O PDF original permanece em `UML_recebido_para_revisao.pdf`, apenas como referência recebida.

## Classes auxiliares fora das caixas

- `LoginActivity` e `CadastroActivity` herdam `AuthActivity`; CadastroActivity sobrescreve `signup()`.
- `MainActivity`, `PaginaInicial`, `PerfilActivity`, `AgendaActivity`, `ConfiguracoesActivity` e `CursosActivity` são telas de entrada ou especializações; consulte o guia e seus fontes.
- `AlunoApplication` herda `Application` e mantém `IO: ExecutorService` estático para tarefas em segundo plano.
- `NextMeetingPolicy.next(JsonArray, long): JsonObject` seleciona o próximo encontro; `SyncPolicy.forStatus(int): Action` decide como tratar falhas HTTP.
- `SessionManager.Session` contém os dados da sessão; `AlunoRepository.Cached` contém payload e horário do cache; `AlunoRepository.Pending` representa uma leitura pendente.
- `ApiClient.ApiException` herda `IOException` e expõe `status: int`.

## Estruturas de dados

O guia detalha `Map<String, String>` para respostas do questionário, `Set<String>` para evitar repetição visual de presenças e `List<Row>` para apresentar dados no RecyclerView. A fila offline é persistida em SQLite.

## Fontes e reprodução

Público: equipe e professora de POO. Responsável: equipe Próxima Etapa. Inspeção: 08/11/2026.

Fontes, a partir da raiz: `src/Entrega2/Frontend/app/src/main/java/com/example/myapplication/` (incluindo `network`, `data`, `sync` e `core`), `src/Entrega2/Backend/src/app.js` e `src/Entrega2/Backend/sql/001_entrega2.sql`.

Execute `python gerar_diagramas.py` com ReportLab instalado para regenerar PDF e draw.io da mesma fonte. O script verifica se cada membro cabe na largura da caixa.
