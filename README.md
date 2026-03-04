# 📚 Sistema de Biblioteca – API REST

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-4.00-red.svg)](https://maven.apache.org/)
[![H2 Database](https://img.shields.io/badge/H2-Database-yellow.svg)](https://www.h2database.com/)
[![GitHub](https://img.shields.io/badge/GitHub-Repo-lightgrey.svg)](https://github.com/Lucasbellodev/sistema-biblioteca)

API REST desenvolvida em Java com Spring Boot para gerenciamento de empréstimos de livros. O sistema permite cadastrar livros, realizar empréstimos, devoluções, renovações, calcular multas e consultar histórico, aplicando validações e regras de negócio.

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Data JPA / Hibernate**
- **Banco de dados H2** (para testes)
- **Maven** (gerenciamento de dependências)
- **Git** (controle de versão)

---

## 📌 Funcionalidades

### Livros
- ✅ Cadastrar, listar, buscar por ID, atualizar e deletar livros.
- ✅ Listar apenas livros disponíveis para empréstimo.

### Empréstimos
- ✅ Realizar empréstimo de um livro (com validações: livro deve existir, estar disponível, e usuário não pode ter mais de 3 empréstimos ativos).
- ✅ Devolver livro (com validação de empréstimo já devolvido).
- ✅ Renovar empréstimo (estender prazo por mais 7 dias, desde que não esteja atrasado).
- ✅ Calcular multa por atraso (R$ 2,00 por dia, apenas para empréstimos ativos e atrasados).
- ✅ Listar empréstimos ativos, atrasados, por usuário, por livro e por período.
- ✅ Verificar disponibilidade de um livro.

---

## ⚙️ Como executar o projeto

### Pré-requisitos
- Java 17 ou superior instalado
- Maven instalado
- Git (opcional, para clonar)

### Passos
1. Clone o repositório:
   ```bash
   git clone https://github.com/Lucasbellodev/sistema-biblioteca.git
   ```
2. Entre na pasta do projeto:
   ```bash
   cd sistema-biblioteca
   ```
3. Execute a aplicação com Maven:
   ```bash
   ./mvnw spring-boot:run   # Linux/macOS
   mvnw spring-boot:run     # Windows
   ```

A API estará disponível em http://localhost:8080.

O banco de dados H2 em memória é criado automaticamente ao iniciar a aplicação. Para acessar o console H2, vá a http://localhost:8080/h2-console (URL JDBC: jdbc:h2:mem:testdb, usuário: sa, senha em branco).

## 📬 Endpoints da API

### Livros
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET    | `/api/livros` | Lista todos os livros |
| GET    | `/api/livros/{id}` | Busca livro por ID |
| POST   | `/api/livros` | Cria um novo livro |
| PUT    | `/api/livros/{id}` | Atualiza um livro existente |
| DELETE | `/api/livros/{id}` | Remove um livro |
| GET    | `/api/livros/disponiveis` | Lista livros disponíveis |

### Empréstimos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/api/emprestimos?livroId={id}&nomeUsuario={nome}` | Realiza um empréstimo |
| PUT    | `/api/emprestimos/{id}/devolver` | Devolve um livro |
| PUT    | `/api/emprestimos/{id}/renovar` | Renova um empréstimo |
| GET    | `/api/emprestimos/{id}` | Busca empréstimo por ID |
| GET    | `/api/emprestimos` | Lista todos os empréstimos |
| GET    | `/api/emprestimos/ativos` | Lista apenas empréstimos ativos |
| GET    | `/api/emprestimos/atrasados` | Lista empréstimos atrasados |
| GET    | `/api/emprestimos/buscar/usuario?nomeUsuario={nome}` | Busca empréstimos por usuário |
| GET    | `/api/emprestimos/buscar/livro?livroId={id}` | Busca empréstimos por livro |
| GET    | `/api/emprestimos/disponibilidade?livroId={id}` | Verifica disponibilidade de um livro |
| GET    | `/api/emprestimos/{id}/multa` | Calcula multa de um empréstimo |
| GET    | `/api/emprestimos/periodo?inicio={data}&fim={data}` | Busca empréstimos por período |


## 🧪 Testando a API com Postman

Você pode importar a coleção do Postman disponível na pasta [`/postman`](./postman) do repositório (ou criar manualmente os endpoints conforme a tabela acima).

Exemplo de requisição para criar um empréstimo:

```http
POST http://localhost:8080/api/emprestimos?livroId=1&nomeUsuario=João
```

## 🔮 Melhorias Futuras

- Implementar entidade `Usuario` com relacionamento JPA.
- Adicionar autenticação e autorização com Spring Security.
- Tratamento global de exceções (`@ControllerAdvice`) com respostas HTTP adequadas.
- Migrar para banco de dados MySQL/PostgreSQL.
- Criar front-end básico (Thymeleaf ou React).
- Adicionar testes unitários e de integração.

## 📄 Licença

Este projeto está sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

**Lucas Bello Valente Chiqueto**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Perfil-blue)](https://www.linkedin.com/in/lucasbellodev/)
[![GitHub](https://img.shields.io/badge/GitHub-Lucasbellodev-lightgrey)](https://github.com/Lucasbellodev)

Fique à vontade para entrar em contato! 😊
