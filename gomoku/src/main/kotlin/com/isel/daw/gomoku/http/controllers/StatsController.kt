package com.isel.daw.gomoku.http.controllers

import com.isel.daw.gomoku.dtos.UserListOutputModel
import com.isel.daw.gomoku.dtos.UserOutputModel
import com.isel.daw.gomoku.hypermedia.SirenModel
import com.isel.daw.gomoku.services.UserServices
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PathTemplate.statsController)
class StatsController(val userServices: UserServices) {

    @GetMapping
    fun getTopPlayers() : ResponseEntity<SirenModel<UserListOutputModel>> {
        val topPlayers = userServices.getTopPlayers()
        val userListOutput = ArrayList<UserOutputModel>(topPlayers.size)
        for (user in topPlayers){
            userListOutput.add(UserOutputModel(user.username, user.numberOfGames, user.numberOfWins))
        }
        return ResponseEntity.status(200).body(UserListOutputModel(userListOutput).toSiren())
    }

}