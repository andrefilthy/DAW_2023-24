package com.isel.daw.gomoku.services

import com.isel.daw.gomoku.domain.*
import com.isel.daw.gomoku.dtos.RuleSetInputModel
import com.isel.daw.gomoku.repositories.TransactionManager
import com.isel.daw.gomoku.utils.Either
import org.springframework.stereotype.Component
import java.util.UUID

typealias GameServiceResult = Either<GameServicesError, GameServicesSuccess>
@Component
class GameServices(
    val transactionManager : TransactionManager,
    private val gameLogic: GameLogic
) {
    var waitingList : MutableMap<User, RuleSet> = mutableMapOf()

    fun getById(gameID : UUID) : GameServiceResult {
        val game = transactionManager.run { it.gamesRepository.getById(gameID) }?: return gameNotFound()
        return Either.Success(GameServicesSuccess.GameRetrieved(game))
    }

    private fun gameNotFound(): GameServiceResult {
        return Either.Error(GameServicesError.GameNotFound())
    }

    fun start(player : User, ruleSet: RuleSetInputModel) : GameServiceResult {

        val rules = RuleSet(
            boardSize = ruleSet.boardSize,
            //variant = ruleSet.variant,
            //openingRules = ruleSet.openingRules,
            placingTime = ruleSet.shootingTime
        )
        var result : RoundResult
        for(entry in waitingList){
            if(entry.key == player) return Either.Error(GameServicesError.AlreadySearching())
            if(entry.value == rules){
                waitingList.remove(entry.key)
                result = gameLogic.start(entry.key, player, rules)
                if(result is RoundResultWithGame.StartPlacingPhase){
                    transactionManager.run {
                        it.gamesRepository.insert(result.game)
                    }
                    return Either.Success(GameServicesResult.roundToServicesResult(result) as GameServicesSuccess)
                }
            }
        }

        waitingList[player] = rules
        return Either.Success(GameServicesSuccess.WaitingForPlayer())
    }

    fun playRound(gameID : UUID, player : User, plays : Play) : GameServiceResult {
        val game = transactionManager.run { it.gamesRepository.getById(gameID) }?:return gameNotFound()
        val result = gameLogic.doPlace(game, Round(player, plays))
        if(result is RoundResultWithGame){
            transactionManager.run {
                it.gamesRepository.update(result.game)
            }
            return Either.Success(GameServicesResult.roundToServicesResult(result) as GameServicesSuccess)
        }
        return Either.Error(GameServicesResult.roundToServicesResult(result) as GameServicesError)
    }
}
