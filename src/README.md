# Instrucoes de utilizacao

## Sobre a implementacao

Este diretorio contem o MVP do PetShare implementado com backend real em Java, seguindo os artefatos das Sprints:

- Cadastro e login de usuarios;
- Edicao basica de perfil;
- Cadastro e consulta de pets;
- Visualizacao de cuidadores;
- Criacao de solicitacao de cuidado;
- Aceite/recusa de solicitacao pelo cuidador;
- Notificacoes de alteracao de status.

## Tecnologias utilizadas

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Banco relacional H2 (arquivo local)
- Interface Web em HTML, CSS e JavaScript (servida pelo proprio backend)

## Como executar

1. No terminal, entre na pasta do codigo:

	cd src

2. Execute o projeto:

	./mvnw spring-boot:run

	Se nao possuir mvnw no ambiente, use:

	mvn spring-boot:run

3. Acesse no navegador:

	http://localhost:8080

4. Console do banco H2 (opcional):

	http://localhost:8080/h2-console

	JDBC URL: jdbc:h2:file:./petshare-db
	User: sa
	Password: (vazio)

## Endpoints principais

- POST /api/usuarios/cadastro
- POST /api/usuarios/login
- PUT /api/usuarios/{id}
- GET /api/cuidadores
- POST /api/pets
- GET /api/usuarios/{id}/pets
- POST /api/solicitacoes
- GET /api/solicitacoes?usuarioId={id}
- PATCH /api/solicitacoes/{id}/status
- GET /api/usuarios/{id}/notificacoes
- PATCH /api/notificacoes/{id}/lida

## Observacao

Na primeira execucao, o sistema cria dois cuidadores de exemplo automaticamente para permitir o fluxo completo do MVP.

## Historico de versoes

### [0.1.0] - 28/06/2026
#### Adicionado
- Implementacao do MVP da Sprint 4 com backend real em Java;
- Persistencia relacional local com H2;
- Interface web funcional para operacao completa do fluxo principal.