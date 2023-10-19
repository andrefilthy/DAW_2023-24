package com.isel.daw.gomoku.repositories.jdbi

import com.isel.daw.gomoku.domain.*
import com.isel.daw.gomoku.repositories.GamesRepository
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.mapper.Nested
import java.time.Instant
import java.util.*

class JdbiGameRepository(private val handle : Handle) : GamesRepository {
    override fun getById(id: UUID): Game? =
        handle.createQuery(
            "SELECT games.gameID, games.ruleSet, games.created, games.currentPhase, games.currentState, games.board1, games.board2, games.player1_logic, games.player2_logic, games.turnStartedAt, " +
                    "player1.username as player1_username, player1.pwd as player1_pwd, player1.numberOfGames as player1_numberOfGames, player1.numberOfWins as player1_numberOfWins, " +
                    "player2.username as player2_username, player2.pwd as player2_pwd, player2.numberOfGames as player2_numberOfGames, player2.numberOfWins as player2_numberOfWins" +
                    " FROM dbo.game games " +
                    "inner join dbo.User player1 on games.player1 = player1.username " +
                    "inner join dbo.User player2 on games.player2 = player2.username " +
                    "WHERE gameID = :id")
            .bind("id", id)
            .mapTo(GameDbModel::class.java)
            .singleOrNull()
            ?.run { toGame() }

    override fun update(game: Game) {
        handle.createUpdate("update dbo.game set board=:board, currentState =:state, currentPhase =:phase, player1_logic =:p1Logic, player2_logic =:p2Logic, turnStartedAt = :turnStartedAt where gameID=:id")
            .bind("id",game.gameID)
            .bind("board", game.board.toString())
            .bind("phase", game.currentPhase)
            .bind("state", game.currentState)
            .bind("p1Logic", game.player1Logic.toString())
            .bind("p2Logic", game.player2Logic.toString())
            .bind("turnStartedAt", game.turnStartedAt?.epochSecond)
            .execute()
    }


    override fun insert(game: Game) {
        handle.createUpdate(
            "insert into dbo.game(gameID, ruleSet, created, currentPhase, currentState, player1, player2, board1, board2, player1_logic, player2_logic, turnStartedAt) values (:id, :ruleSet, :created, :phase, :state, :player1 , :player2, :board1, :board2, :p1Logic, :p2Logic, :turnStartedAt)")
            .bind("id",game.gameID)
            .bind("ruleSet", game.ruleSet.toString())
            .bind("created", game.created.epochSecond)
            .bind("state", game.currentState)
            .bind("phase", game.currentPhase)
            .bind("board", game.board.toString())
            .bind("player1", game.player1.username)
            .bind("player2", game.player2.username)
            .bind("p1Logic", game.player1Logic.toString())
            .bind("p2Logic", game.player2Logic.toString())
            .bind("turnStartedAt", game.turnStartedAt?.epochSecond)

            .execute()
    }

}
class GameDbModel(
    val gameID: UUID,
    val ruleSet: RuleSet,
    val created : Instant,
    val currentPhase: Game.Phase,
    val currentState: Game.State,
    val board : Board,
    val player1Logic: PlayerLogic,
    val player2Logic: PlayerLogic,
    val turnStartedAt : Instant?,
    @Nested("player1")
    val player1: User,
    @Nested("player2")
    val player2: User,
){
    fun toGame() = Game(
        gameID = gameID,
        ruleSet = ruleSet,
        created = created,
        currentPhase = currentPhase,
        currentState = currentState,
        player1 = player1,
        player2 = player2,
        board = board,
        player1Logic = player1Logic,
        player2Logic = player2Logic,
        turnStartedAt = turnStartedAt
    )
}