# Medflow

Medflow é um aplicativo para organizar o uso de medicamentos por paciente, com foco em rotina familiar, controle de horários, dosagem e acompanhamento de estoque.

O objetivo do projeto é ser um MVP simples e evolutivo, pensado para uso no dia a dia, principalmente em contexto doméstico: remédios do pai, da mãe, dos filhos ou de qualquer pessoa da casa.

## Proposta do MVP

O MVP do Medflow busca cobrir o fluxo essencial:

- cadastrar pacientes
- cadastrar medicamentos
- associar medicamentos a um paciente por meio de uma prescrição
- definir dose, frequência e horários
- registrar tomadas
- controlar estoque
- sugerir o melhor momento para nova compra em casos de uso contínuo

## Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Flyway
- H2 para desenvolvimento local
- PostgreSQL como opção de persistência principal
- Angular no frontend
- PWA para experiência mobile

## Arquitetura

O backend segue a ideia de MVC em camadas:

- `controller`: endpoints REST para consumo do frontend
- `service`: regras de negócio
- `repository`: acesso a dados com JPA
- `entity/model`: representação do domínio
- `dto`: contratos de entrada e saída da API

Domínios previstos no projeto:

- `patient`
- `medication`
- `prescription`
- `intake`

## Status atual

O projeto ainda está em construção e vem sendo desenvolvido por fatias.

No momento (v0.2.1):

- a estrutura inicial do backend foi criada
- os domínios `patient`, `medication`, `prescription` e `intake` já possuem base funcional no backend
- a API conta com testes de integração cobrindo o fluxo principal do MVP, validações básicas, cenários de `404` e registro em lote de tomadas
- o frontend Angular já possui fluxo para pacientes, medicamentos, prescrições, tomadas e dashboard
- o dashboard permite selecionar paciente, marcar medicamentos pendentes com checkbox e confirmar a tomada em lote via modal
- o frontend consome a API Spring via proxy local em desenvolvimento

## Como executar

### Requisitos

- JDK 21 configurado no ambiente
- opcionalmente Maven instalado, embora o projeto use Maven Wrapper

### Rodando com Maven Wrapper no Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Rodando os testes

```powershell
.\mvnw.cmd test
```

### Rodando o frontend Angular em desenvolvimento

Dentro da pasta `frontend`, para evitar erro de CORS no navegador durante o desenvolvimento local, suba o frontend com proxy apontando para o backend Spring:

```powershell
cd frontend
npm start -- --proxy-config proxy.conf.json
```

## Próximos passos

- evoluir filtros de prescrições e tomadas
- preparar suporte PWA com service worker e experiência offline
- refinar experiência mobile-first do frontend

## Qualidade atual

- testes de integração do fluxo principal de `patient`, `medication`, `prescription` e `intake`
- resposta de erro padronizada para validação e recursos não encontrados
- build validado com `.\mvnw.cmd test`
- build do frontend validado com `npm run build`

## Observações

O projeto usa Maven Wrapper, então arquivos como `mvnw`, `mvnw.cmd` e `.mvn/wrapper` fazem parte do repositório e não são sensíveis.
