package com.isel.daw.gomoku.http.controllers

import com.isel.daw.gomoku.hypermedia.LinkRelation
import com.isel.daw.gomoku.hypermedia.SirenModel
import com.isel.daw.gomoku.hypermedia.siren
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping(PathTemplate.infoController)
class InfoController {


    @GetMapping
    fun getAuthorInfo() : ResponseEntity<SirenModel<InfoModel>> {
        return ResponseEntity.status(200).body(infoModel.toSiren())
    }


    companion object {
        private val author1 = AuthInfo(
            "Jorge Reis",
            "a44771@isel.alunos.pt",
            44771

        )
        private val author2 = AuthInfo(
            "Hugo Martins",
            "44832@isel.alunos.pt",
            44832

        )
        private val author3 = AuthInfo(
            "André Ramalho",
            "44799@isel.alunos.pt",
            44799

        )
        private val authorList : ArrayList<AuthInfo> = arrayListOf(author1, author2, author3)

        private val infoModel = InfoModel(
            "Gomoku",
            "Gomoku board game",
            "v1.0.0",
            authorList
        )
    }

    data class InfoModel(
        val name : String,
        val description : String,
        val version : String,
        val authors : List<AuthInfo>
    ){
        fun toSiren() : SirenModel<InfoModel> = siren(this){
            clazz("info")
            link(URI("/"), LinkRelation("home"))
            link(URI("/stats"), LinkRelation("stats"))
            link(URI("/info"), LinkRelation("self"))
        }
    }

    data class AuthInfo(
        val name : String,
        val email : String,
        val studentNumber : Int
    )

}
