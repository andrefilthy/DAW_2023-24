package isel.daw.proj.dtos

import isel.daw.proj.domain.Game
import isel.daw.proj.hypermedia.LinkRelation
import isel.daw.proj.hypermedia.SirenModel
import isel.daw.proj.hypermedia.siren
import org.springframework.http.HttpMethod
import java.net.URI

data class HomeOutputModel(
    val user : UserOutputModel?
){

    fun toSiren() : SirenModel<HomeOutputModel> = siren(this){
        clazz("home")
        if(user != null)  action("start game",URI("/game"),HttpMethod.POST,"application/json"){}
        if(user == null)  link(URI("/register"),LinkRelation("register"))
        if(user == null)  link(URI("/login"),LinkRelation("login"))
        link(URI("/stats"),LinkRelation("statsPage"))
        link(URI("/info"),LinkRelation("infoPage"))
        if(user!= null) entity(user, LinkRelation("user")){
            link(URI("/user/${user.username}"), LinkRelation("self"))
            }
        link(URI("/"), LinkRelation("self"))
    }
}
