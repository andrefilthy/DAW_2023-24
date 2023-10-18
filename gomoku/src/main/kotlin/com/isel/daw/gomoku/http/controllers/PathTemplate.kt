package com.isel.daw.gomoku.http.controllers

object PathTemplate {

    const val gameController = "/game"
    const val indexController = "/"
    const val infoController = "/info"
    const val statsController = "/stats"

    /** GAME CONTROLLER, /game **/
    const val start = ""
    const val getGame = "/{gameID}"
    const val place = "/{gameID}/play"
    const val setPosition = "/{gameID}/set"

    /** INDEX CONTROLLER /index **/


    /** USER CONTROLLER, /user **/
    const val register = "/register"
    const val login = "/login"


}