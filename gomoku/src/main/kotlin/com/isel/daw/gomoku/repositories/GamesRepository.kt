package com.isel.daw.gomoku.repositories

import com.isel.daw.gomoku.domain.Game
import java.util.*

interface GamesRepository {
    fun getById(id : UUID) : Game?
    fun update(game : Game)
    fun insert(game : Game)
}