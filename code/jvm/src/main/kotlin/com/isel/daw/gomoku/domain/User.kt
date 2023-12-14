package com.isel.daw.gomoku.domain

data class User(
    val username : String,
    val pwd : PasswordValidationInfo,
    val numberOfGames : Int,
    val numberOfWins : Int
)
