package isel.daw.proj.domain

import java.time.Instant
import java.util.*


data class Game(
    val gameID : UUID,
    val ruleSet : RuleSet,
    val created : Instant,
    val currentPhase : Phase,
    val currentState : State,
    val player1 : User,
    val player2 : User,
    val board1 : Board,
    val board2 : Board,
    val player1Logic : PlayerLogic,
    val player2Logic : PlayerLogic,
    val turnStartedAt: Instant?
){

    enum class Phase(val phase : String) {
        PLANNING("Planning"),
        COMPLETED("Completed")
    }

    enum class State{
        PLAYER1_WAITING,
        PLAYER2_WAITING,
        NEXT_PLAYER1,
        NEXT_PLAYER2,
        PLAYER1_WON,
        PLAYER2_WON
    }
}