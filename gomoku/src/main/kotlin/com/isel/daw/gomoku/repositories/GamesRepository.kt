package com.isel.daw.gomoku.repositories

import com.isel.daw.gomoku.domain.Game
import com.isel.daw.gomoku.domain.User
import java.util.*

interface GamesRepository {
    fun getByUser(user : User) : Game?
    fun getById(id : UUID) : Game?
    fun update(game : Game)
    fun insert(game : Game)
}