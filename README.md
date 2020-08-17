# Customer Manager
Projeto backend utilizando a stack java __spring boot__, para o cadastro de clientes.

## Features
- Cadastro de clientes :heavy_check_mark:

## Build e run dos testes :computer:
Para efetuar o build e o run dos testes iremos utilizar o __maven__.

`mvn clean install`

## Rodando o banco de dados com docker-compose :computer:
Para dar o start no banco de dados, basta apenas executar o comando:

`docker-compose -f docker-db.yml up`

## Rodando a aplicação via maven :computer:
Para executar a aplicação pelo maven basta utilizar o comando:

`mvn spring-boot:run`

A aplicação estará disponível e rodando na porta `8090`

## Criando a imagem do docker da aplicação :scroll:
Se desejar criar a imagem do docker da aplicação, utilize o comando:

`docker build -t customermanager:0.0.1 .`

## Rodando a aplicação com docker-compose :computer:
Depois da imagem criada, para dar o start na aplicação basta apenas executar o comando:

`docker-compose -f docker-app.yml up`

A aplicação estará disponível e rodando na porta `8090`

## Utilizando a aplicação
Para efetuar a consulta dos *endpoints* pode ser usado o postman [Insomnia](https://www.postman.com/downloads/). 
O arquivo com as Collecions/Requests é [Customer.postman_collection.json](/Customer.postman_collection.json), para utilizar
basta você importar este arquivo pro seu __Postman__ e seu ambiente estará preparado.

Também é possível utilizar a API através de seu Swagger `http://localhost:8090/swagger-ui.html`
