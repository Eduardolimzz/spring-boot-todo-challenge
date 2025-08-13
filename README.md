# To-Do List com Spring Boot

API RESTful para gerenciamento de tarefas desenvolvida em Java com Spring Boot.

## Tecnologias

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Validation**
- **MySQL** / **H2** (testes)
- **Swagger/OpenAPI**
- **Docker & Docker Compose**
- **Maven**

##  Execução 

### Com Docker
```bash
git clone [seu-repositorio]
cd desafio-todolist
docker-compose up -d
```

### Local
```bash
# Configure MySQL e crie o banco 'todolist'
./mvnw spring-boot:run
```

**API:** http://localhost:8080  
**Swagger:** http://localhost:8080/swagger-ui.html

###  Funcionalidades
- Criar tarefa com título, descrição, data de vencimento, status e prioridade
- Listar tarefas com filtros por status, prioridade e vencimento
- Atualizar status da tarefa
- Deletar tarefa
- Impedir conclusão de tarefa com subtarefas pendentes

### Funcionalidades Opcionais Implementadas 
- Validação com @Valid e mensagens claras
- Documentação com Swagger/OpenAPI
- Testes unitários e de integração
- Docker Compose com banco de dados
- Paginação e ordenação
- Sistema de subtarefas
- Tratamento global de exceções

##  Regras de Negócio

1. **Conclusão de Tarefas**: Uma tarefa só pode ser marcada como concluída se todas suas subtarefas estiverem concluídas
2. **Exclusão de Tarefas**: Não é possível excluir uma tarefa que possui subtarefas
3. **Validações**: 
   - Nome é obrigatório (1-100 caracteres)
   - Prioridade obrigatória (1-5)
   - Descrição opcional (máx 500 caracteres)
4. **Ordenação padrão**: Por prioridade (desc) e depois por nome (asc)

## 🛠 Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/todos` | Criar tarefa |
| `GET` | `/todos` | Listar com filtros |
| `GET` | `/todos/all` | Listar todas sem paginação |
| `GET` | `/todos/{id}` | Buscar por ID |
| `PUT` | `/todos/{id}` | Atualizar tarefa |
| `PATCH` | `/todos/{id}/status` | Atualizar status |
| `DELETE` | `/todos/{id}` | Deletar tarefa |
| `POST` | `/todos/{id}/subtarefas` | Criar subtarefa |
| `GET` | `/todos/{id}/subtarefas` | Listar subtarefas |
| `GET` | `/todos/vencidas` | Tarefas vencidas |
| `GET` | `/todos/vencimento-proximo` | Vencimento próximo |
| `GET` | `/todos/status/{realizado}` | Filtrar por status |
| `GET` | `/todos/prioridade/{prioridade}` | Filtrar por prioridade |

## Exemplos de Uso

### Criar tarefa
```bash
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Estudar Spring Boot",
    "descricao": "Revisar conceitos de JPA",
    "prioridade": 3,
    "dataVencimento": "2025-08-20T10:00:00"
  }'
```

### Listar com filtros
```bash
# Todas as tarefas
curl "http://localhost:8080/todos?page=0&size=10"

# Filtrar por status
curl "http://localhost:8080/todos?realizado=false"

# Filtrar por prioridade
curl "http://localhost:8080/todos/prioridade/5"

# Tarefas vencidas
curl "http://localhost:8080/todos/vencidas"

# Vencimento próximo (7 dias)
curl "http://localhost:8080/todos/vencimento-proximo?dias=7"
```

### Atualizar status
```bash
curl -X PATCH "http://localhost:8080/todos/1/status?realizado=true"
```

##  Estrutura do Projeto

```
src/
├── main/
│   ├── java/br/com/todo/desafio_todolist/
│   │   ├── config/           # Configurações (Swagger)
│   │   ├── controller/       # Controllers REST
│   │   ├── dto/             # DTOs para requests
│   │   ├── entity/          # Entidades JPA
│   │   ├── exception/       # Exceções customizadas
│   │   ├── repository/      # Repositories JPA
│   │   ├── service/         # Lógica de negócio
│   │   └── DesafioTodolistApplication.java
│   └── resources/
│           ├── templates/
│              ├──  application.properties
│       ├── application-test.properties
│       └── application-docker.properties
└── test/
    └── java/                # Testes unitários e integração
```


##  Modelo de Dados

**Entidade Todo:**
- `id`: Long (PK, auto-increment)
- `nome`: String (obrigatório, 1-100 caracteres)
- `descricao`: String (opcional, máx 500 caracteres)  
- `realizado`: Boolean (padrão: false)
- `prioridade`: Integer (1-5, obrigatório)
- `dataVencimento`: LocalDateTime (opcional)
- `dataCriacao`: LocalDateTime (auto-preenchido)
- `dataAtualizacao`: LocalDateTime (auto-preenchido)
- `tarefaPaiId`: Long (FK, para subtarefas)

##  Testes

```bash
# Executar todos os testes
./mvnw test

# Executar com profile de teste
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

**Cobertura de testes:**
- Testes de integração com WebTestClient
- Validação de criação de tarefas
- Cenários de falha e validação
- Banco H2 em memória para testes

## 🐳 Docker

```bash
# Subir aplicação completa
docker-compose up -d

# Ver logs da aplicação
docker-compose logs -f todolist-app

# Ver logs do MySQL
docker-compose logs -f todolist-mysql

# Parar serviços
docker-compose down

# Rebuild e restart
docker-compose up --build -d
```

**Serviços:**
- `todolist-app` - API Spring Boot (porta 8080)
- `todolist-mysql` - MySQL 8.0 (porta 3306)
- Volume `mysql_data` para persistência

##  Configuração

**Profiles disponíveis:**
- `default` - MySQL local (porta 3306)
- `docker` - MySQL em container  
- `test` - H2 em memória

**Credenciais Docker:**
- Database: `todolist_db`
- User: `todouser`
- Password: `todopassword`
- Root Password: `rootpassword`

**Variáveis de ambiente:**
```bash
SPRING_PROFILES_ACTIVE=docker
SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/todolist_db
SPRING_DATASOURCE_USERNAME=todouser
SPRING_DATASOURCE_PASSWORD=todopassword
```

##  Filtros e Paginação

```bash
# Parâmetros disponíveis
?realizado=true/false          # Filtrar por status
?prioridade=1-5               # Filtrar por prioridade
?dataInicio=2025-08-01T00:00:00  # Data inicial
?dataFim=2025-08-31T23:59:59     # Data final
?page=0                       # Número da página
?size=10                      # Itens por página
?sortBy=prioridade           # Campo de ordenação
?sortDir=desc                # Direção (asc/desc)
```

##  Tratamento de Erros

A API retorna respostas estruturadas para diferentes tipos de erro:

**400 - Validation Failed**
```json
{
  "status": 400,
  "error": "Validation Failed", 
  "message": "Dados inválidos fornecidos",
  "path": "/todos",
  "details": ["Nome é obrigatório", "Prioridade deve ser no mínimo 1"]
}
```

**404 - Resource Not Found**
```json
{
  "status": 404,
  "error": "Resource Not Found",
  "message": "Todo não encontrado com id: 999",
  "path": "/todos/999"
}
```

**400 - Business Rule Violation** 
```json
{
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Não é possível concluir a tarefa. Existem 2 subtarefa(s) pendente(s).",
  "path": "/todos/1/status"
}
```
##  Contribuição

1. Fork o projeto
2. Crie uma feature branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## Autor

**Eduardo Lima dos Santos**
- GitHub: [@Eduardolimzz]([https://github.com/seuusuario](https://github.com/Eduardolimzz))
- LinkedIn: [Eduardo Lima dos Santos](https://www.linkedin.com/in/eduardo-lima-dos-santos-3b1092316)
- Email: eduardoaluno1800@gmail.com
