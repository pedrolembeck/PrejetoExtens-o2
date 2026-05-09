# Guia Completo de Configuração e Desenvolvimento do Projeto

# Projeto Extensão LL

Sistema desenvolvido em Java utilizando Maven, PostgreSQL, JDBC e arquitetura em camadas.

---

# Objetivo deste Documento

Este guia foi criado para orientar todos os integrantes da equipe sobre:

* Como configurar o ambiente;
* Como clonar o projeto;
* Como utilizar corretamente o Git;
* Como executar o sistema;
* Como funciona a conexão com o banco PostgreSQL no Railway;
* Como desenvolver funcionalidades sem causar conflitos no projeto.

---

# Tecnologias Utilizadas

* Java 21
* Maven
* PostgreSQL
* JDBC
* IntelliJ IDEA
* Git
* GitHub
* Railway

---

# Arquitetura do Projeto

O sistema utiliza arquitetura em camadas.

Estrutura:

```txt
src/main/java/com/doacoes

├── App.java
├── connection
│   └── ConnectionFactory.java
│
├── model
│
├── dao
│
├── service
│
└── view
```

---

# Explicação das Pastas

## model

Responsável pelas entidades do sistema.

Exemplo:

* Doador
* Receptor
* Doacao
* Item
* Estoque

Essas classes representam os dados do sistema.

---

## dao

Responsável pela comunicação com o banco de dados.

DAO significa:

```txt
Data Access Object
```

Toda lógica SQL fica nessa camada.

Exemplo:

* INSERT
* UPDATE
* DELETE
* SELECT

---

## service

Responsável pelas regras de negócio.

Essa camada faz a intermediação entre:

```txt
View → Service → DAO
```

Exemplo:

* validações;
* regras;
* verificações;
* tratamentos.

---

## view

Responsável pela interface gráfica.

Futuramente será utilizada com JavaFX.

---

## connection

Responsável pela conexão com o banco PostgreSQL.

A classe:

```txt
ConnectionFactory.java
```

centraliza toda conexão do sistema.

---

# Como Funciona o Banco de Dados

O PostgreSQL está hospedado na nuvem através do Railway.

Fluxo da aplicação:

```txt
Aplicação Java
        ↓
JDBC Driver
        ↓
Internet
        ↓
PostgreSQL Railway
```

Quando o sistema executa um SQL:

```sql
INSERT INTO tabela ...
```

O Java envia esse comando diretamente para o banco remoto.

---

# Importante

O banco é compartilhado por toda equipe.

Isso significa que:

* alterações feitas por um integrante podem aparecer para os demais;
* dados inseridos permanecem salvos no PostgreSQL;
* o Railway funciona como servidor remoto do banco.

---

# Configuração do Ambiente

Todos os integrantes precisam possuir:

* Java JDK 21
* Maven
* IntelliJ IDEA
* Git

---

# Verificando Instalações

## Java

```bash
java -version
```

---

## Maven

```bash
mvn -version
```

---

## Git

```bash
git --version
```

---

# Clonando o Projeto

## 1. Clonar repositório

```bash
git clone URL_DO_REPOSITORIO
```

---

## 2. Entrar na pasta

```bash
cd projeto_extensao_ll
```

---

## 3. Trocar para branch develop

```bash
git checkout develop
```

---

## 4. Atualizar projeto

```bash
git pull origin develop
```

---

# Abrindo no IntelliJ

1. Abrir IntelliJ IDEA;
2. Selecionar:

```txt
Open Project
```

3. Escolher a pasta:

```txt
projeto_extensao_ll
```

4. Esperar Maven carregar dependências.

---

# Dependências Maven

As dependências do projeto são gerenciadas pelo:

```txt
pom.xml
```

Exemplo:

* PostgreSQL JDBC Driver;
* Hibernate;
* plugins Maven.

Quando executar:

```bash
mvn clean install
```

O Maven baixa automaticamente todas dependências necessárias.

---

# Compilar o Projeto

```bash
mvn clean install
```

Se aparecer:

```txt
BUILD SUCCESS
```

O projeto está funcionando corretamente.

---

# Executar o Sistema

```bash
mvn exec:java -Dexec.mainClass="com.doacoes.App"
```

---

# Fluxo Git do Projeto

## Branches

### main

Branch estável do projeto.

---

### develop

Branch principal de desenvolvimento.

Toda equipe deve trabalhar baseada nela.

---

# Fluxo Correto de Trabalho

## 1. Atualizar develop

```bash
git checkout develop
git pull origin develop
```

---

## 2. Criar branch da funcionalidade

Exemplo:

```bash
git checkout -b feature/doador
```

Outros exemplos:

```bash
git checkout -b feature/doacao

git checkout -b feature/item

git checkout -b feature/estoque
```

---

## 3. Desenvolver normalmente

Fazer alterações apenas na própria branch.

---

## 4. Adicionar alterações

```bash
git add .
```

---

## 5. Fazer commit

```bash
git commit -m "Implementação do CRUD de doador"
```

---

## 6. Enviar para GitHub

```bash
git push origin feature/doador
```

---

# Importante

NUNCA desenvolver diretamente na branch:

```txt
develop
```

Sempre criar branch própria.

---

# Convenções do Projeto

## Classes

Utilizar PascalCase.

Exemplo:

```txt
DoadorDAO
TipoDoacao
DistribuicaoService
```

---

## Métodos

Utilizar camelCase.

Exemplo:

```txt
listarDoadores()
salvarDoacao()
criarTabela()
```

---

## DAO

Toda lógica SQL deve permanecer dentro do DAO.

---

## Model

As entidades devem conter:

* atributos;
* construtores;
* getters;
* setters;
* toString.

---

# Funcionalidades do Projeto

## Doador e Receptor — Dacio


Implementar:

* cadastrar;
* listar;
* alterar;
* deletar.

---

## Doação — Andrei


Implementar:

* cadastrar;
* listar;
* alterar;
* deletar.

---

## Item — Alan


Implementar:

* cadastrar;
* listar;
* alterar;
* deletar.

---

## Estoque — Felippe


Implementar:

* cadastrar;
* listar;
* alterar;
* deletar.

---

## Distribuição — Pedro


Implementar:

* cadastrar;
* listar;
* alterar;
* deletar.

---

# Estrutura Recomendada para CRUD

## Model

Representa entidade.

Exemplo:

```txt
Doador.java
```

---

## DAO

Executa SQL.

Exemplo:

```txt
DoadorDAO.java
```

Métodos esperados:

```txt
salvar()
listar()
atualizar()
deletar()
```

---

## Service

Responsável pelas regras.

Exemplo:

```txt
DoadorService.java
```

---

## View

Responsável pela interface.

Exemplo:

```txt
DoadorView.java
```

---

# Banco de Dados

Toda tabela deve possuir:

* chave primária;
* auto incremento;
* nomes padronizados.

Exemplo:

```sql
CREATE TABLE IF NOT EXISTS doador (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);
```

---

# Cuidados Importantes

## Antes de começar qualquer desenvolvimento

Executar:

```bash
git pull origin develop
```

---

## Nunca subir arquivos desnecessários

Exemplo:

* target/
* .idea/
* arquivos temporários.

---

# Sobre o Maven

O Maven:

* gerencia dependências;
* compila o projeto;
* organiza build;
* automatiza execução.

---

# Sobre JDBC

O JDBC é a tecnologia que permite:

```txt
Java ↔ PostgreSQL
```

Sem JDBC o Java não conseguiria acessar o banco.

---

# Sobre o Railway

O Railway está sendo utilizado como hospedagem do PostgreSQL.

Vantagens:

* banco online;
* compartilhado;
* persistência remota;
* acesso simultâneo.

---

# Objetivo Final do Projeto

O objetivo é desenvolver um sistema completo de gerenciamento de doações utilizando:

* Java;
* PostgreSQL;
* JavaFX;
* arquitetura organizada;
* versionamento com Git.

---

# Observações Finais

* Manter organização do código;
* Evitar alterar arquivos de outros integrantes;
* Sempre atualizar a develop antes de iniciar;
* Utilizar commits descritivos;
* Manter padrão do projeto.

---

# Exemplo de Fluxo Completo

```bash
git checkout develop

git pull origin develop

git checkout -b feature/doador

# desenvolvimento...

git add .

git commit -m "CRUD completo de doador"

git push origin feature/doador
```

---

# Projeto Extensão LL

Desenvolvido para fins acadêmicos utilizando boas práticas de desenvolvimento Java.

# Uso de Enumerações (ENUM)

O professor solicitou o uso de `enum` para deixar o código mais organizado, padronizado e seguro.

## O que é um ENUM?

`enum` é um tipo especial do Java usado para representar valores fixos e controlados.

Exemplo:

```java
public enum StatusDoacao {
    PENDENTE,
    RECEBIDA,
    ENTREGUE,
    CANCELADA
}
```

Ao invés de salvar textos soltos como:

```java
"pendente"
"Pendente"
"PENDENTE"
```

O sistema passa a trabalhar com valores padronizados.

---

## Vantagens do ENUM

### Organização

Evita strings espalhadas pelo sistema.

### Segurança

Impede valores inválidos.

### Padronização

Todos utilizam exatamente os mesmos estados.

### Melhor manutenção

Facilita alterações futuras.

### Melhor leitura de código

O código fica mais profissional e compreensível.

---

## Onde Utilizar ENUM no Projeto

### Status da Doação

```java
public enum StatusDoacao {
    PENDENTE,
    RECEBIDA,
    ENTREGUE,
    CANCELADA
}
```

### Tipo de Usuário

```java
public enum TipoUsuario {
    DOADOR,
    RECEPTOR,
    ADMIN
}
```

### Categoria de Item

```java
public enum CategoriaItem {
    ALIMENTO,
    ROUPA,
    BRINQUEDO,
    HIGIENE,
    OUTROS
}
```

---

## Estrutura Recomendada

Adicionar um novo pacote:

```bash
src/main/java/com/doacoes/enums
```

Exemplo:

```bash
mkdir -p src/main/java/com/doacoes/enums
```

Arquivos:

```bash
StatusDoacao.java
CategoriaItem.java
TipoUsuario.java
```

---

## Exemplo de Uso no Model

```java
private StatusDoacao status;
```

Setter:

```java
public void setStatus(StatusDoacao status) {
    this.status = status;
}
```

Uso:

```java
doacao.setStatus(StatusDoacao.PENDENTE);
```

---

## Como Salvar no Banco

O ENUM pode ser salvo como texto no PostgreSQL:

```sql
status VARCHAR(30)
```

Exemplo ao salvar:

```java
preparedStatement.setString(1, status.name());
```

Exemplo ao recuperar:

```java
StatusDoacao.valueOf(resultado.getString("status"));
```

---

## Importante para a Equipe

Todos os integrantes devem utilizar os mesmos ENUMS.

Nunca criar strings manuais para status, categorias ou tipos.

Sempre importar o enum correspondente:

```java
import com.doacoes.enums.StatusDoacao;
```
