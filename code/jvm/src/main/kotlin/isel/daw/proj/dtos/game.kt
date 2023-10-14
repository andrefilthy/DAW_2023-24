package isel.daw.proj.dtos

import isel.daw.proj.domain.Game
import isel.daw.proj.domain.Play
import isel.daw.proj.hypermedia.LinkRelation
import isel.daw.proj.hypermedia.SirenModel
import isel.daw.proj.hypermedia.siren
import org.springframework.http.HttpMethod
import java.net.URI
import java.util.UUID

data class GamePlayInputModel(
    val play : Play
    )

data class GameStartInputModel(
    val ruleSet: RuleSetInputModel
)

data class RuleSetInputModel(
    val boardSize : Int,
    val fleetComposition: FleetCompositionInputModel,
    val shotsPerRound : Int,
    val layoutTime : Int,
    val shootingTime : Int
)

data class FleetCompositionInputModel(
    val comp : Map<String, Int>
    )

data class GamePositionInputModel(
    val board : BoardInputModel
)

data class BoardInputModel(
    val cells: String
)

data class BoardOutputModel(
    val cells : String,
    val username : String
)

data class GameOutputModel(
    val gameID : UUID,
    val resultInfo : String,
    val player1 : UserOutputModel,
    val player2 : UserOutputModel,
    val board1 : BoardOutputModel,
    val board2 : BoardOutputModel,
    val state : Game.State,
    val phase : Game.Phase
){
    fun toSiren() : SirenModel<GameOutputModel> = siren(this){
        clazz("game")
        if (phase == Game.Phase.SHOOTING) action("play", URI("/game/${gameID}/play"), HttpMethod.POST, "application/json"){}
        if (phase == Game.Phase.PLANNING) action("ready up", URI("/game/$gameID/set"), HttpMethod.POST, "application/json"){}
        link(URI("/game/${gameID}"), LinkRelation("self"))
        entity(player1, LinkRelation("player1")){
            link(URI("/user/${player1.username}"), LinkRelation("self"))
        }
        entity(player2, LinkRelation("player2")){
            link(URI("/user/${player2.username}"), LinkRelation("self"))
        }
    }
}
