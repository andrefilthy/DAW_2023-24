package com.isel.daw.gomoku.http.controllers

object PathTemplate {

    const val gameController = "/game"
    const val indexController = "/"
    const val infoController = "/info"
    const val statsController = "/stats"

    /** GAME CONTROLLER, /game **/
    const val start = ""                //começar o jogo
    const val getGame = "/{gameID}"
    const val place = "/{gameID}/set"

    /** INDEX CONTROLLER /index **/
    const val deleteFromWaitingList = "removeFromLobby"

    /** USER CONTROLLER, /user **/
    const val register = "/register"
    const val login = "/login"


}