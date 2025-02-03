# SpringBoard

Demo project using Spring Boot to build a REST API for playing chess.

## Features

- Chess logic
- Real-time game updates using Server-Sent Events (SSE)
- Secure authentication using JWT
- MongoDB persistence
- Unit tests

## Installation

### Prerequisites

- Java 21
- Maven
- MongoDB
- Git

### Steps

1. Clone the repository.
2. Configure MongoDB connection in ``src/main/resources/application.properties``:
````properties
# MongoDB Connection
spring.data.mongodb.uri=mongodb://localhost:27017/springboard
````

3. Build and run the application:
````bash
mvn clean install
mvn spring-boot:run
````

### Run tests

> [!WARNING]
> Running all Perft-tests can take some time.
````bash
mvn test
````

## API Endpoints

### Games

### Players