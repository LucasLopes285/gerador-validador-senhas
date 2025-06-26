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