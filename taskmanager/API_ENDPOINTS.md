# API Task Manager

Base URL padrão: `http://localhost:8080`

## Autenticação
- Público:
  - `POST /auth/login`
  - `POST /auth/refresh`
  - `POST /user`
- Protegido:
  - Dependendo da rota, exige-se permissões específicas (Roles: `ADMIN`, `MANAGER`, `MEMBER`, `MEMBER_MANAGER`).
  - Demais endpoints → autenticados via Bearer token

## Formato de resposta
### Endpoints com payload
```json
{
  "status": 200,
  "id": "uuid",
  "data": {},
  "message": "Mensagem"
}
```

### Endpoints paginados
Padrão de resposta utilizando o `PagedModel` do Spring.

### Erro
Tratamento padrão do Spring/ExceptionsHandler.

---

# 1. Auth

## POST /auth/login
Autenticação na plataforma.

### Permissão
Público

### Body
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

## POST /auth/refresh
Renova a sessão com base no refresh token.

### Permissão
Público

### Body
```json
{
  "refreshToken": "jwt"
}
```

---

# 2. Usuário (User)

## POST /user
Cria um usuário.

### Permissão
Público

### Body
```json
{
  "name": "Nome do Usuário",
  "email": "user@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```

## GET /user
Lista todos os usuários.

### Permissão
`ADMIN`

## GET /user/{id}
Busca usuário por ID.

### Permissão
Autenticado

## PATCH /user/change/{id}/name
Altera o nome do usuário.

### Permissão
Autenticado

## PATCH /user/change/{id}/role
Altera a role do usuário.

### Permissão
`ADMIN`

## PATCH /user/change/{id}/entitystatus
Altera o status da entidade do usuário (ex: ACTIVE, INACTIVE).

### Permissão
Autenticado

---

# 3. Projeto (Project)

## POST /project
Cria um projeto.

### Permissão
`ADMIN`, `MANAGER`

### Body
```json
{
  "name": "Nome do Projeto",
  "description": "Descrição detalhada do projeto",
  "creatorId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "members": [
    "3fa85f64-5717-4562-b3fc-2c963f66afa6"
  ]
}
```

## GET /project
Lista todos os projetos com paginação.

### Permissão
`ADMIN`

## GET /project/{id}
Busca projeto por ID.

### Permissão
Autenticado

## GET /project/creator/{creatorId}
Lista projetos pelo ID do criador.

### Permissão
`ADMIN`, `MANAGER`

## GET /project/member/{memberId}
Lista projetos pelo ID do membro.

### Permissão
`ADMIN`, `MEMBER`, `MEMBER_MANAGER`

## PUT /project/{id}
Atualiza os dados de um projeto.

### Permissão
`ADMIN`, `MANAGER`

### Body
```json
{
  "name": "Nome do Projeto Atualizado",
  "description": "Descrição atualizada do projeto"
}
```

---

# 4. Membros do Projeto (Project Members)

## POST /members/add
Adiciona membros a um projeto.

### Permissão
`ADMIN`, `MANAGER`, `MEMBER_MANAGER`

### Body
```json
{
  "projectId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "memberIds": [
    "3fa85f64-5717-4562-b3fc-2c963f66afa6"
  ]
}
```

## GET /members
Lista os vínculos de membros.

### Permissão
`ADMIN`

## GET /members/{id}
Busca um vínculo de membro por ID.

### Permissão
Autenticado

## GET /members/project/{projectId}
Lista todos os membros de um determinado projeto.

### Permissão
Autenticado

## GET /members/member/{memberId}
Lista os projetos vinculados a um determinado membro.

### Permissão
Autenticado

## PATCH /members/change/{id}/status
Altera o status de um membro no projeto.

### Permissão
Autenticado na API REST via `@PatchMapping`. (Obs: Na configuração de segurança do Spring `FilterConfiguration`, consta exigência de roles `ADMIN`, `MANAGER`, `MEMBER_MANAGER` para o método `POST` em `/members/change/*/status`).

---

# 5. Tarefa (Task)

## POST /task
Cria uma tarefa.

### Permissão
Autenticado

### Body
```json
{
  "title": "Título da Tarefa",
  "description": "Descrição da tarefa",
  "status": "PENDING",
  "priority": "HIGH",
  "deadline": "2026-12-31T23:59:59",
  "responsibleId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "projectId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

## GET /task
Lista tarefas com paginação.

### Permissão
Autenticado (via fallback, pois a `FilterConfiguration` mapeia `/tasks` para `ADMIN`, mas o controller está em `/task`).

## GET /task/{id}
Busca tarefa por ID.

### Permissão
Autenticado

## POST /task/filters
Filtra tarefas baseado no body enviado.

### Permissão
Autenticado

### Body
```json
{
  "status": "PENDING",
  "priority": "HIGH",
  "responsibleId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "startDate": "2026-01-01T00:00:00",
  "endDate": "2026-12-31T23:59:59",
  "searchText": "termo de busca"
}
```

## PUT /task/{id}
Atualiza uma tarefa.

### Permissão
Autenticado

### Body
```json
{
  "title": "Título da Tarefa Atualizado",
  "description": "Nova descrição da tarefa",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "deadline": "2027-01-31T23:59:59"
}
```

## PATCH /task/change/{id}/status
Altera o status de uma tarefa.

### Permissão
Autenticado

## PATCH /task/change/{id}/priority
Altera a prioridade de uma tarefa.

### Permissão
Autenticado

## PATCH /task/change/{id}/responsible/{responsibleId}
Muda o responsável de uma tarefa.

### Permissão
Autenticado

---

# 6. Relatórios de Projeto (Project Report)

## GET /projects/{projectId}/report
Gera relatório do projeto (DTO).

### Permissão
Autenticado

---

# Resumo rápido das regras principais
- `POST /user`, `POST /auth/login` e `POST /auth/refresh` são **públicos**.
- Endpoints de listagem geral como `GET /user`, `GET /project`, `GET /members` e `PATCH /user/change/*/role` exigem a role `ADMIN`.
- A criação e atualização de projetos (`POST /project`, `PUT /project/*`) e listagem por criador exige `ADMIN` ou `MANAGER`.
- Consultas a projetos por membro exigem `ADMIN`, `MEMBER` ou `MEMBER_MANAGER`.
- Adicionar membros (`POST /members/add`) exige `ADMIN`, `MANAGER` ou `MEMBER_MANAGER`.
- A grande maioria dos endpoints específicos (por ID, listagem de filhos, patchs pontuais e todas as rotas de Task e Relatórios) exige apenas que o usuário esteja **autenticado** via token JWT, caindo no `.anyRequest().authenticated()` da cadeia de filtros do Spring Security.
