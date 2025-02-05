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

## Games

## ``GET /api/games``

Retrieves all games

## ``GET /api/games/{gameId}``

Retrieves a specific game

## ``POST /api/games``

Creates a new game

### Request

### Header

Expects httpOnly cookie in header with JWT

| name        | value          |
|-------------|----------------|
| accessToken | JSON Web Token |

### Body

| key   | type   | valid values | required |
|-------|--------|--------------|----------|
| color | String | white, black | no       |

If the body is emtpy or the value is neither "white" nor "black", the default
color will be white.

## ``PUT /api/games/{gameId}``

Joins an existing game

### Request

### Header

Expects httpOnly cookie in header with JWT

| name        | value          |
|-------------|----------------|
| accessToken | JSON Web Token |

## ``PUT /api/games/{gameId}/moves``

Play a move in a game

### Request

### Header

Expects httpOnly cookie in header with JWT

| name        | value          |
|-------------|----------------|
| accessToken | JSON Web Token |

### Body

| key  | type   | example | required |
|------|--------|---------|----------|
| move | String | e2e4    | yes      |

The value needs to be a move in the "pure coordinate notation". Otherwise, 
it will be an illegal move.

## ``GET /api/games/{gameId}/moves``

Retrieves all moves made in a specific game

## ``GET /api/games/{gameId}/events``

Subscribes to a Server-Sent-Event stream sending updates of a game

## Players