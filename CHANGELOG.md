# Changelog

## [0.1.2] - 2026-05-04

### Adicionado

- suíte de testes de integração cobrindo o fluxo principal do MVP da API
- cenários automatizados para validação de payload e resposta `404`
- handler global simples para padronizar erros de validação e recursos não encontrados

### Observações

- build validado com `.\mvnw.cmd test`

## [0.1.1] - 2026-04-29

### Adicionado

- domínio `intake` com entidade, enum de status, DTOs, repository, service, controller e migration
- endpoints REST para criação, listagem, busca, atualização e inativação de tomadas

### Observações

- build validado com `.\mvnw.cmd test`

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
