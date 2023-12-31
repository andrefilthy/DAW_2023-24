package com.isel.daw.gomoku.services

import com.isel.daw.gomoku.domain.*
import com.isel.daw.gomoku.dtos.RuleSetInputModel
import com.isel.daw.gomoku.repositories.TransactionManager
import com.isel.daw.gomoku.utils.Either
import isel.daw.proj.dtos.WaitingEntry
import org.springframework.stereotype.Component
import java.util.UUID

typealias GameServiceResult = Either<GameServicesError, GameServicesSuccess>
@Component
class GameServices(
    val transactionManager : TransactionManager,
    private val gameLogic: GameLogic
) {

    fun getByUser(user: User) : GameServiceResult{
        val game = transactionManager.run { it.gamesRepository.getByUser(user) }?: return gameNotFound()
        return Either.Success(GameServicesSuccess.GameRetrieved(game))
    }

    fun getById(gameID : UUID) : GameServiceResult {
        val game = transactionManager.run { it.gamesRepository.getById(gameID) }?: return gameNotFound()
        return Either.Success(GameServicesSuccess.GameRetrieved(game))
    }

    private fun gameNotFound(): GameServiceResult {
        return Either.Error(GameServicesError.GameNotFound())
    }

    fun start(player : User, ruleSet: RuleSetInputModel) : GameServiceResult {
        val game = transactionManager.run { it.gamesRepository.getByUser(player) }
        if(game != null) return Either.Success(GameServicesSuccess.GameRetrieved(game))
        val rules = RuleSet(
            boardSize = ruleSet.boardSize,
            //variant = ruleSet.variant,
            //openingRules = ruleSet.openingRules,
            placingTime = ruleSet.placingTime
        )
        var result : RoundResult
        val waitingEntry : WaitingEntry? = transactionManager.run { it.gamesRepository.searchForWaitingEntry(rules) }

        if(waitingEntry != null){
            if(waitingEntry.player1 == player) return Either.Error(GameServicesError.AlreadySearching())
            if(waitingEntry.ruleSet == rules){
                transactionManager.run { it.gamesRepository.deleteEntryFromWaitingList(waitingEntry.player1) }
                result = gameLogic.start(waitingEntry.player1, player, rules)
                if(result is RoundResultWithGame.StartPlacingPhase){
                    transactionManager.run {
                        it.gamesRepository.insert(result.game)
                    }
                    return Either.Success(GameServicesResult.roundToServicesResult(result) as GameServicesSuccess)
                }
            }
        }

        transactionManager.run { it.gamesRepository.insertToWaitingList(WaitingEntry(player, rules)) }
        return Either.Success(GameServicesResult.roundToServicesResult(EmptyRoundResult.WaitingForOtherPlayer) as GameServicesSuccess)
    }

    fun playRound(gameID : UUID, player : User, plays : Play) : GameServiceResult {
        val game = transactionManager.run { it.gamesRepository.getById(gameID) }?:return gameNotFound()
        val result = gameLogic.doPlace(game, Round(player, plays))
        if(result is RoundResultWithGame){
            transactionManager.run {
                it.gamesRepository.update(result.game)
            }
            if(result.game.currentPhase == Game.Phase.COMPLETED){
                val winner = if(result.game.currentState == Game.State.PLAYER1_WON) result.game.player1
                else result.game.player2
                val loser = if(result.game.currentState == Game.State.PLAYER2_WON) result.game.player1
                else result.game.player2
                transactionManager.run {
                    it.userRepository.update(User(winner.username,winner.pwd, winner.numberOfGames+1, winner.numberOfWins+1))
                    it.userRepository.update(User(loser.username,loser.pwd, loser.numberOfGames+1, loser.numberOfWins))
                }
            }
            return Either.Success(GameServicesResult.roundToServicesResult(result) as GameServicesSuccess)
        }
        return Either.Error(GameServicesResult.roundToServicesResult(result) as GameServicesError)
    }
}
