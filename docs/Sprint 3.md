# SPRINT 3 - Definição da Arquitetura do Sistema

## Descrição da Arquitetura

Para o desenvolvimento da plataforma **PetShare**, foi adotada a **Arquitetura em Camadas (Layered Architecture)**. 
Essa arquitetura organiza o sistema em diferentes níveis de responsabilidade, promovendo melhor manutenção, reutilização de código e separação das regras de negócio.

A solução foi dividida em quatro camadas principais:

- **Camada de Apresentação (Controller):** responsável por receber as requisições da interface do usuário e encaminhá-las para a camada de serviço.
- **Camada de Serviço (Service):** responsável por implementar as regras de negócio do sistema.
- **Camada de Persistência (Repository):** responsável pelo acesso e manipulação dos dados armazenados.
- **Camada de Domínio (Model):** responsável por representar as entidades e objetos de negócio do sistema.

---

# Estrutura da Arquitetura

```text
┌───────────────────────┐
│      Interface Web    │
└───────────┬───────────┘
            │
            ▼
┌───────────────────────┐
│      Controllers      │
│ UsuarioController     │
│ PetController         │
│ SolicitacaoController │
│ MensagemController    │
└───────────┬───────────┘
            │
            ▼
┌───────────────────────┐
│        Services       │
│ UsuarioService        │
│ PetService            │
│ SolicitacaoService    │
│ MensagemService       │
│ AvaliacaoService      │
└───────────┬───────────┘
            │
            ▼
┌───────────────────────┐
│      Repositories     │
│ UsuarioRepository     │
│ PetRepository         │
│ SolicitacaoRepository │
│ MensagemRepository    │
│ AvaliacaoRepository   │
└───────────┬───────────┘
            │
            ▼
┌───────────────────────┐
│      Banco de Dados   │
└───────────────────────┘

         ▲
         │
┌───────────────────────┐
│        Models         │
│ Usuario              │
│ DonoPet              │
│ Cuidador             │
│ Perfil               │
│ Pet                  │
│ SolicitacaoCuidado   │
│ Mensagem             │
│ Notificacao          │
│ Avaliacao            │
└───────────────────────┘
```

---

# Responsabilidades das Camadas

## Camada Controller

Responsável pela comunicação entre a interface e o sistema.

### Principais responsabilidades

- Receber requisições da aplicação;
- Validar dados básicos de entrada;
- Encaminhar solicitações para a camada de serviço;
- Retornar respostas ao usuário.

### Classes

- UsuarioController
- PetController
- SolicitacaoController
- MensagemController
- AvaliacaoController

---

## Camada Service

Responsável pelas regras de negócio da aplicação.

### Principais responsabilidades

- Processar regras de negócio;
- Validar operações do sistema;
- Coordenar a comunicação entre entidades e repositórios.

### Classes

- UsuarioService
- PetService
- CuidadorService
- SolicitacaoService
- MensagemService
- AvaliacaoService

---

## Camada Repository

Responsável pela persistência dos dados.

### Principais responsabilidades

- Salvar registros;
- Atualizar informações;
- Buscar dados;
- Remover registros.

### Classes

- UsuarioRepository
- PetRepository
- SolicitacaoRepository
- MensagemRepository
- AvaliacaoRepository

---

## Camada Model

Responsável por representar as entidades do domínio identificadas na Sprint 2.

### Classes

- Usuario
- DonoPet
- Cuidador
- Perfil
- Pet
- SolicitacaoCuidado
- Mensagem
- Notificacao
- Avaliacao

---

# Comunicação Entre os Componentes

A comunicação entre os componentes será realizada por meio de uma **API REST**.

## Exemplo de Fluxo: Solicitação de Cuidado

### 1. O usuário envia uma requisição

```http
POST /solicitacoes
```

### 2. O Controller recebe a requisição

```text
SolicitacaoController
```

O controlador recebe os dados da solicitação e encaminha para a camada de serviço.

### 3. O Service executa as regras de negócio

O serviço verifica:

- Existência do dono do pet;
- Existência do cuidador;
- Validação dos dados da solicitação;
- Disponibilidade do cuidador.

### 4. O Repository persiste os dados

```text
SolicitacaoRepository
```

Realiza a operação de gravação no banco de dados.

### 5. O sistema retorna a resposta

```json
{
  "id": 15,
  "status": "PENDENTE"
}
```

---

# Organização dos Pacotes

```text
petshare
│
├── controller
│   ├── UsuarioController
│   ├── PetController
│   ├── SolicitacaoController
│   ├── MensagemController
│   └── AvaliacaoController
│
├── service
│   ├── UsuarioService
│   ├── PetService
│   ├── SolicitacaoService
│   ├── MensagemService
│   └── AvaliacaoService
│
├── repository
│   ├── UsuarioRepository
│   ├── PetRepository
│   ├── SolicitacaoRepository
│   ├── MensagemRepository
│   └── AvaliacaoRepository
│
├── model
│   ├── Usuario
│   ├── DonoPet
│   ├── Cuidador
│   ├── Perfil
│   ├── Pet
│   ├── SolicitacaoCuidado
│   ├── Mensagem
│   ├── Notificacao
│   └── Avaliacao
│
└── database
```

---

# Justificativa da Arquitetura Escolhida

A arquitetura em camadas foi escolhida por proporcionar uma clara separação de responsabilidades entre a interface do usuário, as regras de negócio e a persistência dos dados.

Essa organização reduz o acoplamento entre os componentes, facilita a manutenção e evolução do sistema e melhora a reutilização do código.

Além disso, a utilização de uma API REST permite futuras integrações com aplicações móveis e outros sistemas externos sem alterações significativas na lógica de negócio.

Dessa forma, a arquitetura adotada atende adequadamente às necessidades do MVP da plataforma PetShare e segue boas práticas de Engenharia de Software.
