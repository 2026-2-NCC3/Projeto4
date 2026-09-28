# Próxima Etapa — App do Aluno

Projeto Interdisciplinar (PI) do 3º semestre de Ciência da Computação —
FECAP, 2º semestre de 2026.

## Sobre o projeto

**Próxima Etapa** é uma ONG que atua democratizando o acesso ao ensino
superior e à orientação profissional para estudantes de escolas públicas.
Este repositório contém o **app Android do aluno**: o aplicativo pelo qual
o estudante se cadastra, consulta os cursos e encontros oferecidos pela
ONG, acompanha sua agenda de aulas e gerencia seu perfil.

O projeto é desenvolvido como parte da disciplina de Projeto Interdisciplinar
(PI) do curso de Ciência da Computação da FECAP, integrando conteúdos de
múltiplas disciplinas do semestre em uma única entrega de software.

## Integrantes

| Nome | Usuário GitHub |
|---|---|
| Fabrizzio Puttini | [@FabrizzioPuttini](https://github.com/FabrizzioPuttini) |
| Kaike Cavalcante | [@kaikecav](https://github.com/kaikecav) |
| Enzo Sakita | [@EnzoSakita](https://github.com/EnzoSakita) |
| Luiz Roberto Silvestre | [@Luiz-Silvestre](https://github.com/Luiz-Silvestre) |

## Ambiente publicado

O backend desta entrega já está **hospedado na Vercel** e em uso pelo app:

- **API em produção:** <https://nextgeneration-seven.vercel.app/>
- **Health check:**
  ```bash
  curl https://nextgeneration-seven.vercel.app/health
  ```
  Resposta esperada:
  ```json
  { "status": "ok" }
  ```

O app Android (`ApiClient.BASE_URL`) já está configurado para consumir essa
URL de produção por padrão — não é necessário rodar o backend localmente
para usar o app normalmente.

## Stack tecnológica

**Mobile (App Android)**
- Java
- Android Studio / Android SDK
- Retrofit + Gson (consumo da API REST)
- RecyclerView (listas de cursos e agenda)

**Backend**
- Node.js + Express
- `@supabase/supabase-js` (cliente do banco)
- `dotenv`, `cors`

**Banco de dados**
- Supabase (PostgreSQL) com Row Level Security (RLS) ativa

**Hospedagem**
- Backend: Vercel
- Banco: Supabase (gerenciado)

**Ferramentas**
- Git / GitHub
- Postman ou `curl` para testes manuais da API

## Estrutura de pastas

```
Projeto4/
├── documentos/              # Documentos entregáveis das disciplinas do PI
│   ├── Entrega 1/            # Disciplinas 1 a 4 — Entrega 1
│   ├── Entrega 2/            # Disciplinas 1 a 4 — Entrega 2 (em andamento)
│   └── README.md             # Índice dos documentos entregues
├── src/
│   ├── Entrega1/
│   │   ├── Backend/          # API Node.js/Express (hospedada na Vercel)
│   │   └── Frontend/         # App Android (Java)
│   └── Entrega2/             # Estrutura reservada para a próxima entrega
└── README.md                 # Este arquivo
```

## Funcionalidades

### Entregues na Entrega 1

- Cadastro e login de aluno (autenticação via Supabase Auth)
- Página inicial com dados de resumo do aluno
- Listagem de cursos e inscrição em curso
- Agenda de encontros do aluno, com campo de presença (`attended`) exposto
  pela API (o registro de presença via check-in ainda está em
  desenvolvimento)
- Perfil do aluno
- Backend REST publicado em produção (Vercel), consumido diretamente pelo
  app

### Previstas para a Entrega 2

- Consulte `documentos/Entrega 2/` e os arquivos `Colocar os códigos do
  Backend/Frontend Aqui.txt` em `src/Entrega2/` — a estrutura de pastas já
  está reservada, mas o conteúdo desta etapa ainda será desenvolvido.

## Como rodar o backend localmente

Só é necessário se você for alterar o backend. Para apenas usar o app,
ele já aponta para o ambiente de produção.

```bash
cd "src/Entrega1/Backend"
npm install
cp .env.example .env
# edite o .env com os valores do seu projeto Supabase (veja Backend/README.md)
npm start
# ou, em modo desenvolvimento (reinicia ao salvar):
npm run dev
```

O servidor sobe em `http://localhost:3000` (ou na porta definida em `PORT`).
Detalhes completos de configuração, variáveis de ambiente e deploy estão em
[`src/Entrega1/Backend/README.md`](src/Entrega1/Backend/README.md).

## Como rodar o app Android

1. Abra a pasta `src/Entrega1/Frontend` no Android Studio.
2. Deixe o Android Studio baixar/sincronizar o Gradle e o SDK necessário.
3. Rode em um emulador ou dispositivo físico.

Por padrão, o app já consome a API em produção
(`https://nextgeneration-seven.vercel.app/`), então nenhuma configuração
extra é necessária para testar o app normalmente.

Se quiser testar contra um backend rodando **localmente**, troque a
constante `BASE_URL` em
`app/src/main/java/com/example/myapplication/network/ApiClient.java`:

```java
// Produção (padrão):
private static final String BASE_URL = "https://nextgeneration-seven.vercel.app/";

// Local, no emulador Android (aponta para o "localhost" da sua máquina):
private static final String BASE_URL = "http://10.0.2.2:3000/";
```

Em dispositivo físico, use o IP da sua máquina na rede Wi-Fi em vez de
`10.0.2.2`. O tráfego HTTP sem TLS só é liberado para `10.0.2.2` e
`localhost` (veja `res/xml/network_security_config.xml`), exclusivamente
para desenvolvimento — qualquer outro host continua exigindo HTTPS.

## Endpoints da API

Base URL de produção: `https://nextgeneration-seven.vercel.app`

Rotas autenticadas exigem o header:
```
Authorization: Bearer <access_token>
```
O `access_token` é obtido no login (`POST /api/auth/login`) e é o JWT do
Supabase Auth do aluno.

| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| GET | `/health` | Não | Health check do serviço |
| POST | `/api/auth/cadastro` | Não | Cadastra um novo aluno |
| POST | `/api/auth/login` | Não | Login do aluno; retorna `access_token` |
| GET | `/api/inicio` | Sim | Dados de resumo da página inicial do aluno |
| GET | `/api/cursos` | Não | Lista todos os cursos disponíveis |
| GET | `/api/cursos/:id` | Não | Detalhe de um curso específico |
| POST | `/api/cursos/:id/inscricao` | Sim | Inscreve o aluno logado no curso |
| GET | `/api/agenda` | Sim | Encontros do aluno logado (aceita `?from=YYYY-MM-DD`) |
| GET | `/api/perfil` | Sim | Perfil do aluno logado |

Exemplos com `curl` usando a URL de produção:

```bash
# Health check
curl https://nextgeneration-seven.vercel.app/health

# Cadastro
curl -X POST https://nextgeneration-seven.vercel.app/api/auth/cadastro \
  -H "Content-Type: application/json" \
  -d '{ "nome": "Aluno Teste", "email": "aluno@teste.com", "senha": "senha-do-aluno" }'

# Login
curl -X POST https://nextgeneration-seven.vercel.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "email": "aluno@teste.com", "senha": "senha-do-aluno" }'

# Agenda do aluno logado (substitua $ACCESS_TOKEN pelo token retornado no login)
curl "https://nextgeneration-seven.vercel.app/api/agenda?from=2026-03-01" \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

Mais exemplos, incluindo os demais endpoints autenticados, estão em
[`src/Entrega1/Backend/README.md`](src/Entrega1/Backend/README.md).

## APK / build do app

O APK de debug desta entrega está versionado em:
[`documentos/Entrega 1/Disciplina Programação de Dispositivos Móveis/app-debug.apk`](<documentos/Entrega%201/Disciplina%20Programa%C3%A7%C3%A3o%20de%20Dispositivos%20M%C3%B3veis/app-debug.apk>)

## Documentação das entregas

- [`documentos/README.md`](documentos/README.md) — índice dos documentos
  entregues por disciplina
- [`documentos/Entrega 1/`](documentos/Entrega%201) — Disciplinas 1 a 4
- [`documentos/Entrega 2/`](documentos/Entrega%202) — Disciplinas 1 a 4

## Privacidade e LGPD

Todos os dados de aluno usados em demonstrações, testes e capturas de tela
deste projeto são **fictícios ou anonimizados**. Nenhum dado pessoal real
de aluno da ONG Próxima Etapa é utilizado neste repositório ou em suas
apresentações.
