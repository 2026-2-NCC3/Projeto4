# Programação Orientada a Objetos e Estrutura de Dados - Entrega 2

[Diagrama em PDF](UML_Entrega2.pdf) · [Arquivo editável](UML_Entrega2.drawio) · [Guia de POO](GUIA_POO.md)

O diagrama apresenta, em uma página, as relações principais de inscrição, presença por QR e certificado. Foi simplificado a partir do modelo enviado pelo grupo, com caixas em preto e branco, poucos atributos e multiplicidades.

## Como ler

- Um aluno pode realizar várias inscrições; cada inscrição pertence a um curso.
- Um curso oferece atividades. Cada presença relaciona um aluno a uma atividade.
- Uma atividade pode ter vários códigos QR ao longo do tempo.
- Cada certificado pertence a um aluno e se refere a um curso.
- `1` significa um; `0..*`, zero ou mais; `-`, atributo privado no modelo conceitual.

Este é um **modelo conceitual do domínio**, com nomes em português e atributos selecionados. As entidades são tratadas como JSON pela API; as caixas não afirmam que existem classes Java com esses nomes. Comunicação, carteirinha e questionário ficaram fora deste recorte para manter a leitura simples. Os exemplos de herança, interfaces e estruturas de dados do código estão no guia.

## Fontes e manutenção

Público: equipe e professora de POO. Recorte: comportamento implementado na Entrega 2, em 08/11/2026. Responsável: equipe Próxima Etapa.

Fontes: `src/Entrega2/Backend/src/app.js`, `src/Entrega2/Backend/sql/001_entrega2.sql` e `src/Entrega2/Backend/docs/http-contract.md`, a partir da raiz do repositório. UUID identifica os registros; os nomes dos atributos no desenho foram simplificados para apresentação.

O original recebido permanece em `UML_recebido_para_revisao.pdf`. Para regenerar o PDF e o draw.io, execute `python gerar_diagramas.py` com ReportLab instalado.
