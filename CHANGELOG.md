# Changelog

## [0.1.0] - 2026-04-29

### Adicionado

- estrutura inicial do projeto Spring Boot com Maven Wrapper
- configuração do backend para Java 21
- `README.md` inicial do projeto
- domínio `patient` com entidade, DTOs, repository, service, controller e migration
- domínio `medication` com entidade, DTOs, repository, service, controller e migration
- domínio `prescription` com entidade, DTOs, repository, service, controller e migration
- enums iniciais para relacionamento do paciente e unidade de dosagem
- configuração de H2 em memória para desenvolvimento local

### Observações

- o domínio `intake` ainda está apenas como esqueleto inicial
- build validado com `.\mvnw.cmd test`
