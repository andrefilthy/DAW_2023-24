package com.isel.daw.gomoku.http.controllers

import com.isel.daw.gomoku.domain.User
import com.isel.daw.gomoku.dtos.HomeOutputModel
import com.isel.daw.gomoku.dtos.UserOutputModel
import com.isel.daw.gomoku.hypermedia.SirenModel
import com.isel.daw.gomoku.services.UserServices
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PathTemplate.indexController)
class IndexController(val userServices: UserServices) {

    @GetMapping
    fun homePage(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization : String?
    ) : ResponseEntity<SirenModel<HomeOutputModel>>  /* homepage setup JSON*/
    {
        if(authorization != null){
            val user = userServices.getPlayerByToken(authorization.split(" ")[1])
            return when(user){
                is User -> ResponseEntity.status(200).body(HomeOutputModel(UserOutputModel(user.username, user.numberOfGames, user.numberOfWins)).toSiren())
                else -> ResponseEntity.status(200).body(HomeOutputModel(null).toSiren())
            }
        }
        return ResponseEntity.status(200).body(HomeOutputModel(null).toSiren())
    }


}