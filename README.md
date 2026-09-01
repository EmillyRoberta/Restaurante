# Restaurante
Backend de uma plataforma de gestão de restaurantes: API REST em Java 21 /
Spring Boot 4 com PostgreSQL, Flyway, Docker e documentação das API com Swagger UI.

Projeto desenvolvido como Tech Challenge da Fase 1 da Pós-Graduação em Arquitetura e
Desenvolvimento Java (FIAP).

## Sumário

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Execução com Docker](#execução-com-docker)
- [Execução local (sem Docker)](#execução-local-sem-docker)
- [Perfis de configuração](#perfis-de-configuração)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Endpoints principais](#endpoints-principais)
- [Banco de dados e migrações](#banco-de-dados-e-migrações)
- [Tratamento de erros](#tratamento-de-erros)
- [Postman](#postman)
- [Documentação da API (Swagger)](#documentação-da-api-swagger)
- [Autores](#autores)

## Tecnologias

| Camada | Tecnologia |
|--------|------------|
| Linguagem / build | Java 21, Maven (wrapper incluído) |
| Framework | Spring Boot 4.1.1 — Web, Data JPA, Validation, Security |
| Banco de dados | PostgreSQL 18 (H2 em memória nos testes) |
| Migrações de schema | Flyway |
| Documentação da API | springdoc-openapi / Swagger UI |
| Utilitários | Lombok, Apache Commons Validator |
| Empacotamento | Docker / Docker Compose |

## Pré-requisitos

- **Docker** e **Docker Compose** — forma recomendada de execução; ou
- **JDK 21** e um **PostgreSQL** acessível — para execução local.
- Não é necessário instalar o Maven: utilize o wrapper `./mvnw` (Linux/macOS) ou `mvnw.cmd` (Windows).

## Execução com Docker

1. Clonar o projeto do GitHub:
```
git clone https://github.com/EmillyRoberta/Restaurante restaurante
```

2. Navegar até a raiz da pasta do projeto:
```
cd restaurante
```

3. Preencher os valores no arquivo .env
> [!NOTE]
> Caso ainda não exista, renomeie o arquivo `docker/.env.example` (com valores do ambiente de
> develop) para `docker/.env`.

```
mv docker/.env.example docker/.env
```

4. Subir o DB e a aplicação com o Docker Compose:
```
docker compose -f docker/docker-compose.yml up
```

A aplicação sobe em `http://localhost:8080` e o PostgreSQL em `localhost:5432`. A aplicação
usa o perfil `docker` (variável `SPRING_PROFILES_ACTIVE=docker`) e as migrações do Flyway são
aplicadas automaticamente na inicialização.

> [!NOTE]
> Para construir/executar **apenas a aplicação** (imagem isolada), rode a partir da raiz do
> projeto:
> ```
> docker build -t restaurante -f docker/application/Dockerfile .
> docker run --rm -p 8080:8080 \
>   -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/restaurante \
>   restaurante
> ```
> O contêiner continua precisando de um PostgreSQL acessível (ajuste `SPRING_DATASOURCE_URL`,
> `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` conforme o seu ambiente).

## Execução local (sem Docker)

1. Suba um PostgreSQL na porta `5432` com database `restaurante`, usuário `admin` e senha
   `123456` (valores definidos em `src/main/resources/application.properties`). Exemplo rápido
   com Docker:
```
docker run --name restaurante-db -p 5432:5432 \
  -e POSTGRES_DB=restaurante -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123456 \
  -d postgres:18-alpine
```

2. Executar a aplicação com o perfil padrão:
```
./mvnw spring-boot:run
```

3. Alternativamente, gerar e executar o JAR:
```
./mvnw clean package
java -jar target/restaurante-0.0.1-SNAPSHOT.jar
```

## Perfis de configuração

| Perfil | Arquivo | Uso |
|--------|---------|-----|
| `default` | `application.properties` | Execução local; datasource em `localhost:5432` |
| `docker` | `application-docker.properties` | Docker Compose; datasource no host `db` |
| `test` | `src/test/resources/application-test.properties` | Testes automatizados; banco H2 em memória |

O `spring.jpa.hibernate.ddl-auto` está em `validate`: o schema é gerido exclusivamente pelas
migrações do Flyway, e o Hibernate apenas valida o mapeamento.

## Estrutura do projeto

```
src/main/java
├── br/com/fiap/restaurante/restaurante
│   ├── config           # SecurityConfig (BCrypt), OpenApiConfig
│   ├── controllers      # UserController, RestaurantController, AuthController
│   │   └── handlers     # GlobalExceptionHandler (RFC 7807)
│   ├── entities         # User, Restaurant, Address (Value Object), UserType
│   ├── repositories     # Interfaces Spring Data JPA
│   └── services         # UserService, RestaurantService, AuthService
└── dtos                 # Records de request/response
src/main/resources
├── db/migration         # Migrações Flyway (V1__create_tables.sql)
└── application*.properties
docker                   # Dockerfile, docker-compose.yml e .env
postman                  # Coleção de requisições
```

Arquitetura em camadas com dependência unidirecional
`controller → service → repository → banco de dados`; as regras de negócio residem nos
*services* e nas entidades.

## Endpoints principais

URL base: `http://localhost:8080` — prefixo de versão `/v1`. Formato `application/json`.

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `POST` | `/v1/users/save` | Cadastra usuário (e-mail e login únicos) |
| `PUT` | `/v1/users/{id}` | Atualiza dados cadastrais |
| `DELETE` | `/v1/users/{id}` | Exclui usuário (bloqueado se possui restaurantes) |
| `GET` | `/v1/users` | Lista usuários (paginado: `page`, `size`) |
| `GET` | `/v1/users/{id}` | Consulta usuário por identificador |
| `GET` | `/v1/users/search?name=` | Pesquisa usuários por nome |
| `POST` | `/v1/auth/login` | Autentica por login e senha |
| `PUT` | `/v1/auth/change-password` | Troca a senha |
| `POST` | `/v1/restaurants` | Cadastra restaurante (proprietário deve ser `RESTAURANT_OWNER`) |
| `PUT` | `/v1/restaurants/{id}` | Atualiza restaurante |
| `DELETE` | `/v1/restaurants/{id}` | Exclui restaurante |
| `GET` | `/v1/restaurants` | Lista restaurantes (paginado: `page`, `size`) |
| `GET` | `/v1/restaurants/{id}` | Consulta restaurante por identificador |

Nesta fase não há autenticação obrigatória nos endpoints; apenas o fluxo de login valida
credenciais. As senhas são armazenadas com hash BCrypt.

## Banco de dados e migrações

- Tabela `users` — dados de identificação, autenticação e as colunas do endereço embutidas
  (`street`, `number`, `city`, `state`, `zip_code`); `email` e `login` únicos.
- Tabela `restaurants` — `name`, `description` e `owner_id` (FK obrigatória para `users.id`,
  constraint `fk_restaurant_owner`).
- As migrações ficam em `src/main/resources/db/migration` e são aplicadas pelo Flyway na
  inicialização da aplicação.

## Tratamento de erros

As respostas de erro seguem o padrão **RFC 7807** (`application/problem+json`), com a
propriedade `TimeStamp` e, nos erros de validação, o mapa `invalid_fields` (campo → mensagem).

| Situação | Status |
|----------|--------|
| Falha de validação ou campo duplicado (e-mail/login) | `400` |
| Login inexistente ou senha incorreta | `401` |
| Usuário, restaurante ou proprietário inexistente | `404` |
| Regra de negócio violada (e-mail inválido, proprietário inválido, exclusão bloqueada) | `422` |

## Postman
As requisições estão em [Postman Request Collection](postman/postman-request_collection.json) 
(`postman/postman-request_collection.json`).</br>
Há requisições para CRUD e validações das entidades do projeto que funcionam de forma independente. 
Apenas é necessário executá-las na ordem dentro da mesma pasta, que estão organizadas por cenários.

### Configurando as variáveis globais
Há duas variáveis globais, a `host` e a `port`, popule-as para configurar a URL corretamente.
A porta padrão configurada na aplicação é a `8080`.

## Documentação da API (Swagger)
Acesse o path "my-docs" para visualizar a documentação da API. 
```
Exemplo: http://localhost:8080/my-docs
```

A especificação JSON (OpenAPI 3) está disponível no path "api-docs".
```
Exemplo: http://localhost:8080/api-docs
```

## Autores

- Emilly Roberta da Silva
- Julian Brasil Nichikuma
