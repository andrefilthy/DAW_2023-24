package isel.daw.proj.dtos

import isel.daw.proj.hypermedia.LinkRelation
import isel.daw.proj.hypermedia.SirenModel
import isel.daw.proj.hypermedia.siren
import org.springframework.http.HttpMethod
import java.net.URI
import java.time.Instant

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
    val users : List<UserOutputModel>
){
    fun toSiren() : SirenModel<UserListOutputModel> = siren(this){
        clazz("ranking")
        action("home",URI("/"),HttpMethod.GET,"application/json"){}
        action("getMore",URI("/stats"),HttpMethod.GET,"application/json"){}
        for(user in users){
            entity(user, LinkRelation("/user")){
                link(URI("/user/${user.username}"), LinkRelation("self"))
            }
        }

        link(URI("/stats"), LinkRelation("self"))
    }
}