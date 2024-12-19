# projeto_mensagens
Projeto de um app de mensagens para fins de prática/estudo

Tecnologias usadas/ a utilizar: 
- Front-end: Angular + TypeScript + HTML + CSS  
- Back-end: Java + Spring Boot + JPA + JUnit
- Banco de dados - PostgreSQL

* Pacotes criados no Back-end -> Controller, Service, Repository, Entity, DTO e Config.

Correções/Implementações a fazer:
- Alinhar o front-end com a autenticação do back;
- Corrigir "encerrar conversas" e ajustar "iniciar conversa" a partir da página de contatos;
- Ajustar Arquivo no Front/Back;

Observações -> A autenticação era feita por Basic Auth, troquei para cookies/sessões pois o navegador estava armazenando os dados de autenticação fazendo com que logins posteriores recebessem dados quebrados. Apesar disso, o navegador seguia pedindo autorização ao logar causando a mesma quebra de dados. A partir disso inseri JJWT e o comportamento agora está ajustado.
