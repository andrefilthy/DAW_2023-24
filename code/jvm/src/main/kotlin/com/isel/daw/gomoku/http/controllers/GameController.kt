package com.isel.daw.gomoku.http.controllers

import com.isel.daw.gomoku.domain.Game
import com.isel.daw.gomoku.domain.User
import com.isel.daw.gomoku.domain.isPlayer1
import com.isel.daw.gomoku.domain.isPlayer2
import com.isel.daw.gomoku.dtos.*
import com.isel.daw.gomoku.services.GameServices
import com.isel.daw.gomoku.services.GameServicesSuccess
import com.isel.daw.gomoku.services.UserServices
import com.isel.daw.gomoku.utils.Either
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(PathTemplate.gameController)
class GameController(private val gameServices : GameServices, private val userServices: UserServices) {

    @GetMapping()
    fun getGameByUser(
        user: User
    ) : ResponseEntity<*> {
        val result = gameServices.getByUser(user)
        when (result) {
            is Either.Success -> {
                val res = result.value as GameServicesSuccess.GameServicesSuccessWithGame
                val game = res.game
                var board = game.board

                val output = GameOutputModel(
                    gameID = game.gameID,
                    resultInfo = res.resInfo,
                    player1 = UserOutputModel(
                        game.player1.username,
                        game.player1.numberOfGames,
                        game.player1.numberOfWins
                    ),
                    player2 = UserOutputModel(
                        game.player2.username,
                        game.player2.numberOfGames,
                        game.player2.numberOfWins
                    ),
                    board = BoardOutputModel(board.toString().split("/")[0]),
                    state = game.currentState,
                    phase = game.currentPhase
                )
                return ResponseEntity.status(res.statusCode).header("Content-Type", "application/vnd.siren+json")
                    .body(output.toSiren())
            }
            is Either.Error -> return ResponseEntity.status(result.value.statusCode)
                .body(ProblemOutputModel(result.value.error, result.value.message, null))
        }
    }

    @GetMapping(PathTemplate.getGame)
    fun getGame(
        user : User,
        @PathVariable gameID: UUID
    ) : ResponseEntity<*> {
        val result = gameServices.getById(gameID)
        when (result) {
            is Either.Success -> {
                val res = result.value as GameServicesSuccess.GameServicesSuccessWithGame
                val game = res.game
                var board = game.board

                val output = GameOutputModel(
                    gameID = game.gameID,
                    resultInfo = res.resInfo,
                    player1 = UserOutputModel(
                        game.player1.username,
                        game.player1.numberOfGames,
                        game.player1.numberOfWins
                    ),
                    player2 = UserOutputModel(
                        game.player2.username,
                        game.player2.numberOfGames,
                        game.player2.numberOfWins
                    ),
                    board = BoardOutputModel(board.toString()),
                    state = game.currentState,
                    phase = game.currentPhase
                )
                return ResponseEntity.status(res.statusCode).body(output.toSiren())
            }

            is Either.Error -> return ResponseEntity.status(result.value.statusCode).body(ProblemOutputModel(result.value.error, result.value.message,null))
        }
    }

    @PostMapping(PathTemplate.start)
    fun startGame(
        user : User,
        @RequestBody g : GameStartInputModel
    ): ResponseEntity<Any> {
        val player = userServices.getByUsername(user.username)
        return when (val result = gameServices.start(player, g.ruleSet)){
            is Either.Success -> {
                if(result.value is GameServicesSuccess.WaitingForPlayer) return ResponseEntity.status(result.value.statusCode).body(result.value.message)
                val res = result.value as GameServicesSuccess.GameServicesSuccessWithGame
                val game = res.game
                var board = game.board

                val outGame = GameOutputModel(
                    gameID = game.gameID,
                    resultInfo = res.resInfo,
                    player1 = UserOutputModel(game.player1.username, game.player1.numberOfGames, game.player1.numberOfWins),
                    player2 = UserOutputModel(game.player2.username, game.player2.numberOfGames, game.player2.numberOfWins),
                    board = BoardOutputModel(board.toString().split("/")[0]),
                    state = game.currentState,
                    phase = game.currentPhase
                )
                ResponseEntity.status(res.statusCode).header("Content-Type", "application/vnd.siren+json").body(outGame.toSiren())
            }
            is Either.Error -> {
                ResponseEntity.status(result.value.statusCode).body(ProblemOutputModel(result.value.error, result.value.message,null))

            }
        }
    }

    @PostMapping(PathTemplate.giveUp)
    fun giveUp(
        user : User,
        @PathVariable gameID : UUID
    ) : ResponseEntity<*> {
        val player = userServices.getByUsername(user.username)
        val result = gameServices.giveUp(gameID, player)
        when (result) {
            is Either.Success -> {
                val res = result.value as GameServicesSuccess.GameServicesSuccessWithGame
                val game = res.game
                var board = game.board

                val output = GameOutputModel(
                    gameID = game.gameID,
                    resultInfo = res.resInfo,
                    player1 = UserOutputModel(game.player1.username, game.player1.numberOfGames, game.player1.numberOfWins),
                    player2 = UserOutputModel(game.player2.username, game.player2.numberOfGames, game.player2.numberOfWins),
                    board = BoardOutputModel(board.toString().split("/")[0]),
                    state = game.currentState,
                    phase = game.currentPhase
                )
                return ResponseEntity.status(res.statusCode).body(output.toSiren())
            }
            is Either.Error -> return ResponseEntity.status(result.value.statusCode)
                .body(ProblemOutputModel(result.value.error, result.value.message, null))
        }
    }

    @PostMapping(PathTemplate.place)
    fun playGame(
        user : User,
        @PathVariable gameID : UUID,
        @RequestBody playInput : GamePlayInputModel
    ) : ResponseEntity<*> {
        val player = userServices.getByUsername(user.username)
        val result = gameServices.playRound(gameID, player, playInput.play)
        when (result) {
            is Either.Success -> {
                val res = result.value as GameServicesSuccess.GameServicesSuccessWithGame
                val game = res.game
                var board = game.board
                val output = GameOutputModel(
                    gameID = game.gameID,
                    resultInfo = res.resInfo,
                    player1 = UserOutputModel(game.player1.username, game.player1.numberOfGames, game.player1.numberOfWins),
                    player2 = UserOutputModel(game.player2.username, game.player2.numberOfGames, game.player2.numberOfWins),
                    board = BoardOutputModel(board.toString()),
                    state = game.currentState,
                    phase = game.currentPhase
                )
                return ResponseEntity.status(res.statusCode).body(output.toSiren())
            }
            is Either.Error ->{
                return ResponseEntity.status(result.value.statusCode).body(ProblemOutputModel(result.value.error, result.value.message, null))
            }
        }

    }
}