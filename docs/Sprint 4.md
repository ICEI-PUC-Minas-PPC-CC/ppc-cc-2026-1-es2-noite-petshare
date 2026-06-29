# SPRINT 4 – Organização, Integração e Planejamento do MVP

## 1. Descrição do Fluxo Principal do Sistema

A funcionalidade principal do sistema PetShare consiste na solicitação de um cuidador por parte do dono do pet. Esse fluxo representa o objetivo central da plataforma: conectar donos de animais a cuidadores disponíveis para prestar serviços temporários.

### Fluxo Principal

1. O usuário realiza cadastro e login na plataforma.

2. O dono do pet cadastra um ou mais pets no sistema, informando dados como nome, espécie, raça, idade e necessidades especiais.

3. O dono acessa a funcionalidade de busca de cuidadores disponíveis.

4. O sistema apresenta os cuidadores cadastrados, permitindo a consulta de seus perfis.

5. O dono seleciona um cuidador e preenche os dados da solicitação de cuidado:

   * data de início;
   * data de término;
   * observações;
   * pet que receberá o serviço.

6. A solicitação é enviada ao sistema.

7. O sistema valida os dados informados e verifica a existência do dono, do pet e do cuidador selecionado.

8. A solicitação é registrada com status “PENDENTE”.

9. O cuidador visualiza a solicitação recebida.

10. O cuidador pode aceitar ou recusar o pedido.

11. O sistema atualiza o status da solicitação.

12. Uma notificação é enviada ao dono do pet informando o resultado da solicitação.

13. O dono visualiza a resposta diretamente na plataforma.

### Fluxo Resumido

```text
Login
   ↓
Cadastro do Pet
   ↓
Busca de Cuidador
   ↓
Criação da Solicitação
   ↓
Validação da Solicitação
   ↓
Registro no Banco
   ↓
Resposta do Cuidador
   ↓
Atualização de Status
   ↓
Notificação ao Dono
```

---

# 2. Definição do Escopo do MVP

O MVP (Minimum Viable Product) representa a primeira versão funcional da plataforma PetShare, contendo apenas as funcionalidades essenciais para validar o principal objetivo do sistema.

## Funcionalidades Incluídas no MVP

### Gestão de Usuários

* Cadastro de usuário;
* Login no sistema;
* Edição básica do perfil.

### Gestão de Pets

* Cadastro de pets;
* Consulta dos pets cadastrados.

### Gestão de Cuidadores

* Visualização de cuidadores disponíveis;
* Consulta de perfil do cuidador.

### Solicitação de Cuidados

* Criação de solicitação de cuidado;
* Visualização de solicitações;
* Aceitação ou recusa da solicitação pelo cuidador;
* Atualização do status da solicitação.

### Notificações

* Envio de notificação quando houver alteração no status da solicitação.

---

## Funcionalidades Fora do MVP

As funcionalidades abaixo foram identificadas durante a modelagem do sistema, porém serão implementadas em versões futuras:

* Sistema de mensagens (chat);
* Avaliações de cuidadores;
* Geolocalização;
* Sistema de pagamentos;
* Agenda integrada;
* Upload de documentos;
* Recomendação inteligente de cuidadores;
* Autenticação OAuth;
* Sistema de denúncias.

A exclusão dessas funcionalidades do MVP permite reduzir a complexidade inicial e concentrar os esforços na validação da principal proposta de valor do sistema.

---

# 3. Planejamento Técnico da Implementação

A implementação do MVP seguirá a arquitetura em camadas definida na Sprint 3.

## Arquitetura Utilizada

```text
Interface Web
      ↓
Controllers
      ↓
Services
      ↓
Repositories
      ↓
Banco de Dados
```

As entidades do domínio permanecerão na camada Model e serão utilizadas pelas demais camadas.

---

## Classes Utilizadas no MVP

### Model

```text
Usuario
DonoPet
Cuidador
Perfil
Pet
SolicitacaoCuidado
Notificacao
```

### Controller

```text
UsuarioController
PetController
SolicitacaoController
```

### Service

```text
UsuarioService
PetService
CuidadorService
SolicitacaoService
```

### Repository

```text
UsuarioRepository
PetRepository
SolicitacaoRepository
```

---

## Implementação da Funcionalidade Principal

A funcionalidade principal do MVP será a criação de uma solicitação de cuidado.

### Etapa 1 – Requisição do Usuário

O dono do pet seleciona um cuidador e envia uma solicitação através da interface.

Exemplo:

```http
POST /solicitacoes
```

---

### Etapa 2 – Controller

A requisição é recebida pelo:

```text
SolicitacaoController
```

Responsabilidades:

* receber os dados da solicitação;
* validar campos obrigatórios;
* encaminhar para a camada de serviço.

---

### Etapa 3 – Service

A lógica de negócio é executada pelo:

```text
SolicitacaoService
```

Responsabilidades:

* verificar existência do dono;
* verificar existência do cuidador;
* verificar existência do pet;
* validar datas informadas;
* definir o status inicial da solicitação.

Após a validação, a solicitação é enviada para persistência.

---

### Etapa 4 – Repository

A camada:

```text
SolicitacaoRepository
```

será responsável por:

* salvar a solicitação;
* recuperar solicitações;
* atualizar status.

---

### Etapa 5 – Banco de Dados

Os dados da solicitação serão armazenados contendo:

```text
id
dataInicio
dataFim
observacoes
status
idDono
idCuidador
idPet
```

---

### Etapa 6 – Retorno ao Usuário

Após o salvamento, o sistema retorna uma resposta informando o sucesso da operação.

Exemplo:

```json
{
  "id": 15,
  "status": "PENDENTE"
}
```

---

# 4. Integração Entre os Componentes

A integração entre os componentes do MVP ocorrerá da seguinte forma:

```text
Dono do Pet
      ↓
Interface Web
      ↓
SolicitacaoController
      ↓
SolicitacaoService
      ↓
SolicitacaoRepository
      ↓
Banco de Dados
      ↓
SolicitacaoRepository
      ↓
SolicitacaoService
      ↓
Notificacao
      ↓
Interface Web
```

Durante esse processo:

* o Controller recebe as requisições;
* o Service executa as regras de negócio;
* o Repository realiza o acesso aos dados;
* o Model representa as entidades do domínio;
* a Notificação informa alterações de status ao usuário.

---

# Conclusão

O MVP do PetShare será focado no principal objetivo da plataforma: permitir que donos de pets encontrem cuidadores e realizem solicitações de cuidado. A implementação seguirá a arquitetura em camadas definida anteriormente, utilizando as classes modeladas nas Sprints 1, 2 e 3. Essa abordagem garante coerência entre requisitos, modelagem e arquitetura, além de fornecer uma base sólida para a evolução futura do sistema.
