package com.isel.daw.gomoku.domain

import com.isel.daw.gomoku.Clock
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class GameLogic(
    private val clock: Clock,
)
{

    fun start(player1 : User, player2 : User, rules : RuleSet) : RoundResult{
        val now = clock.now()
        val game = Game(
            gameID = UUID.randomUUID(),
            ruleSet = rules,
            created = now,
            currentPhase = Game.Phase.PLACING,
            currentState = Game.State.NEXT_PLAYER1,
            player1 = player1,
            player2 = player2,
            board = Board.create(rules.boardSize),
            player1Logic = PlayerLogic.create(1),
            player2Logic = PlayerLogic.create(2),
            turnStartedAt = null
        )
        return RoundResultWithGame.StartPlacingPhase(game)
    }

    fun doPlace(game: Game, round : Round) : RoundResult{
        if(round.player.username != game.player1.username && round.player.username != game.player2.username){
            return EmptyRoundResult.NotAPlayer
        }
        val now = clock.now()
        return when (game.currentState){
            Game.State.PLAYER1_WON -> EmptyRoundResult.GameAlreadyEnded
            Game.State.PLAYER2_WON -> EmptyRoundResult.GameAlreadyEnded
            Game.State.NEXT_PLAYER1 -> applyPlace(game, round, now, game.player1Logic)
            Game.State.NEXT_PLAYER2 -> applyPlace(game, round, now, game.player2Logic)
            else -> EmptyRoundResult.NotAllPlayersAreReady
        }
    }

    private fun applyPlace(
        game : Game,
        round : Round,
        now : Instant,
        playerLogic : PlayerLogic
    ) : RoundResult {
        if (!playerLogic.isTurn(game, round.player)) {
            return EmptyRoundResult.NotYourTurn
        } else {
//            if(now.epochSecond >= game.turnStartedAt!!.epochSecond + game.ruleSet.placingTime){
//                val state = playerLogic.changeTurn(game, round.player)
//                return RoundResultWithGame.TooLate(game.copy(currentState = state, turnStartedAt = now))
//            }
            var board = game.board
            var phase = game.currentPhase
            var state = game.currentState

            if(!board.isPlayable(round.play.l, round.play.c)){
                return EmptyRoundResult.PositionNotAvailable
            }else {
                board = when (board.get(round.play.l, round.play.c)) {
                    CellState.EMPTYCELL ->{
                        if(game.isPlayer1(round.player))
                            board.mutate(CellState.BLACKPIECE, round.play.l, round.play.c) //player 1 has blackpieces
                        else
                            board.mutate(CellState.WHITEPIECE, round.play.l, round.play.c) //player 2 has whitepieces
                    }
                    else -> { //A cell já tem uma peça: condição repetida
                        return EmptyRoundResult.PositionNotAvailable
                    }
                }
                state = playerLogic.changeTurn(game, round.player) //changeTurn
            }
            if(game.isPlayer1(round.player)){
                if(board.hasWon(CellState.BLACKPIECE)) {
                    state = Game.State.PLAYER1_WON
                    phase = Game.Phase.COMPLETED
                    return RoundResultWithGame.GameEnded(game.copy(board = board, currentState = state, currentPhase = phase, player1Logic = playerLogic))
                }
                return RoundResultWithGame.StartPlacingPhase(game.copy(board = board, currentState = state, currentPhase = phase, player1Logic = playerLogic))
            }else{
                if(board.hasWon(CellState.WHITEPIECE)) {
                    state = Game.State.PLAYER2_WON
                    phase = Game.Phase.COMPLETED
                    return RoundResultWithGame.GameEnded(game.copy(board = board, currentState = state, currentPhase = phase, player2Logic = playerLogic))
                }
                return RoundResultWithGame.StartPlacingPhase(game.copy(board = board, currentState = state, currentPhase = phase, player2Logic = playerLogic))
            }
        }
    }
}

interface RoundResult{

}

sealed class RoundResultWithGame(open val game : Game): RoundResult{
    data class TooLate(override val game : Game) : RoundResultWithGame(game)
    data class TimeOut(override val game : Game) : RoundResultWithGame(game)
    data class GameEnded(override val game: Game) : RoundResultWithGame(game)
    data class OthersTurn(override val game: Game) : RoundResultWithGame(game)
    data class StartPlacingPhase(override val game : Game) : RoundResultWithGame(game)
    data class OtherPlayerNotReady(override val game: Game) : RoundResultWithGame(game)
}
sealed class EmptyRoundResult : RoundResult{
    object NotYourTurn : EmptyRoundResult()
    object GameAlreadyEnded : EmptyRoundResult()
    object NotAPlayer : EmptyRoundResult()
    object PositionNotAvailable : EmptyRoundResult()
    object WaitingForOtherPlayer : EmptyRoundResult()
    object NotAllPlayersAreReady : EmptyRoundResult()

}


fun Game.isPlayer1(player: User) = this.player1.username == player.username

fun Game.isPlayer2(player: User) = this.player2.username == player.username