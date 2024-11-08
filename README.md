# projeto_mensagens
Projeto de um app de mensagens para fins de prática/estudo

Tecnologias usadas/ a utilizar: 
- Front-end: Angular + TypeScript + HTML + CSS  
- Back-end: Java + Spring Boot + JPA + JUnit
- Banco de dados - PostgreSQL

* Pacotes criados no Back-end -> Controller, Service, Repository, Entity, DTO e Config.

Correções/Implementações a fazer:
Subitamente no projeto, os relacionamentos bidirecionais JSON estão com problemas de serialização que não se resolvem com @JsonIgnore, nem @JsonIgnoreProperties, nem @JsonBack/ManagedReference, @JsonIdentityInfo;
