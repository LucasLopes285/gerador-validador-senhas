# Gerador e Validador de Senhas

Este é um projeto acadêmico para a disciplina de Segurança da Informação, consistindo em uma aplicação web para geração e validação de senhas seguras.

## Funcionalidades Principais

* **Gerador de Senhas:** Cria senhas personalizáveis com diferentes níveis de complexidade (letras maiúsculas, minúsculas, números e símbolos).
* **Validador de Força:** Analisa a robustez de senhas existentes através de um sistema de pontuação e cálculo de entropia.
* **Comunicação Segura:** A API é servida exclusivamente via HTTPS para garantir a confidencialidade dos dados.
* **Histórico:** Salva as senhas geradas em um banco de dados para consulta futura.

## Tecnologias Utilizadas

* **Backend:**
    * Java 17
    * Spring Boot 3
    * Spring Security
    * Spring Data JPA
* **Frontend:**
    * React
    * Vite
* **Banco de Dados:**
    * PostgreSQL
* **Versionamento:**
    * Git & GitHub

## Estrutura do Projeto

O projeto é organizado em uma arquitetura full-stack, com duas pastas principais na raiz: `frontend` e `gerador-senhas-api`.

### Backend (`gerador-senhas-api`)

O backend é uma API REST construída com Spring Boot e segue uma arquitetura em camadas para organizar as responsabilidades:

* `src/main/java/br/com/lucas/gerador_senhas_api/`
  * **`config/`**: Contém classes de configuração do Spring, com destaque para o `SecurityConfig`, que gerencia toda a segurança da aplicação (regras de acesso, filtros, CORS).
  * **`controller/`**: Define os endpoints da API (as URLs) que o frontend pode chamar.
  * **`service/`**: Contém a lógica de negócio principal (gerar senhas, validar força, autenticar usuários, gerenciar políticas).
  * **`repository/`**: Interfaces do Spring Data JPA que facilitam o acesso ao banco de dados sem a necessidade de escrever SQL.
  * **`model/`**: Classes de Entidade (`@Entity`) que representam as tabelas do banco de dados.
  * **`policy/`**: Implementa o sistema de regras e políticas customizáveis para validação de senhas.
  * **`filter/`**: Contém filtros customizados, como o `JwtAuthFilter`, que intercepta requisições para validar tokens de autenticação.
  * **`crypto/`**: Responsável pela lógica de criptografia dos dados salvos no banco.
  * **`dto/`**: (Data Transfer Objects) Classes simples para transportar dados entre o frontend e o backend.
* `src/main/resources/`
  * `application.properties`: Configurações gerais da aplicação (porta do servidor, configurações de SSL).
  * `application-secrets.properties`: **(Ignorado pelo Git)** Arquivo que armazena dados sensíveis, como senhas de banco de dados e chaves secretas.

### Frontend (`frontend`)

O frontend é uma Single Page Application (SPA) construída com React e Vite.

* `src/`
  * **`pages/`**: Componentes React que representam uma página inteira da aplicação (ex: `HomePage`, `LoginPage`).
  * **`components/`**: Componentes React menores e reutilizáveis usados dentro das páginas (ex: `PasswordHistory`).
  * **`context/`**: Contém o `AuthContext`, nosso gerenciador de estado global que compartilha as informações de login por toda a aplicação.
  * `main.jsx`: Ponto de entrada da aplicação, onde o roteador (`react-router-dom`) é configurado para gerenciar a navegação entre as páginas.
* `vite.config.js`: Arquivo de configuração do Vite, onde habilitamos o HTTPS e o proxy para o ambiente de desenvolvimento.

## Como Executar o Projeto

Este é um projeto full-stack com dois componentes que precisam ser executados separadamente.

### Backend (Porta 8443)

1.  Navegue até a pasta `gerador-senhas-api`.
2.  Certifique-se de ter um arquivo `application-secrets.properties` dentro de `src/main/resources` com as credenciais do seu banco de dados e do keystore.
3.  Execute a aplicação através da classe principal `GeradorSenhasApiApplication.java`.

### Frontend (Porta 5173)

1.  Navegue até a pasta `frontend` via terminal.
2.  Execute `npm install` para instalar as dependências.
3.  Execute `npm run dev` para iniciar o servidor de desenvolvimento.
4.  Acesse `http://localhost:5173` no seu navegador.