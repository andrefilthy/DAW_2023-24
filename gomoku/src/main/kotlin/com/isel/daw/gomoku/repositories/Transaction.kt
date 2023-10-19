package com.isel.daw.gomoku.repositories

interface Transaction {
    val gamesRepository : GamesRepository
    val userRepository : UserRepository
    fun rollback()
}