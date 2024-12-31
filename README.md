# projeto_mensagens
Projeto de um app de mensagens para fins de prática/estudo

Tecnologias usadas/ a utilizar: 
- Front-end: Angular + TypeScript + HTML + CSS  
- Back-end: Java + Spring Boot + JPA + JUnit + Mockito + Maven
- Banco de dados - PostgreSQL

* Pacotes criados no Back-end -> Controller, Service, Repository, Entity, DTO, Util e Config.

Correções/Implementações a fazer:
- Verificar se há ajustes extras nas operações restantes;
- Criar Arquivo no Front-end;

Observações -> A autenticação era feita por Basic Auth, troquei para cookies/sessões pois o navegador estava armazenando os dados de autenticação fazendo com que logins posteriores recebessem dados quebrados. Apesar disso, o navegador seguia pedindo autorização ao logar causando a mesma quebra de dados. A partir disso inseri JJWT e o comportamento agora está ajustado.
