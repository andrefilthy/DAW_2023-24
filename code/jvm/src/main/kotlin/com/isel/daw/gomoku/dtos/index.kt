package com.isel.daw.gomoku.dtos

import com.isel.daw.gomoku.hypermedia.LinkRelation
import com.isel.daw.gomoku.hypermedia.SirenModel
import com.isel.daw.gomoku.hypermedia.siren
import org.springframework.http.HttpMethod
import java.net.URI

data class HomeOutputModel(
    val user : UserOutputModel?
){

    fun toSiren() : SirenModel<HomeOutputModel> = siren(this){
        clazz("home")
        if(user != null)  action("start game",URI("/game"),HttpMethod.POST,"application/json"){}
        if(user != null)  action("delete from waiting list",URI("/game"),HttpMethod.DELETE,"application/json"){}
        if(user == null)  link(URI("/register"),LinkRelation("register"))
        if(user == null)  link(URI("/login"),LinkRelation("login"))
        link(URI("/stats"),LinkRelation("stats"))
        link(URI("/info"),LinkRelation("info"))
        if(user!= null) entity(user, LinkRelation("user")){
            link(URI("/user/${user.username}"), LinkRelation("self"))
        }
        link(URI("/"), LinkRelation("self"))
    }
}
