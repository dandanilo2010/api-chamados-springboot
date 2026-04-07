# API de Chamados - Spring Boot

API REST desenvolvida com **Java + Spring Boot** para gerenciamento de **usuários e chamados**, com autenticação via **JWT**, controle de acesso por **roles**, regras de negócio, paginação, documentação com Swagger e testes unitários.

## Tecnologias utilizadas

- Java 17
- Banco Mysql
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- Maven
- Swagger / OpenAPI
- JUnit 5
- Mockito
- BCrypt

## Funcionalidades

### Usuários
- Cadastro de usuários
- Listagem de usuários
- Busca de usuário por ID
- Atualização de usuário
- Exclusão de usuário
- Senha criptografada com BCrypt
- Definição de role no cadastro:
  - `ROLE_USER`
  - `ROLE_ADMIN`

### Autenticação e segurança
- Login com geração de token JWT
- Rotas protegidas com Spring Security
- Controle de acesso com `@PreAuthorize`
- Autorização baseada em roles
- Integração do Swagger com Bearer Token

### Chamados
- Criação de chamado com usuário autenticado automaticamente
- Listagem de chamados
- Listagem dos chamados do usuário logado
- Busca de chamado por ID
- Atualização de chamado
- Exclusão de chamado
- Paginação nas listagens

## Regras de negócio implementadas

- O chamado é criado automaticamente com o usuário autenticado
- Todo chamado novo inicia com status **ABERTO**
- Chamados com prioridade **ALTA** exigem descrição mais detalhada
- Chamados **FINALIZADOS** não podem ser alterados
- Usuário comum visualiza apenas os próprios chamados
- Endpoints administrativos são protegidos por role

## Estrutura do projeto

O projeto foi organizado em camadas seguindo boas práticas do Spring Boot:

controller
service
repository
entity
dto
security
config
exception

Documentação da API

A API possui documentação com Swagger.

Após subir o projeto, acesse:

http://localhost:8080/swagger-ui/index.html

No Swagger é possível autenticar usando o botão Authorize com:

Bearer SEU_TOKEN
Paginação

As rotas de listagem utilizam paginação com Pageable.

Exemplo:

GET /chamados?page=0&size=5
GET /chamados/meus?page=0&size=5
GET /usuarios?page=0&size=5
Testes unitários

Foram implementados testes unitários com JUnit 5 e Mockito para validar regras importantes da aplicação.

Exemplos de testes implementados
Criação de usuário com senha criptografada
Criação de usuário com role ROLE_USER
Criação de chamado com usuário autenticado
Validação de prioridade alta com descrição curta
Principais endpoints
Usuários
POST /usuarios
GET /usuarios
GET /usuarios/{id}
PUT /usuarios/{id}
DELETE /usuarios/{id}
Autenticação
POST /auth/login
Chamados
POST /chamados
GET /chamados
GET /chamados/meus
GET /chamados/{id}
PUT /chamados/{id}
DELETE /chamados/{id}
Como executar o projeto
Clonar o repositório
git clone https://github.com/dandanilo2010/api-chamados-springboot.git
Entrar na pasta do projeto
cd api-chamados-springboot
Rodar a aplicação
mvn spring-boot:run
Autor

Danilo Augusto
Desenvolvedor Full Stack com foco em backend Java

GitHub: https://github.com/dandanilo2010
LinkedIn: https://linkedin.com/in/danilooaugusto

---

## Minha sugestão de ajuste final
Só mudaria uma linha do clone se o nome do seu repositório no GitHub for outro.

Se quiser, eu também posso te mandar uma **versão mais bonita e mais profissional visualmente*
