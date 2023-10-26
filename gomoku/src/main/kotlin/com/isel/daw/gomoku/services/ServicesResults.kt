package com.isel.daw.gomoku.services

import com.isel.daw.gomoku.domain.EmptyRoundResult
import com.isel.daw.gomoku.domain.Game
import com.isel.daw.gomoku.domain.RoundResult
import com.isel.daw.gomoku.domain.RoundResultWithGame

open class GameServicesResult{

    companion object{
        fun roundToServicesResult(result : RoundResult) : GameServicesResult {
            return when (result){
                is RoundResultWithGame.TooLate -> GameServicesSuccess.GameServicesSuccessWithGame.PlacingTimeout(result.game, "PlacingTimeout", 200)
                is RoundResultWithGame.GameEnded -> GameServicesSuccess.GameServicesSuccessWithGame.PlaySuccessful(result.game, "GameCompleted", 200)
                is RoundResultWithGame.OtherPlayerNotReady -> GameServicesSuccess.GameServicesSuccessWithGame.PositionDefined(result.game, "WaitingForOtherPlayer", 200)
                is RoundResultWithGame.StartPlacingPhase -> GameServicesSuccess.GameServicesSuccessWithGame.PositionDefined(result.game, "PlacingDone", 200)
                is RoundResultWithGame.OthersTurn -> GameServicesSuccess.GameServicesSuccessWithGame.PlaySuccessful(result.game, "TurnChanged", 200)
                is EmptyRoundResult.NotAPlayer -> GameServicesError.NotAPlayer()
                is EmptyRoundResult.PositionNotAvailable -> GameServicesError.NotAValidPlay()
                is EmptyRoundResult.GameAlreadyEnded -> GameServicesError.GameHasEnded()
                is EmptyRoundResult.NotYourTurn -> GameServicesError.NotYourTurn()
                is EmptyRoundResult.WaitingForOtherPlayer -> GameServicesSuccess.WaitingForPlayer()
                else -> GameServicesResult()
            }
        }
    }

}

sealed class GameServicesError(
    open val error : String,
    open val message : String,
    open val statusCode : Int


) : GameServicesResult(){
    class GameNotFound() : GameServicesError(
        "GameNotFound", "The specified game could not be retrieved", 404
    )
    class NotAPlayer() : GameServicesError(
        "NotAPlayer", "Request by a player who is not playing the game", 403)
    class NotAValidPlay() : GameServicesError(
        "InvalidPlay", "The specified position is not a valid playing position", 403)
    class GameHasEnded() : GameServicesError(
        "GameEnded", "The game has finished and the operation is not valid for a finished game", 403)
    class NotYourTurn() : GameServicesError(
        "NotYourTurn", "It is not your turn to play", 403)
    class AlreadySearching() : GameServicesError(
        "AlreadySearching", "You are already searching for a game, cannot perform that action", 403
    )
}

sealed class GameServicesSuccess(
    open val statusCode: Int
) : GameServicesResult(){

    data class WaitingForPlayer(val message: String, override val statusCode: Int) : GameServicesSuccess(statusCode)


    companion object {

        fun GameRetrieved(game: Game): GameServicesSuccessWithGame.GameRetrieved {
            return GameServicesSuccessWithGame.GameRetrieved(game, "GameRetrieved", 200)
        }
        fun WaitingForPlayer() : WaitingForPlayer{
            return WaitingForPlayer("Waiting for the other player to ready up", 200)
        }
    }
    sealed class GameServicesSuccessWithGame(
        open val game : Game,
        open val resInfo : String,
        override val statusCode: Int
    ) : GameServicesSuccess(statusCode){
        data class GameRetrieved(override val game : Game, override val resInfo : String, override val statusCode: Int) : GameServicesSuccessWithGame(game, resInfo, statusCode)
        data class GameStarted(override val game: Game, override val resInfo : String, override val statusCode: Int) : GameServicesSuccessWithGame(game, resInfo, statusCode)
        data class PositionDefined(override val game: Game, override val resInfo : String, override val statusCode: Int) : GameServicesSuccessWithGame(game, resInfo, statusCode)
        data class PlaySuccessful(override val game : Game, override val resInfo : String, override val statusCode: Int) : GameServicesSuccessWithGame(game, resInfo, statusCode)
        data class PlacingTimeout(override val game : Game, override val resInfo : String, override val statusCode: Int) : GameServicesSuccessWithGame(game, resInfo, statusCode)

    }
}