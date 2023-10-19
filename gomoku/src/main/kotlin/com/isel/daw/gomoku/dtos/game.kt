package com.isel.daw.gomoku.dtos

import com.isel.daw.gomoku.domain.Game
import com.isel.daw.gomoku.domain.Play
import com.isel.daw.gomoku.hypermedia.LinkRelation
import com.isel.daw.gomoku.hypermedia.SirenModel
import com.isel.daw.gomoku.hypermedia.siren
import org.springframework.http.HttpMethod
import java.net.URI
import java.util.*

data class GamePlayInputModel(
    val play : Play
)

data class GameStartInputModel(
    val ruleSet: RuleSetInputModel
)

data class RuleSetInputModel(
    val boardSize : Int,
    //val variant : Variant,
    //val openingRules : OpeningRules,
    val shootingTime : Int
)

data class GamePositionInputModel(
    val board : BoardInputModel
)

data class BoardInputModel(
    val cells: String
)

data class BoardOutputModel(
    val cells : String
)

data class GameOutputModel(
    val gameID : UUID,
    val resultInfo : String,
    val player1 : UserOutputModel,
    val player2 : UserOutputModel,
    val board : BoardOutputModel,
    val state : Game.State,
    val phase : Game.Phase
){
    fun toSiren() : SirenModel<GameOutputModel> = siren(this){
        clazz("game")
        if (phase == Game.Phase.PLACING) action("play", URI("/game/${gameID}/play"), HttpMethod.POST, "application/json"){}
        link(URI("/game/${gameID}"), LinkRelation("self"))
        entity(player1, LinkRelation("player1")){
            link(URI("/user/${player1.username}"), LinkRelation("self"))
        }
        entity(player2, LinkRelation("player2")){
            link(URI("/user/${player2.username}"), LinkRelation("self"))
        }
    }
}