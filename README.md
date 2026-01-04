# Estate

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

## Start the project

Git clone:

> git clone https://github.com/OpenClassrooms-Student-Center/P3-Full-Stack-portail-locataire

Go inside folder:

> cd P3-Full-Stack-portail-locataire

Install dependencies:

> npm install

Launch Front-end:

> npm run start;


## Ressources

### Mockoon env

Download Mockoon here: https://mockoon.com/download/

After installing you could load the environement

> ressources/mockoon/rental-oc.json

directly inside Mockoon 

> File > Open environmement

For launching the Mockoon server click on play bouton

Mockoon documentation: https://mockoon.com/docs/latest/about/

### Postman collection

For Postman import the collection

> ressources/postman/rental.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

## Back-end (Spring Boot)

Prerequisites:
- Java 17
- MySQL running locally

### Start the API

Create the database + tables:

> mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS rental_db;"
>
> Get-Content .\ressources\sql\script.sql \| mysql -u root -p rental_db

Set environment variables (PowerShell, recommended):

> $env:DB_USER="root"
>
> $env:DB_PASSWORD="your_password"
>
> $env:JWT_SECRET="change_me_change_me_change_me_change_me"  # >= 32 chars

If you see `Error: connect ECONNREFUSED 127.0.0.1:3001` from the front-end proxy, the API is not running (or it crashed at startup). Start it from `.\backend` and check the console output (missing `JWT_SECRET`, MySQL not running, wrong DB credentials, etc.).

Run the API (port `3001`, base path `/api`):

> cd .\backend
>
> .\mvnw.cmd spring-boot:run


