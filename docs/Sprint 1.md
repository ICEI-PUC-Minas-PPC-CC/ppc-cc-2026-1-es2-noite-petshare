# Sprint 1

## 1. Histórias dos Usuários
Com base na análise das personas e nas funcionalidades definidas para o MVP da plataforma PetShare, foram identificadas as seguintes histórias de usuário. 
Essas histórias representam as principais necessidades dos usuários do sistema.

| H | Histórias dos Usuários |
|---|---|
|H1.1|	Como usuário, eu desejo criar uma conta para acessar o sistema.|
|H1.2|	Como usuário, eu desejo fazer login para acessar meu perfil.|
|H2.1|	Como usuário, eu desejo editar meus dados pessoais.|
|H2.2|	Como usuário, eu desejo adicionar uma foto ao meu perfil.|
|H3.1|	Como dono, eu desejo cadastrar meu pet no sistema.|
|H4.1|	Como dono, eu desejo buscar cuidadores próximos.|
|H4.2|	Como dono, eu desejo aplicar filtros (tamanho, tipo de animal, etc.) na busca.|
|H5.1|	Como dono, eu desejo solicitar um cuidador para meu pet.|
|H6.1|	Como usuário, eu desejo conversar com o cuidador ou com o dono do pet antes da contratação, para alinhar informações sobre o serviço.|
|H7.1|	Como usuário, eu desejo ser informado quando minha solicitação mudar de status.|
|H8.1|	Como usuário, eu desejo avaliar o cuidador após o serviço.|

---

## 2. Identificação das Entidades de Domínio
| Entidade | Descrição |
| --- | --- |
| Usuário | Representa qualquer pessoa cadastrada na plataforma |
| DonoPet | Especialização da entidade Usuário |
| Cuidador | Especialização da entidade Usuário. |
| Perfil | Representa as informações públicas do usuário |
| Pet | Representa os animais cadastrados pelos donos |
| SolicitaçãoCuidado | Representa o pedido de cuidado enviado pelo dono ao cuidador |
| Mensagem | Representa mensagens trocadas no chat interno |
| Notificação | Representa alertas enviados pelo sistema |
| Avaliação | Representa avaliações realizadas após um serviço |

---

## 3. Classes do Sistema

Com base nas entidades identificadas, foram levantadas as seguintes classes iniciais para o sistema:

### Classe Usuário
+ Responsabilidades:
  + realizar cadastro;
  + autenticar acesso;
  + editar informações pessoais;
  + gerenciar perfil.
 
### Classe DonoPet
+ Responsabilidades:
  + cadastrar pets;
  + buscar cuidadores;
  + enviar solicitações de cuidado;
  + avaliar cuidadores;
 
### Classe Cuidador
+ Responsabilidades:
  + informar disponibilidade;
  + visualizar solicitações;
  + aceitar ou recusar pedidos;
  + responder mensagens;
 
### Classe Perfil
+ Responsabilidades:
  + armazenar descrição do usuário;
  + armazenar foto e informações públicas;
  + exibir experiência e disponibilidade;

### Classe Pet
+ Responsabilidades:
  + armazenar informações do animal;
  + registrar necessidades especiais;
  + manter histórico de dados do pet;
 
### Classe SolicitaçãoCuidado
+ Responsabilidades:
  + registrar pedidos de cuidado;
  + controlar status da solicitação;
  + armazenar data, horário e observações;
  + relacionar dono, cuidador e pet.

### Classe Mensagem
+ Responsabilidades:
  + permitir comunicação entre usuários;
  + armazenar mensagens enviadas;
  + registrar data e horário das conversas.

### Classe Notificação
+ Responsabilidades:
+ informar alterações nas solicitações;
+ alertar sobre novas mensagens;
+ atualizar status de serviços.

### Classe Avaliação
+ Responsabilidades:
  + registrar notas e comentários;
  + calcular reputação do cuidador;
  + armazenar feedbacks dos serviços realizados.
