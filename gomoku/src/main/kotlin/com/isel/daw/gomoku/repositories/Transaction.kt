package com.isel.daw.gomoku.repositories

interface Transaction {
    //val gamesRepository : GamesRepository TODO()
    val userRepository : UserRepository
    fun rollback()
}