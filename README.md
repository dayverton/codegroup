# Aplicação web para gerenciamento de projetos

Aplicação Java com JSP, Ajax, empacotamento WAR, testes unitários e banco PostgreSQL embutido.

## Requisitos Necessários

- O **Docker** precisa estar instalado na sua máquina local.

## Construir a Imagem e Rodar o Container

Do diretório raiz execute os seguintes comandos no terminal para construir a imagem e iniciar o container:

```bash
cd codegroup\java\retro-jira
docker-compose up --build
```
## Como acessar

A porta `8080` foi exposta para permitir acessar a aplicação Java através do **Swagger**. Acesse a API pelo seguinte endereço:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

E para acessar a página:

[http://localhost:8080/projetos](http://localhost:8080/projetos)

