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

### Example response

````json
[
    {
        "id": "679ea90e043bb37534b91b2e",
        "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
        "state": "WAITING_FOR_PLAYER_TO_JOIN",
        "playerWhite": {
            "username": "test",
            "id": "6733c6c89fe0365287f71878"
        },
        "playerBlack": null,
        "moves": []
    },
    {
        "id": "677c3065bb49272438de7be9",
        "fen": "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2",
        "state": "ONGOING",
        "playerWhite": {
            "username": "max",
            "id": "6735250f6b543068d539dca1"
        },
        "playerBlack": {
            "username": "test",
            "id": "6733c6c89fe0365287f71878"
        },
        "moves": [
            "e2e4",
            "d7d5"
        ]
    }
]
````

## ``GET /api/games/{gameId}``

Retrieves a specific game

### Example response

````json
{
    "id": "679ea90e043bb37534b91b2e",
    "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    "state": "WAITING_FOR_PLAYER_TO_JOIN",
    "playerWhite": {
        "username": "max",
        "id": "6735250f6b543068d539dca1"
    },
    "playerBlack": null,
    "moves": []
}
````

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

#### Example

````json
{
    "color": "white"
}
````

### Example response

````json
{
    "id": "67a3b3051a645a69fc9c8a97",
    "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    "state": "WAITING_FOR_PLAYER_TO_JOIN",
    "playerWhite": {
        "username": "test",
        "id": "6733c6c89fe0365287f71878"
    },
    "playerBlack": null,
    "moves": []
}
````

## ``PUT /api/games/{gameId}``

Joins an existing game

### Request

### Header

Expects httpOnly cookie in header with JWT

| name        | value          |
|-------------|----------------|
| accessToken | JSON Web Token |

### Example response

````json
{
    "id": "67a3b3051a645a69fc9c8a97",
    "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    "state": "ONGOING",
    "playerWhite": {
        "username": "test",
        "id": "6733c6c89fe0365287f71878"
    },
    "playerBlack": {
        "username": "max",
        "id": "6735250f6b543068d539dca1"
    },
    "moves": []
}
````

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

#### Example

````json
{
    "move": "e2e4"
}
````

## ``GET /api/games/{gameId}/moves``

Retrieves all moves made in a specific game.

### Example response

````json
[
    "e2e4",
    "e7e5"
]
````

## ``GET /api/games/{gameId}/events``

Subscribes to a Server-Sent-Event stream sending updates of a game. It provides 2 channels (move and join),
where all data is sent as Strings.

| channel | sends data when     | sends what data                  | example data |
|---------|---------------------|----------------------------------|--------------|
| move    | Player makes a move | Move in pure coordinate notation | e2e4         |
| join    | Player joins a game | Username of player that joined   | max          |


## Players

## ``GET /api/players``

Retrieves all players.

## Example response

````json
[
    {
        "username": "test",
        "id": "6733c6c89fe0365287f71878"
    },
    {
        "username": "max",
        "id": "6735250f6b543068d539dca1"
    }
]
````

## ``GET /api/players/{playerId}``

Retrieves one specific player.

## Example response

````json
{
    "username": "test",
    "id": "6733c6c89fe0365287f71878"
}
````