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
  "id": "uuid",
  "message": "Mensagem",
  "data": {}
}
```

### Endpoints paginados
Padrão de resposta utilizando o `PagedModel` do Spring.
```json
{
  "content": [
    {}
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

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

### Retorno
```json
{
  "token": "jwt_token",
  "refreshToken": "jwt_refresh_token"
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

### Retorno
```json
{
  "token": "jwt_token",
  "refreshToken": "jwt_refresh_token"
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

### Retorno
```json
{
  "id": "uuid",
  "message": "Usuário criado com sucesso",
  "data": {
    "user": {
      "id": "uuid",
      "name": "Nome do Usuário",
      "email": "user@example.com",
      "role": "ADMIN",
      "entityStatus": "ACTIVE"
    },
    "tokenResponse": {
      "token": "jwt_token",
      "refreshToken": "jwt_refresh_token"
    }
  }
}
```

## GET /user
Lista todos os usuários.

### Permissão
`ADMIN`

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "name": "Nome do Usuário",
      "email": "user@example.com",
      "role": "ADMIN",
      "entityStatus": "ACTIVE"
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## GET /user/{id}
Busca usuário por ID.

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Usuário encontrado",
  "data": {
    "id": "uuid",
    "name": "Nome do Usuário",
    "email": "user@example.com",
    "role": "ADMIN",
    "entityStatus": "ACTIVE"
  }
}
```

## GET /user/email/{email}
Busca usuário por e-mail.

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Usuário encontrado",
  "data": {
    "id": "uuid",
    "name": "Nome do Usuário",
    "email": "user@example.com",
    "role": "ADMIN",
    "entityStatus": "ACTIVE"
  }
}
```

## PATCH /user/change/{id}/name
Altera o nome do usuário.

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Nome do usuário atualizado",
  "data": "Novo Nome do Usuário"
}
```

## PATCH /user/change/{id}/role
Altera a role do usuário.

### Permissão
`ADMIN`

### Retorno
```json
{
  "id": "uuid",
  "message": "Role do usuário atualizada",
  "data": "MANAGER"
}
```

## PATCH /user/change/{id}/entitystatus
Altera o status da entidade do usuário (ex: ACTIVE, INACTIVE).

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Status da entidade atualizado",
  "data": "INACTIVE"
}
```

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
  "creatorId": "uuid",
  "members": [
    "uuid"
  ]
}
```

### Retorno
```json
{
  "id": "uuid",
  "message": "Projeto criado com sucesso",
  "data": {
    "id": "uuid",
    "name": "Nome do Projeto",
    "description": "Descrição detalhada do projeto",
    "creatorId": "uuid",
    "members": [
      {
        "id": "uuid",
        "projectId": "uuid",
        "userId": "uuid",
        "userProjectStatus": "ACTIVE"
      }
    ]
  }
}
```

## GET /project
Lista todos os projetos com paginação.

### Permissão
`ADMIN`

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "name": "Nome do Projeto",
      "description": "Descrição do projeto",
      "creatorId": "uuid",
      "members": [
        {
          "id": "uuid",
          "projectId": "uuid",
          "userId": "uuid",
          "userProjectStatus": "ACTIVE"
        }
      ]
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## GET /project/{id}
Busca projeto por ID.

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Projeto encontrado",
  "data": {
    "id": "uuid",
    "name": "Nome do Projeto",
    "description": "Descrição do projeto",
    "creatorId": "uuid",
    "members": [
      {
        "id": "uuid",
        "projectId": "uuid",
        "userId": "uuid",
        "userProjectStatus": "ACTIVE"
      }
    ]
  }
}
```

## GET /project/creator/{creatorId}
Lista projetos pelo ID do criador.

### Permissão
`ADMIN`, `MANAGER`

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "name": "Nome do Projeto",
      "description": "Descrição do projeto",
      "creatorId": "uuid",
      "members": [
        {
          "id": "uuid",
          "projectId": "uuid",
          "userId": "uuid",
          "userProjectStatus": "ACTIVE"
        }
      ]
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## GET /project/member/{memberId}
Lista projetos pelo ID do membro.

### Permissão
`ADMIN`, `MEMBER`, `MEMBER_MANAGER`

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "name": "Nome do Projeto",
      "description": "Descrição do projeto",
      "creatorId": "uuid",
      "members": [
        {
          "id": "uuid",
          "projectId": "uuid",
          "userId": "uuid",
          "userProjectStatus": "ACTIVE"
        }
      ]
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

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

### Retorno
```json
{
  "id": "uuid",
  "message": "Projeto atualizado",
  "data": {
    "id": "uuid",
    "name": "Nome do Projeto Atualizado",
    "description": "Descrição atualizada do projeto",
    "creatorId": "uuid",
    "members": [
      {
        "id": "uuid",
        "projectId": "uuid",
        "userId": "uuid",
        "userProjectStatus": "ACTIVE"
      }
    ]
  }
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
  "projectId": "uuid",
  "memberIds": [
    "uuid"
  ]
}
```

### Retorno
```json
{
  "id": "uuid",
  "message": "Membros adicionados ao projeto com sucesso",
  "data": {
    "projectId": "uuid",
    "membersAdded": [
      {
        "id": "uuid",
        "projectId": "uuid",
        "userId": "uuid",
        "userProjectStatus": "ACTIVE"
      }
    ]
  }
}
```

## GET /members
Lista os vínculos de membros.

### Permissão
`ADMIN`

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "projectId": "uuid",
      "userId": "uuid",
      "userProjectStatus": "ACTIVE"
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## GET /members/{id}
Busca um vínculo de membro por ID.

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Membro encontrado",
  "data": {
    "id": "uuid",
    "projectId": "uuid",
    "userId": "uuid",
    "userProjectStatus": "ACTIVE"
  }
}
```

## GET /members/project/{projectId}
Lista todos os membros de um determinado projeto.

### Permissão
Autenticado

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "projectId": "uuid",
      "userId": "uuid",
      "userProjectStatus": "ACTIVE"
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## GET /members/member/{memberId}
Lista os projetos vinculados a um determinado membro.

### Permissão
Autenticado

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "projectId": "uuid",
      "userId": "uuid",
      "userProjectStatus": "ACTIVE"
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## PATCH /members/change/{id}/status
Altera o status de um membro no projeto.

### Permissão
Autenticado na API REST via `@PatchMapping`. (Obs: Na configuração de segurança do Spring `FilterConfiguration`, consta exigência de roles `ADMIN`, `MANAGER`, `MEMBER_MANAGER` para o método `POST` em `/members/change/*/status`).

### Retorno
```json
{
  "id": "uuid",
  "message": "Status do membro atualizado",
  "data": "INACTIVE"
}
```

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
  "responsibleId": "uuid",
  "projectId": "uuid"
}
```

### Retorno
```json
{
  "id": "uuid",
  "message": "Tarefa criada com sucesso",
  "data": {
    "id": "uuid",
    "title": "Título da Tarefa",
    "description": "Descrição da tarefa",
    "status": "PENDING",
    "priority": "HIGH",
    "deadline": "2026-12-31T23:59:59",
    "responsibleId": "uuid",
    "projectId": "uuid",
    "entityStatus": "ACTIVE",
    "createdAt": "2026-06-23T15:00:00",
    "updatedAt": "2026-06-23T15:00:00"
  }
}
```

## GET /task
Lista tarefas com paginação.

### Permissão
Autenticado (via fallback, pois a `FilterConfiguration` mapeia `/tasks` para `ADMIN`, mas o controller está em `/task`).

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "title": "Título da Tarefa",
      "description": "Descrição da tarefa",
      "status": "PENDING",
      "priority": "HIGH",
      "deadline": "2026-12-31T23:59:59",
      "responsibleId": "uuid",
      "projectId": "uuid",
      "entityStatus": "ACTIVE",
      "createdAt": "2026-06-23T15:00:00",
      "updatedAt": "2026-06-23T15:00:00"
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## GET /task/{id}
Busca tarefa por ID.

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Tarefa encontrada",
  "data": {
    "id": "uuid",
    "title": "Título da Tarefa",
    "description": "Descrição da tarefa",
    "status": "PENDING",
    "priority": "HIGH",
    "deadline": "2026-12-31T23:59:59",
    "responsibleId": "uuid",
    "projectId": "uuid",
    "entityStatus": "ACTIVE",
    "createdAt": "2026-06-23T15:00:00",
    "updatedAt": "2026-06-23T15:00:00"
  }
}
```

## POST /task/filters
Filtra tarefas baseado no body enviado.

### Permissão
Autenticado

### Body
```json
{
  "status": "PENDING",
  "priority": "HIGH",
  "responsibleId": "uuid",
  "projectId": "uuid",
  "startDate": "2026-01-01T00:00:00",
  "endDate": "2026-12-31T23:59:59",
  "searchText": "termo de busca"
}
```

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "title": "Título da Tarefa",
      "description": "Descrição da tarefa",
      "status": "PENDING",
      "priority": "HIGH",
      "deadline": "2026-12-31T23:59:59",
      "responsibleId": "uuid",
      "projectId": "uuid",
      "entityStatus": "ACTIVE",
      "createdAt": "2026-06-23T15:00:00",
      "updatedAt": "2026-06-23T15:00:00"
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
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

### Retorno
```json
{
  "id": "uuid",
  "message": "Tarefa atualizada",
  "data": {
    "id": "uuid",
    "title": "Título da Tarefa Atualizado",
    "description": "Nova descrição da tarefa",
    "status": "IN_PROGRESS",
    "priority": "MEDIUM",
    "deadline": "2027-01-31T23:59:59",
    "responsibleId": "uuid",
    "projectId": "uuid",
    "entityStatus": "ACTIVE",
    "createdAt": "2026-06-23T15:00:00",
    "updatedAt": "2026-06-23T16:00:00"
  }
}
```

## PATCH /task/change/{id}/status
Altera o status de uma tarefa.

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Status da tarefa atualizado",
  "data": "IN_PROGRESS"
}
```

## PATCH /task/change/{id}/priority
Altera a prioridade de uma tarefa.

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Prioridade da tarefa atualizada",
  "data": "MEDIUM"
}
```

## PATCH /task/change/{id}/responsible/{responsibleId}
Muda o responsável de uma tarefa.

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Responsável da tarefa atualizado",
  "data": "uuid"
}
```

---

# 6. Relatórios de Projeto (Project Report)

## GET /projects/{projectId}/report
Gera relatório do projeto (DTO).

### Permissão
Autenticado

### Retorno
```json
{
  "id": "uuid",
  "message": "Relatório gerado",
  "data": {
    "byStatus": {
      "PENDING": 3,
      "IN_PROGRESS": 5,
      "COMPLETED": 2
    },
    "byPriority": {
      "LOW": 2,
      "MEDIUM": 4,
      "HIGH": 4
    }
  }
}
```

---

# 7. Histórico de Tarefas (Task Log)

## GET /log/task/{id}
Lista o histórico de alterações (logs) de uma tarefa específica, com paginação.

### Permissão
Autenticado

### Retorno
```json
{
  "content": [
    {
      "id": "uuid",
      "taskId": "uuid",
      "userId": "uuid",
      "alteredField": "status",
      "oldValue": "PENDING",
      "newValue": "IN_PROGRESS",
      "alteredDate": "2026-06-23T15:00:00"
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

---

# Resumo rápido das regras principais
- `POST /user`, `POST /auth/login` e `POST /auth/refresh` são **públicos**.
- Endpoints de listagem geral como `GET /user`, `GET /project`, `GET /members` e `PATCH /user/change/*/role` exigem a role `ADMIN`.
- A criação e atualização de projetos (`POST /project`, `PUT /project/*`) e listagem por criador exige `ADMIN` ou `MANAGER`.
- Consultas a projetos por membro exigem `ADMIN`, `MEMBER` ou `MEMBER_MANAGER`.
- Adicionar membros (`POST /members/add`) exige `ADMIN`, `MANAGER` ou `MEMBER_MANAGER`.
- A grande maioria dos endpoints específicos (por ID, listagem de filhos, patchs pontuais e todas as rotas de Task e Relatórios) exige apenas que o usuário esteja **autenticado** via token JWT, caindo no `.anyRequest().authenticated()` da cadeia de filtros do Spring Security.
