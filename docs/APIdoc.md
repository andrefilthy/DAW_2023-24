# Gomoku API

## Introduction

The Gomoku API allows you to play the classic game of gomoku, with different variants and opening rules.

Every request which requires a body should also have ```application/json``` as its ```Content-Type``` header.

The API is described in the following points. Anything delimited by [] should be replaced by the respective value for the variable

## Home Page
```
GET /
```
A GET request to the API's base URL will return a [Siren Object](https://github.com/kevinswiber/siren) which contains all the information necessary for the user to navigate to other API resources. The ```Content-Type``` for the response is ```application/vnd.siren+json```. The Siren object returned is different depending on whether the request was performed by an authenticated or unauthenticated user.

The Siren object returned for a request performed by an unauthenticated user looks like this:
```
{
    "class": [
        "home"
    ],
    "properties": {
        "user": null
    },
    "links": [
        {
            "rel": [
                "register"
            ],
            "href": "/register"
        },
        {
            "rel": [
                "login"
            ],
            "href": "/login"
        },
        {
            "rel": [
                "statsPage"
            ],
            "href": "/stats"
        },
        {
            "rel": [
                "infoPage"
            ],
            "href": "/info"
        },
        {
            "rel": [
                "self"
            ],
            "href": "/"
        }
    ],
    "entities": [],
    "actions": []
}
```
An authenticated user will have the ability to start a game, as such, the Siren object returned will have a Start Game action which will possibilitate the matchmaking service.

## Statistics Page and Info Page
The statistics page and the info page can be accessed by authenticated and unauthenticated users with very little difference on the response, both being able to access the relevant data.

Accessing the statistics page:
```
GET /stats
```
This page doesn't show any relevant information yet. The plan is that in the future it will show a ranking with the top players, ordering them by the number of wins.

Accessing the info page:
```
GET /info
```
This page will return a response with a Siren object that contains the relevant navigation info together with information on the API and the authors.



## Authentication Process
For users to be able play games they will have to register, that is if they don't already have an account to login. This process will associate a user with a token giving them permission, while the token is still valid, to play games against other users.

### Register & Login
If the user does not have an account and wants to create one, he will have to execute the following request:
```
POST /register
```
If the user already has an account and wants to login then he should send the following request:
```
POST /login
```

For both these operations, the data sent in the request body should look like this:
```
{
    "username":"daw",
    "password":"changeit"
}
```
After successfully executing these requests the user should expect a response carrying his token with the respective format:
```
{
    "token_type": "Bearer",
    "access_token": "lXm4vnJxPX0xTSsMDQK_ayWkk-1AepN7QS0uRTtQTfk=",
    "expires_on": 1698404722
}

```



## Game Functionalities
After being authenticated the user can access all functionalities related to starting and playing a game of gomoku.

### Start a Game
For this request the user has to provide a ```ruleSet```, this ruleSet will determine the rules of a game. The ruleSet must be composed by:
- boardSize : Describes the size of the square board
- placingTime : The number of seconds the user has to place his piece
```
POST /game
{
    "ruleSet":
    {
        "boardSize":15,
        "placingTime":120
    }
}
```
After successfully executing this request the user can get one of two responses:

#### Waiting for another player
If it needs to wait for another player to start a game with the same RuleSet input then it will show a text message like this:
```
Waiting for the other player to ready up
```
#### Found a game 
If the API immediately finds a game that has the same rules as the user requested, the API will immediately create a game, insert it into the database, and return a response containing a Siren object containing the Game object
```
{
    "class": [
        "game"
    ],
    "properties": {
        "gameID": "036d121a-a197-4d0a-b966-c13246246f26",
        "resultInfo": "PlacingStarted",
        "player1": {
            "username": "daw",
            "numberOfGames": 0,
            "numberOfWins": 0
        },
        "player2": {
            "username": "daw2",
            "numberOfGames": 0,
            "numberOfWins": 0
        },
        "board": {
            "cells": "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
        },
        "state": "NEXT_PLAYER1",
        "phase": "PLACING"
    },
    "links": [
        {
            "rel": [
                "self"
            ],
            "href": "/game/036d121a-a197-4d0a-b966-c13246246f26"
        }
    ],
    "entities": [
        {
            "properties": {
                "username": "daw",
                "numberOfGames": 0,
                "numberOfWins": 0
            },
            "links": [
                {
                    "rel": [
                        "self"
                    ],
                    "href": "/user/daw"
                }
            ],
            "rel": [
                "player1"
            ]
        },
        {
            "properties": {
                "username": "daw2",
                "numberOfGames": 0,
                "numberOfWins": 0
            },
            "links": [
                {
                    "rel": [
                        "self"
                    ],
                    "href": "/user/daw2"
                }
            ],
            "rel": [
                "player2"
            ]
        }
    ],
    "actions": [
        {
            "name": "play",
            "href": "/game/036d121a-a197-4d0a-b966-c13246246f26/play",
            "method": "POST",
            "type": "application/json",
            "fields": []
        }
    ]
}
```


### Set Position
Once you've found an opponent and your game has started, it's time to actually start playing the game!
The player that starts placing first is always the player that had to wait for the another player for the game to start (at least for now).
Player 1 always has the black pieces, while Player 2 has the white pieces.
There are 3 different cell types:
```
'-' -> EMPTYCELL
'W' -> WHITEPIECE
'B' -> BLACKPIECE
```
To place a piece in the board it is required that the player is placing the piece on his turn, if it isn't his turn then it will send a response message stating that the player can't place a piece if it is not his turn.
```
POST /game/[gameID]/set
```
The body has to be a JSON with the board position where the player wants to place his piece:
```
{
    "play":
    {
        "l":4,
        "c":4
    }
}
```
After setting your position, there are two possibilities, if the correct player placed his piece, then the response will contain a Siren Object similar to the one received when the game starts but with the updated board, current phase and state. If a player tried to place a piece while its not his turn, then it will send JSON response informing of that, as said above.
```
{
    "class": [
        "game"
    ],
    "properties": {
        "gameID": "036d121a-a197-4d0a-b966-c13246246f26",
        "resultInfo": "PlacingStarted",
        "player1": {
            "username": "daw",
            "numberOfGames": 0,
            "numberOfWins": 0
        },
        "player2": {
            "username": "daw2",
            "numberOfGames": 0,
            "numberOfWins": 0
        },
        "board": {
            "cells": "----------------------------------------------------------------B----------------------------------------------------------------------------------------------------------------------------------------------------------------"
        },
        "state": "NEXT_PLAYER2",
        "phase": "PLACING"
    },
    "links": [
        {
            "rel": [
                "self"
            ],
            "href": "/game/036d121a-a197-4d0a-b966-c13246246f26"
        }
    ],
    "entities": [
        {
            "properties": {
                "username": "daw",
                "numberOfGames": 0,
                "numberOfWins": 0
            },
            "links": [
                {
                    "rel": [
                        "self"
                    ],
                    "href": "/user/daw"
                }
            ],
            "rel": [
                "player1"
            ]
        },
        {
            "properties": {
                "username": "daw2",
                "numberOfGames": 0,
                "numberOfWins": 0
            },
            "links": [
                {
                    "rel": [
                        "self"
                    ],
                    "href": "/user/daw2"
                }
            ],
            "rel": [
                "player2"
            ]
        }
    ],
    "actions": [
        {
            "name": "play",
            "href": "/game/036d121a-a197-4d0a-b966-c13246246f26/play",
            "method": "POST",
            "type": "application/json",
            "fields": []
        }
    ]
}
```



### Get Game
While placing a piece is an operation reserved for players participating in the game, retrieving a game's information can be performed by any authenticated user, with the request:
```
GET /game/[gameID]
```
The response will contain a Siren Object similar to the one shown in the Start Game section, with all the important information about the game in question.


## Common Errors



### Authentication-related Errors
For now, ProblemOutputModel is being used to manage the errors and send detailed responses with the error and message. The objective is to implement error management with application/problem+json, presenting the error types and custom fields for each type.

#### UserAlreadyExists
* status code: 409
* body:
    ```
    {
        "error": "UserAlreadyExists",
        "message": "Username already exists"
    }
    ```
This error will occur when a user tries to register with a username that is already present in the database.

#### WeakPasswordError
* status code: 400
* body:
    ```
    {
        "error": "WeakPasswordError",
        "message": "Your password is too weak"
    }
    ```
This error will occur when the password received from the request does not fulfill the minimum security requirements.


#### UserNotFound
* status code: 404
* body:
    ```
    {
        "error": "UserNotFound",
        "message": "User with given Username was not found"
    }
    ```
This error will occur when the user logins with  a username that is not yet present in the database.

#### WrongPassword
* status code: 401
* body:
    ```
    {
        "error": "WrongPassword",
        "message": "User password does not match"
    }
    ```
This error will occur when the password inserted does not correspond to the password of the user in the database.

### Game-related Errors
#### GameNotFound
* status code: 404
* body:
    ```
    {
        "error": "GameNotFound",
        "message": "The specified game could not be retrieved"
    }
    ```
This error will occur when the game specified in the [gameID] field cannot be found in the database, please make sure the ID is correct.
#### NotAPlayer
* status code: 403
* body:
    ```
    {
        "error": "NotAPlayer",
        "message": "Request by a player who is not playing the game"
    }
    ```
This error will occur when a player that is not participating in the game tries to place a piece in the board. 
#### NotAValidPlay
* status code: 403
* body:
    ```
    {
        "error": "InvalidPlay",
        "message": "The specified position is not a valid playing position"
    }
    ```
This error will occur when a player tries to play in a position which is not valid for a play (a piece is already on that position). Need to implement a condition for this error that if the player tries to place a piece in a position that exceeds the board dimensions.
#### GameHasEnded
* status code: 403
* body:
    ```
    {
        "error": "GameEnded",
        "message": "The game has finished and the operation is not valid for a finished game"
    }
    ```
This error will occur when trying to perform any action on an already completed game.
#### NotYourTurn
* status code: 403
* body:
    ```
    {
        "error": "NotYourTurn",
        "message": "It is not your turn to play"
    }
    ```
This error will occur when a player tries to perform a shot when it is not their turn. Make sure the opponent's turn has ended before trying to shoot.
#### AlreadySearching
* status code: 403
* body:
    ```
    {
        "error": "AlreadySearching",
        "message": "You are already searching for a game, cannot perform that action"
    }
    ```
This error will occur when a player who is already searching for a game tries searching again. You can only search for one game at the time.