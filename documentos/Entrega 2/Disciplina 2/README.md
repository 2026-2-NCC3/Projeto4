# POO e estruturas de dados - Entrega 2

[Diagrama em PDF](UML_Entrega2.pdf) · [Fonte editável no diagrams.net](UML_Entrega2.drawio) · [Guia para apresentação](GUIA_POO.md)

## O que o diagrama representa

A pergunta é: **como a jornada do aluno se relaciona com as classes e estruturas do código integrado?** O público é a equipe e os professores. O recorte é o código local da Entrega 2 em 08/11/2026; o desenho não comprova implantação no Supabase/Vercel. A equipe mantém estes artefatos quando alterar o modelo ou as classes.

O PDF separa níveis de abstração:

1. Cursos, inscrições, atividades, presenças e QR.
2. Card, certificados, mensagens e notificações.
3. Questionário, perguntas, alternativas e resultados.
4. Classes Java de integração, suas dependências e classes auxiliares.
5. Fragmento, contrato do adaptador e controle das tentativas de autenticação.

Nas três primeiras páginas, os nomes em português representam **conceitos de domínio**. No código atual, a API Express retorna JSON e o Android usa Gson `JsonElement`/`JsonObject`; não existem classes Java `Aluno`, `Curso`, `Card` etc. As duas últimas páginas identificam as classes realmente implementadas. Card e TestePerfil são projeções da API, e Alternativa/Resposta são valores JSON. Não há tabelas próprias para esses quatro conceitos.

Nas associações, `1`, `0..1`, `0..*` e `1..*` indicam multiplicidade. `UUID` é o identificador no PostgreSQL, transportado como `String` no Java. O identificador de uma alternativa é uma string local à pergunta, não um UUID. Seta tracejada com ponta aberta significa dependência de uso; triângulo vazio tracejado indica implementação de interface. Na visão de código, `+` indica público e `-` privado. Campos/métodos foram selecionados para manter a legibilidade; o código continua sendo a fonte completa.

## Rastreabilidade

| Conceito | Implementação de referência |
|---|---|
| Aluno | `auth.users`, `profiles`; `GET /api/perfil` |
| Curso, Atividade | `courses`, `activities`; rotas administrativas e consulta do aluno |
| Inscrição, Presença | `enrollments`, `attendances`; RPCs `e2_enroll`, `e2_check_in` |
| Código QR | `e2_qr_codes`; `e2_issue_qr`; `core/QrPayload.java` |
| Card | Projeção `GET /api/card`, derivada do usuário/perfil |
| Certificado | `certificates`; `GET /api/certificados` |
| TestePerfil | Projeção `GET /api/teste-perfil` |
| Pergunta, Alternativa | `e2_profile_questions` e coluna JSON `alternatives` |
| ResultadoPerfil, Resposta | `e2_profile_results` e coluna JSON `answers`; `e2_submit_profile` |
| Mensagem | `e2_messages`; remetente aluno/equipe |
| Notificação, Leitura | `e2_notifications`, `e2_notification_reads` |
| Comunicado | `e2_announcements` |

Fontes versionadas:

- [API Express](../../../src/Entrega2/Backend/src/app.js)
- [Migração e regras transacionais](../../../src/Entrega2/Backend/sql/001_entrega2.sql)
- [Contrato HTTP](../../../src/Entrega2/Backend/docs/http-contract.md)
- [Classes Android](../../../src/Entrega2/Frontend/app/src/main/java/com/example/myapplication)

## Reprodução e verificação

`gerar_diagramas.py` é a fonte comum das cinco páginas PDF e draw.io, requer Python 3 e ReportLab. Execute nesta pasta:

```sh
python3 gerar_diagramas.py
```

O `.drawio` é XML editável, com cinco páginas, caixas, conexões, textos e multiplicidades. Abra com diagrams.net; não é uma imagem achatada. Se editar o draw.io manualmente, exporte novamente o PDF e preserve a alteração também na fonte Python, ou escolha o draw.io como nova fonte oficial.

Validação realizada: geração concluída, XML reaberto, PDF com cinco páginas; todas as páginas renderizadas e inspecionadas visualmente. Corrigido sobreposição de multiplicidades próxima às caixas. Revisão independente do conteúdo deve ser consolidada pelo agente principal; não foi simulada neste documento.

## Arquivo recebido

[PDF original da equipe](UML_recebido_para_revisao.pdf) foi preservado sem alteração. A revisão inicial identificou classes cortadas entre páginas, ausência de multiplicidades e IDs inteiros divergentes do backend. A versão desta pasta reorganiza a apresentação e explicita a diferença entre modelo de domínio e classes implementadas.

## Apresentação visual

Versão revisada com caixas UML em preto e branco, nome centralizado, atributos/métodos
em compartimento separado e títulos simples. As notas técnicas completas ficam no guia.
Essa revisão altera a apresentação; não muda as entidades ou as relações documentadas.
