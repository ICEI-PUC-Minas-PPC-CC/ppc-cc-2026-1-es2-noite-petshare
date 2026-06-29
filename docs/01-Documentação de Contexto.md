# Introdução

O **PetShare** é uma aplicação web/mobile que tem como objetivo conectar donos de animais de estimação a cuidadores disponíveis para prestar serviços temporários. A proposta surge a partir de uma necessidade real observada no cotidiano: a dificuldade em encontrar pessoas confiáveis para cuidar de pets em situações específicas, como viagens, compromissos profissionais ou imprevistos.

O sistema busca oferecer uma plataforma prática, segura e acessível, facilitando a comunicação entre as partes e tornando o processo de encontrar cuidadores mais ágil e eficiente.

---

## Problema

Atualmente, donos de animais de estimação enfrentam dificuldades ao precisar deixar seus pets sob os cuidados de terceiros por um determinado período. Situações como viagens, jornadas de trabalho extensas ou compromissos inesperados tornam essa necessidade recorrente.

O problema se agrava devido à falta de divulgação e centralização de serviços de cuidado de pets, especialmente em determinadas regiões. Como consequência, a solução mais comum adotada pelos donos é recorrer a amigos ou familiares.

Entretanto, esse modelo apresenta limitações importantes:

- Nem sempre há disponibilidade por parte dessas pessoas;
- Pode gerar desconforto ou inconveniência;
- Não há garantia de experiência ou preparo no cuidado com animais.

Dessa forma, observa-se a ausência de uma solução estruturada, acessível e confiável que conecte donos de pets a cuidadores disponíveis de forma eficiente.

---

## Objetivos

### Objetivo Geral

Desenvolver um sistema de software que conecte donos de animais de estimação a cuidadores, facilitando a contratação de serviços temporários de cuidado de forma prática, segura e eficiente.

### Objetivos Específicos

- Desenvolver uma plataforma web que permita o cadastro de usuários e pets;
- Implementar funcionalidades para solicitação de serviços de cuidado (data, horário e detalhes do pet);
- Permitir que cuidadores se cadastrem e ofereçam seus serviços;
- Explorar conceitos de Engenharia de Software, incluindo definição de arquitetura, modelagem e tomada de decisões de implementação;
- Criar um sistema de interação entre usuários que favoreça confiança e usabilidade.

---

## Justificativa

O desenvolvimento do PetShare se justifica pela necessidade real identificada no cotidiano de donos de pets, especialmente em contextos onde não há fácil acesso a serviços especializados de cuidado animal.

Além disso, há uma tendência crescente no aumento da população de animais de estimação e, consequentemente, da demanda por serviços relacionados ao seu bem-estar. Isso reforça a relevância de soluções tecnológicas que atendam esse público.

O projeto também possui caráter acadêmico, permitindo a aplicação prática de conceitos de Engenharia de Software, como levantamento de requisitos, definição de arquitetura e desenvolvimento de sistemas reais.

A escolha do tema surgiu a partir de uma necessidade vivenciada por um dos integrantes do grupo, o que contribui para maior aderência do projeto à realidade e aumenta seu potencial de utilidade.

---

## Público-Alvo

O sistema é direcionado a dois principais perfis de usuários:

### Donos de Pet

Pessoas que possuem animais de estimação e necessitam de serviços temporários de cuidado. Em geral:

- Possuem rotina ocupada;
- Valorizam praticidade e segurança;
- Buscam soluções digitais para o dia a dia;
- Utilizam predominantemente dispositivos móveis.

### Cuidadores de Pet

Pessoas interessadas em cuidar de animais, seja de forma voluntária ou remunerada. Podem incluir:

- Estudantes (ex: veterinária);
- Profissionais autônomos;
- Pessoas que buscam renda extra.

Esses usuários:

- Possuem conhecimento básico de tecnologia;
- Precisam de visibilidade e oportunidades;
- Valorizam sistemas de avaliação e reputação.

### Características Gerais

- Faixa etária: majoritariamente maiores de idade;
- Nível tecnológico: básico a intermediário;
- Plataforma preferencial: dispositivos móveis;
- Relação entre usuários: baseada em confiança, podendo ser mediada por sistemas de avaliação.

---

## Personas

### Persona 1: Mariana Souza — Dona de Pet

- **Idade:** 32 anos  
- **Profissão:** Analista de Marketing Digital  
- **Localização:** São Paulo - SP  

**Características:**
- Organizada, responsável e prática;
- Usuária frequente de aplicativos;
- Possui rotina híbrida e viagens frequentes.

**Necessidades:**
- Encontrar cuidadores confiáveis;
- Garantir o bem-estar do pet;
- Ter segurança ao delegar cuidados;
- Utilizar uma plataforma simples e rápida.

---

### Persona 2: Lucas Almeida — Cuidador

- **Idade:** 25 anos  
- **Profissão:** Estudante de Medicina Veterinária  
- **Localização:** Belo Horizonte - MG  

**Características:**
- Empático, proativo e cuidadoso;
- Busca experiência prática e renda extra;
- Tem disponibilidade em horários flexíveis.

**Necessidades:**
- Divulgar seus serviços;
- Ser reconhecido por avaliações;
- Encontrar clientes na sua região;
- Facilitar comunicação com donos de pets.

---

## Papéis no Sistema

Atualmente, o sistema considera três entidades principais:

- **Pai de Pet (dono)**  
- **Pet**  
- **Cuidador**

Esses papéis serão utilizados como base para a modelagem do sistema e definição das funcionalidades.