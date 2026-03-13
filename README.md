# Library Management System - Backend

This is the **backend service** for the Library Management System built using **Spring Boot**.
It provides REST APIs for managing users, books, and borrowing operations.

## Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* JPA / Hibernate
* PostgreSQL / Supabase
* Maven

## Features

* User registration and login
* JWT-based authentication
* Role-based access (Admin / User)
* Add, update, delete books
* Issue and return books
* RESTful APIs

## Running the Application

1. Clone the repository

git clone https://github.com/your-username/library-management-backend.git

2. Navigate to project folder

cd library-management-backend

3. Run the application

mvn spring-boot:run

The server will start on:

http://localhost:8080

## API Example

GET /books
Returns all available books.

POST /auth/login
Authenticates a user and returns a JWT token.


