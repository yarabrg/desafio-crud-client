# CRUD de Clientes — API REST com Spring Boot

API REST desenvolvida em Java e Spring Boot para gerenciamento de clientes, implementando as cinco operações fundamentais de um CRUD: consulta, listagem paginada, inserção, atualização e exclusão de registros.

O projeto foi desenvolvido como parte de um desafio prático de backend, com foco na aplicação de conceitos de desenvolvimento de APIs REST, arquitetura em camadas, persistência de dados, validação de entradas e tratamento de exceções.

## Tecnologias utilizadas

- **Java 25** — Linguagem de programação.
- **Spring Boot 4.1.2** — Framework para desenvolvimento da aplicação.
- **Spring Data JPA** — Persistência e acesso aos dados.
- **Hibernate** — Implementação do JPA.
- **Jakarta Validation** — Validação dos dados de entrada.
- **H2 Database** — Banco de dados em memória.
- **Maven** — Gerenciamento de dependências e build.

## Funcionalidades

A API disponibiliza as seguintes operações:

- **Busca por ID:** consulta um cliente específico pelo identificador.
- **Listagem paginada:** retorna clientes com suporte a paginação e ordenação.
- **Inserção:** cadastra novos clientes, validando os dados recebidos.
- **Atualização:** modifica os dados de um cliente existente.
- **Exclusão:** remove um cliente pelo identificador.

### Endpoints

| Método | Endpoint | Descrição | Resposta de sucesso |
|---|---|---|---|
| GET | `/clients/{id}` | Busca cliente por ID | 200 OK |
| GET | `/clients` | Lista clientes com paginação | 200 OK |
| POST | `/clients` | Cadastra um novo cliente | 201 Created |
| PUT | `/clients/{id}` | Atualiza um cliente existente | 200 OK |
| DELETE | `/clients/{id}` | Exclui um cliente | 204 No Content |

### Paginação e ordenação

A listagem de clientes utiliza os recursos de paginação do Spring Data, permitindo controlar o número de registros por página e o critério de ordenação.

Exemplo:

```http
GET /clients?page=0&size=6&sort=name
```

Nesse exemplo:
- `page=0`: primeira página.
- `size=6`: seis registros por página.
- `sort=name`: ordenação pelo nome do cliente.

## Modelo de dados

Cada cliente possui os seguintes atributos:

| Atributo | Tipo | Descrição |
|---|---|---|
| `id` | Long | Identificador do cliente |
| `name` | String | Nome |
| `cpf` | String | CPF (valor único) |
| `income` | Double | Renda |
| `birthDate` | LocalDate | Data de nascimento |
| `children` | Integer | Quantidade de filhos |

O CPF possui restrição de unicidade no banco de dados.

## Validações e tratamento de exceções

A aplicação utiliza Jakarta Validation para validar os dados recebidos e possui tratamento customizado de exceções, centralizado na classe `ControllerExceptionHandler`.

As regras de validação implementadas são:

- **Nome:** não pode ser vazio ou conter apenas espaços em branco (`@NotBlank`).
- **Data de nascimento:** não pode ser uma data futura (`@PastOrPresent`).

Além das validações, a aplicação trata os seguintes cenários:

| Situação | Código HTTP | Descrição |
|---|---|---|
| Recurso encontrado | 200 | Operação de consulta ou atualização realizada com sucesso |
| Recurso criado | 201 | Cliente cadastrado com sucesso |
| Recurso excluído | 204 | Exclusão realizada sem conteúdo na resposta |
| Recurso não encontrado | 404 | ID informado não corresponde a um cliente existente |
| Dados inválidos | 422 | Requisição apresenta erros de validação |
| Conflito de dados | 409 | Violação de integridade, como CPF duplicado |

As respostas de erro são personalizadas e incluem informações como data e hora, código HTTP, mensagem e caminho da requisição. Nos erros de validação, também são informados os campos que apresentam problemas e suas respectivas mensagens.

## Carga inicial de dados (Seed)

Ao iniciar a aplicação, o banco H2 é populado automaticamente com uma massa de dados contendo clientes com informações significativas.

Os registros foram inspirados em personagens de universos de ficção e cultura pop, incluindo Cyberpunk 2077, Arcane, Signalis e Mass Effect.

Essa carga inicial facilita a exploração dos endpoints e permite testar a paginação, as consultas e as operações de atualização e exclusão sem precisar cadastrar todos os registros manualmente.

## Estrutura do projeto

O projeto utiliza arquitetura em camadas, separando as responsabilidades de cada componente:

```text
src/main/java/com/mooncake/crud_clients
│
├── controllers
│   ├── ClientController.java
│   └── handlers
│       └── ControllerExceptionHandler.java
│
├── dto
│   ├── ClientDTO.java
│   ├── CustomError.java
│   ├── FieldMessage.java
│   └── ValidationError.java
│
├── entities
│   └── Client.java
│
├── repositories
│   └── ClientRepository.java
│
└── services
    ├── ClientService.java
    └── exceptions
        ├── DatabaseException.java
        └── ResourceNotFoundException.java
```

**Responsabilidades das camadas:**

- **Controllers:** recebem as requisições HTTP e retornam as respostas da API.
- **Services:** concentram a lógica de negócio e as operações de gerenciamento dos clientes.
- **Repositories:** realizam o acesso e a persistência dos dados utilizando Spring Data JPA.
- **Entities:** representam as entidades persistidas no banco de dados.
- **DTOs:** definem os objetos utilizados na transferência de dados entre a API e os clientes.
- **Handlers:** centralizam o tratamento das exceções e a construção das respostas de erro.

## Como executar o projeto

### Pré-requisitos

- Java JDK 25.
- Maven.
- Uma IDE compatível com projetos Java e Maven (IntelliJ IDEA, Eclipse, Spring Tool Suite etc.).

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/nome-do-repositorio.git
```

### 2. Importe o projeto

Abra o projeto na IDE de sua preferência e aguarde a resolução das dependências Maven.

### 3. Execute a aplicação

Localize e execute a classe principal:

```text
CrudClientsApplication.java
```

A API estará disponível em:

```text
http://localhost:8080
```

### 4. Acesse o banco de dados H2

O console do banco pode ser acessado pelo endereço:

```text
http://localhost:8080/h2-console
```

O H2 é utilizado em modo *in-memory*, portanto os dados são mantidos durante a execução da aplicação e reinicializados quando ela é encerrada.

## Como testar a API

O projeto disponibiliza uma collection do Postman com as requisições configuradas para testar os endpoints.

1. Acesse a pasta `postman` do repositório.
2. Importe o arquivo `crud-clients.postman_collection.json` no Postman.
3. Certifique-se de que a aplicação esteja em execução.
4. Execute as requisições disponíveis na collection.

### Exemplo de cadastro de cliente

**POST** `/clients`

Corpo da requisição:

```json
{
    "name": "Mooncake",
    "cpf": "12345678901",
    "income": 6500.0,
    "birthDate": "1994-07-20",
    "children": 2
}
```

### Exemplo de atualização

**PUT** `/clients/1`

```json
{
    "name": "Mooncake",
    "cpf": "12345678901",
    "income": 7000.0,
    "birthDate": "1994-07-20",
    "children": 2
}
```

### Exemplo de consulta paginada

**GET** `/clients?page=0&size=6&sort=name`

## Objetivo do projeto

Este projeto foi desenvolvido para consolidar conhecimentos práticos em desenvolvimento backend com Java e Spring Boot, explorando conceitos fundamentais de construção de APIs REST, organização de código, persistência de dados e tratamento de erros.
