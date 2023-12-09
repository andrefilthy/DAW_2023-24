package com.isel.daw.gomoku.http.controllers

import com.isel.daw.gomoku.dtos.UserListOutputModel
import com.isel.daw.gomoku.dtos.UserOutputModel
import com.isel.daw.gomoku.hypermedia.SirenModel
import com.isel.daw.gomoku.services.UserServices
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PathTemplate.statsController)
class StatsController(val userServices: UserServices) {

    @GetMapping
    fun getTopPlayers(
        @RequestParam("size")
        size : Int,
        @RequestParam("offset")
        offset : Int
    ) : ResponseEntity<SirenModel<UserListOutputModel>> {
        val topPlayers = userServices.getTopPlayers(size,offset)
        val numberOfPlayers = userServices.getNumberOfPlayers()
        val userListOutput = ArrayList<UserOutputModel>(topPlayers.size)
        for (user in topPlayers){
            userListOutput.add(UserOutputModel(user.username, user.numberOfGames, user.numberOfWins))
        }
        return ResponseEntity.status(200).header("Content-Type", "application/vnd.siren+json").body(UserListOutputModel(userListOutput,size,offset, numberOfPlayers).toSiren())
    }

}