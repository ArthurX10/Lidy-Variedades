# Lidy Variedades 🛍️

Sistema de gestão para a loja **Lidy Variedades**, composto por:

| Módulo | Diretório | Tecnologia |
|--------|-----------|------------|
| API / Backend | `backend/` | Spring Boot 4 + Java 21 + PostgreSQL (Neon) |
| Loja Web | `AplicacaoWeb/` | React + Vite |
| Aplicativo Desktop | `desktop/` | JavaFX 21 + Java 21 |

## Pré-requisitos

- **Java 21** (JDK)
- **Maven 3.9+** (ou use o wrapper `mvnw` incluído)
- **PostgreSQL** (banco hospedado no [Neon](https://neon.tech))

Configure `JAVA_HOME` para apontar para o JDK 21. No Windows, reinicie o terminal
ou a IDE depois de alterar essa variável.

## Publicação da API e da loja

A API é hospedada no Render (`https://lidy-variedades.onrender.com`) e a loja pode
ser publicada na Vercel. Nas variáveis de ambiente do projeto frontend na Vercel,
configure `VITE_API_URL` com a URL base da API. No serviço da API no Render,
configure `APP_CORS_ALLOWED_ORIGIN` com a origem exata da loja publicada
(por exemplo, `https://seu-projeto.vercel.app`, sem barra no final). Se também
precisar liberar a loja local durante o desenvolvimento, informe as origens
separadas por vírgula.

## Configuração

### 1. Variáveis de ambiente (Backend)

Crie um arquivo `backend/.env` ou configure as variáveis no sistema:

```
DB_URL=jdbc:postgresql://<host>/<database>?sslmode=require
DB_USERNAME=<usuario>
DB_PASSWORD=<senha>
ADMIN_EMAIL=<email-do-administrador>
ADMIN_PASSWORD=<senha-forte-do-administrador>
APP_CORS_ALLOWED_ORIGIN=http://localhost:5173
```

As credenciais administrativas são obrigatórias para iniciar o backend e não devem
ser versionadas. O backend usa uma sessão protegida por CSRF; as operações de
criação, edição e exclusão de produtos exigem autenticação. Para produção, defina
`APP_CORS_ALLOWED_ORIGIN` com a origem exata do frontend e sirva a API por HTTPS.
O desktop também precisa receber `ADMIN_EMAIL` e `ADMIN_PASSWORD` no ambiente do
processo; configure essas variáveis na execução/IDE do desktop. Ele usa um cabeçalho
de autenticação próprio, não habilitado para o navegador.

Para criar três produtos fictícios de teste, configure `APP_SEED_SAMPLE_PRODUCTS=true`
no ambiente do backend e reinicie a API. A carga só acontece quando ainda não há
nenhum produto cadastrado; deixe a opção desativada em produção.

### 2. Executar o Backend

No Render, use o runtime **Docker**, deixe o campo **Root Directory** vazio (raiz
do repositório) e defina o caminho do Dockerfile como `backend/Dockerfile`. O
contexto de build precisa ser a raiz do repositório, pois o Dockerfile copia os
arquivos de `backend/`.

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

O servidor inicia em `http://localhost:8080`.

### 3. Executar a loja web

Com o backend e o banco configurados, execute:

```powershell
cd AplicacaoWeb
npm install
$env:VITE_API_URL = "http://localhost:8080"
npm run dev
```

Para executar o backend localmente com o frontend, defina também
`APP_CORS_ALLOWED_ORIGIN=http://localhost:5173` e `APP_COOKIE_SECURE=false` no
ambiente do backend.

Sem `VITE_API_URL`, o frontend usa a API de produção do Render. Para publicar a
loja na Vercel, configure essa variável no painel da Vercel com
`https://lidy-variedades.onrender.com`.

O gerenciamento de produtos fica em `/PaginaAdmin` e `/admin/produtos`. Os
produtos cadastrados no banco alimentam também as páginas públicas da loja.
Os produtos de demonstração antigos não são importados automaticamente; cadastre
os produtos reais pela área administrativa.

### 4. Executar o Desktop

```bash
cd desktop
.\mvnw.cmd javafx:run
```

O desktop usa `https://lidy-variedades.onrender.com` por padrão. Para apontá-lo a
um backend local, configure `LIDY_API_URL` antes de iniciá-lo:

```powershell
$env:LIDY_API_URL = "http://localhost:8080"
```

Quando `LIDY_API_URL` aponta para um endereço local, o launcher ainda tenta iniciar
o backend empacotado se ele não estiver disponível.

## Estrutura do Projeto

```
LidyVariedades/
├── backend/          # API REST (Spring Boot)
│   ├── src/
│   └── pom.xml
├── desktop/          # Aplicativo Desktop (JavaFX)
│   ├── src/
│   └── pom.xml
├── .gitignore
└── README.md
```

## Licença

Projeto privado — todos os direitos reservados.
