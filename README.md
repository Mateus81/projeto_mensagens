# projeto_mensagens
Projeto de um app de mensagens para fins de prática/estudo

Tecnologias usadas/ a utilizar: 
- Front-end: Angular + TypeScript + HTML + CSS  
- Back-end: Java + Spring Boot + JPA + JUnit
- Banco de dados - PostgreSQL

* Pacotes criados no Back-end -> Controller, Service, Repository, Entity, DTO e Config.

Correções/Implementações a fazer:
- Corrigir Autenticação;
- Corrigir "encerrar conversas" e ajustar "iniciar conversa" a partir da página de contatos;
- Ajustar Arquivo no Front/Back;

Observações -> A autenticação era feita por Basic Auth, troquei para cookies/sessões pois o navegador estava armazenando os dados de autenticação fazendo com que logins posteriores recebessem dados quebrados. Apesar disso, o navegador segue pedindo autorização ao logar causando a mesma quebra de dados. Se a autorização não é dada o sistema responde com erro 401. O armazenamento de cookies/sessão está aparentemente OK.
