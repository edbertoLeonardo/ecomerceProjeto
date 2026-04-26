
# 🛒 E-commerce (Spring Boot)

Este projeto consiste em uma API REST de e-commerce desenvolvida como trabalho acadêmico.
A aplicação utiliza o ecossistema Java e Spring Boot para fornecer o gerenciamento completo de usuários, catálogo de produtos e processamento de pedidos.
---

## 🚀 Tecnologias utilizadas

* Java 17
* Spring Boot
* Spring Data JPA
* Thymeleaf
* H2 Database
* MySQL
* Apache Maven

---

## ⚙️ Funcionalidades

* Cadastro de usuários
* Listagem de produtos
* Cadastro e gerenciamento de produtos
* Validação de dados
* Envio de e-mails
* Simulação de fluxo de compra

---

## ▶️ Como executar o projeto

### Pré-requisitos

* Java 17 instalado
* Apache Maven instalado

---

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/ecomerce.git
```

---

### 2. Acessar a pasta do projeto

```bash
cd ecomerce
```

---

### 3. Executar a aplicação

Usando o Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Ou com Maven instalado:

```bash
mvn spring-boot:run
```

---

### 4. Acessar no navegador

http://localhost:8080

---

## 🗄️ Banco de dados

O projeto suporta dois bancos de dados:

* H2 Database (ambiente de testes)
* MySQL (ambiente mais próximo de produção)

### Acesso ao H2 Console

http://localhost:8080/h2-console

---

## 📁 Estrutura do projeto

* `src/main/java` → Código fonte da aplicação
* `src/main/resources` → Configurações e templates
* `templates/` → Páginas HTML com Thymeleaf

---

## 🔮 Melhorias futuras

### 🧪 Testes automatizados

Serão implementados testes automatizados para garantir a qualidade da aplicação:

* Uso do JUnit para testes unitários
* Uso do Mockito para simulação de dependências
* Testes das regras de negócio e serviços

**Objetivo:** aumentar a confiabilidade e facilitar a manutenção do sistema.

---

### 🔐 Autenticação com JWT

Será implementado um sistema de autenticação utilizando JSON Web Token (JWT):

* Login de usuários com geração de token
* Proteção de rotas da aplicação
* Validação e expiração de tokens

**Objetivo:** garantir maior segurança e controle de acesso.



### 📦 Outras melhorias


* Criação da interface do usuário
* Integração com sistemas de pagamento

---

## 👨‍💻 Autor

* Edberto Leonardo

---

## 📌 Observações

Este projeto foi desenvolvido para fins acadêmicos e pode ser expandido com novas funcionalidades conforme a evolução do sistema.
