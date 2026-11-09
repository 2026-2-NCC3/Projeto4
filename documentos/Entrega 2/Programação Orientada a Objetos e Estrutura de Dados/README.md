# Programação Orientada a Objetos e Estrutura de Dados - Entrega 2

[Diagrama em PDF](UML_Entrega2.pdf) · [Arquivo editável](UML_Entrega2.drawio) · [Guia de POO no código](GUIA_POO.md)

Diagrama no estilo do original da equipe: aluno no centro, classes relacionadas ao redor e caixas com três partes (nome, atributos e getters/setters). As 13 classes conceituais estão em uma única página, sem textos cortados.

## Como explicar as relações

- Aluno consulta sua carteirinha (Card), troca mensagens e recebe notificações.
- Aluno realiza inscrições em cursos, registra presenças em atividades e consulta certificados relacionados a cursos.
- Curso oferece atividades.
- Aluno obtém resultados do TestePerfil, composto por perguntas com alternativas.

As linhas mostram associações conceituais; não representam herança nem direção de chamadas. Multiplicidades foram omitidas nesta visão resumida, como na referência.

## Escopo do modelo

Este é um **modelo conceitual de POO para apresentação acadêmica**. Os getters/setters ilustram encapsulamento no modelo e não afirmam que existem essas classes e métodos no Android. A API trabalha com JSON; os exemplos efetivos de POO no código estão em GUIA_POO.md.

Os atributos foram ajustados ao domínio atual: registros usam UUID; Alternativa tem identificador textual e área; Certificado contém URL e data de emissão. Card e TestePerfil são projeções, sem tabela própria. A condição de leitura de uma notificação é relativa ao aluno. Os tokens e detalhes internos do QR ficam fora deste desenho; a presença é validada pela API.

Público: equipe e professora de POO. Responsável: equipe Próxima Etapa. Recorte: Entrega 2, 08/11/2026.
Fontes: PDF original (`UML_recebido_para_revisao.pdf`), `src/Entrega2/Backend/src/app.js` e `src/Entrega2/Backend/sql/001_entrega2.sql` a partir da raiz do repositório.

Para regenerar PDF e draw.io: `python gerar_diagramas.py`, com ReportLab instalado.
