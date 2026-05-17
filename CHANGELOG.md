# Changelog

## Não versionado - 2026-05-08

### Adicionado

- ações rápidas no dashboard para marcar tomadas pendentes como tomadas ou puladas
- fluxo inicial do frontend Angular para prescrições
- fluxo inicial do frontend Angular para tomadas
- dashboard inicial com resumo do MVP e próximas tomadas
- navegação mobile-first com acesso a pacientes, medicamentos, prescrições, tomadas e dashboard

### Ajustado

- página inicial do frontend redirecionando para o dashboard
- metadados básicos do `index.html` em PT-BR
- formatação inicial de datas e horários nas listas
- configuração do `tsconfig.app.json` com `rootDir` explícito

### Observações

- sem alteração de versão semver nesta etapa
- build do frontend validado com `npm run build`

## [0.1.3] - 2026-05-04

### Ajustado

- configuração de ambiente do frontend para usar `/api` em desenvolvimento
- proxy local do Angular para integração com o backend sem erro de CORS no navegador

### Observações

- frontend validado localmente com `npm start -- --proxy-config proxy.conf.json`

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
