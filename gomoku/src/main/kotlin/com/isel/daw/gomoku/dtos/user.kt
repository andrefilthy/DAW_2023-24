package com.isel.daw.gomoku.dtos

import com.isel.daw.gomoku.hypermedia.LinkRelation
import com.isel.daw.gomoku.hypermedia.SirenModel
import com.isel.daw.gomoku.hypermedia.siren
import org.springframework.http.HttpMethod
import java.net.URI

data class UserInputModel(
    val username: String,
    val password: String
)

data class UserOutputModel(
    val username: String,
    val numberOfGames : Int,
    val numberOfWins : Int
){
    //TODO("Siren output model")
}

data class TokenOutputModel(
    val token_type : String,
    val access_token : String,
    val expires_on : Long
)

data class UserListOutputModel(
    val users : List<UserOutputModel>,
    val size : Int,
    val offset : Int,
    val numberOfPlayers : Int
){
    fun toSiren() : SirenModel<UserListOutputModel> = siren(this){
        clazz("ranking")
        if(offset>=size)
            action("prevPage",URI("/stats?size=${size}&offset=${offset-size}"),HttpMethod.GET,"application/json"){}
        if(offset + size < numberOfPlayers)
            action("nextPage",URI("/stats?size=${size}&offset=${offset+size}"),HttpMethod.GET,"application/json"){}
        link(URI("/stats?size=${size}&offset=${offset}"), LinkRelation("self"))
        link(URI("/"), LinkRelation("home"))
        link(URI("/info"), LinkRelation("info"))
    }
}