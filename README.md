# Desafio Elotech Taskmanager

---

### Backend é o principal, o frontend é para demonstrar o funcionamento do backend.

#### Endpoints documentados em:

``
/taskmanager/docs/Elotech_Task_Manager.postman_collection.json
``
###### Arrastar o arquivo para a interface do Postman e importar
#### e
``
/taskmanager/docs/API_ENDPOINTS.md
``

---

## Instruções para Rodar o Projeto

### Pré-requisitos
Antes de iniciar, certifique-se de ter as seguintes ferramentas instaladas na sua máquina:
- [Git](https://git-scm.com)
- [Docker & Docker Compose](https://www.docker.com/) (Caso queira utilizar o docker para rodar o projeto)
- [SDK/Runtime correspondente, ex: Java JDK 25, Node.js v18+]

### Passo a Passo

#### 1. Clonar o Projeto
```bash
git clone https://github.com/BrunoCarmoS2004/Elotech-Desafio-Tecnico-Task-Manager.git
```
#### 2. Acessar a pasta:
``
/Elotech-Desafio-Tecnico-Back-End-Task-Maneger
``
#### 3. Existe 3 jeitos de rodar o projeto
###### Tendo Docker instalado
###### 1. Rodar na primeira vez:
```bash 
docker compose -f infra/docker-compose.yml up -d --build
```
### ou
###### Tendo Docker instalado
###### 2. Abrir o \taskmanager na sua máquina numa IDE que suporte java
###### 2.1. Abrir o \taskmanager-frontend na sua máquina numa IDE que suporte TypeScript
###### 2.2 Rodar: 
```bash 
docker run --name postgres-taskmanager -e POSTGRES_USER=elotech -e POSTGRES_PASSWORD=elotech -e POSTGRES_DB=elotech -p 5432:5432 -d postgres:15-alpine
```
### ou
###### 1. Abrir o \taskmanager na sua máquina numa IDE que suporte java
###### 1.1. Abrir o \taskmanager-frontend na sua máquina numa IDE que suporte TypeScript
###### 1.2 Roda o Postgres local e criar um usuário, perfil e banco de dados chamado: elotech

### Caso opte pela 1º opção, existem alguns comandos do Docker para ajudar na análise:
##### 1. Subir sem dar build novamente:
```bash 
docker compose -f infra/docker-compose.yml up -d 
```
##### 2. Derrubar o container build:
```bash 
docker compose -f infra/docker-compose.yml down 
```
##### 3. Limpar todos os dados do banco de dados (Irá derrubar o container):
```bash 
docker compose -f infra/docker-compose.yml down -v 
```
---
## Decisoes tecnicas tomadas e os seus tradeoffs
### Uso do oauth2-resource-server para gerar o token JWL:
###### Vantagem: O oauth2-resource-server auxilia na criação do token JWT, usando chaves privadas e publicas para as validações de tokens.
###### Problema: Aumenta a complexidade da geração do token. 
### Uso do Caffeine para gerenciar o cache:
###### Vantagem: O Caffeine é muito rápido e simples de implementar.
###### Problema: O cache armazenado é em memória local, portanto pode encher a memoria da JVM rapidamente e também perde os dados ao reiniciar o sistema.
### Uso do PagedModel para a paginação:
###### Vantagem: O json de retorno é muito mais limpo se comparado ao do Page comum.
###### Problema: Um pouco mais complexo de se implementar se comparado ao Page comum.
### Uso do Flyway para a gestão de migrações no banco de dados:
###### Vantagem: Ele ajuda a versionar o banco de dados em migrações, por mais que no projeto tenha só uma migração, é sempre bom caso precise de outra ter esse versionamento.
###### Problema: Uso de SQL nativo e problemas de build quando fizer alguma alteração direto no banco, sem fazer uma migration no flyway.
### Uso do ModelMapper para a conversão e mapeamento de uma classe para outra:
###### Vantagem: Ele é extremamente simples de ser implementado e muito inteligente quando se diz em conversão de uma entidade para outra
###### Problema: Dependendo de como é implementado pode ter a desempenho reduzida e debuggar ele é muito difícil.

### Uso do Signal para a o frontend:
###### Vantagem: O Signal tem uma sintaxe simples, as variáveis são atualizadas automaticamente e ele atualiza a parte específica em que eme está no projeto quando o conteúdo é alterado.
###### Problema: Tudo é por função, até as variáveis. 

---
## O que eu faria diferente com mais tempo
#### Eu ampliaria a cobertura de testes automatizados.

#### Criaria uma pipelines de CI/CD, para deploy do projeto online.

#### Melhorar o mecanismo de Cache, deixando eles mais completos.

#### Criaria um microserviço de email para envio de email quando alguém criar uma conta, ser adicionado em um projeto e tiver alguma movimentação de task.

#### Melhoraria o sistema de gerenciamento dos projetos, criando mais endpoints de gestão de projetos e layouts personalizados

#### Criaria um frontend completo além de um simples só para demonstrar um backend.

---

