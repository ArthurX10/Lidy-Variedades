# Lidy Variedades 🛍️

Sistema de gestão para a loja **Lidy Variedades**, composto por:

| Módulo | Diretório | Tecnologia |
|--------|-----------|------------|
| API / Backend | `backend/` | Spring Boot 4 + Java 26 + PostgreSQL (Neon) |
| Aplicativo Desktop | `desktop/` | JavaFX 24 + Java 26 |

## Pré-requisitos

- **Java 26+** (JDK)
- **Maven 3.9+** (ou use o wrapper `mvnw` incluído)
- **PostgreSQL** (banco hospedado no [Neon](https://neon.tech))

## Configuração

### 1. Variáveis de ambiente (Backend)

Crie um arquivo `backend/.env` ou configure as variáveis no sistema:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>/<database>?sslmode=require
SPRING_DATASOURCE_USERNAME=<usuario>
SPRING_DATASOURCE_PASSWORD=<senha>
```

### 2. Executar o Backend

```bash
cd backend
./mvnw spring-boot:run
```

O servidor inicia em `http://localhost:8080`.

### 3. Executar o Desktop

```bash
cd desktop
./mvnw javafx:run
```

> O app Desktop espera que o backend esteja rodando na porta 8080.

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
