package com.isel.daw.gomoku.repositories

import com.isel.daw.gomoku.domain.Token
import com.isel.daw.gomoku.domain.User
import java.time.Instant

interface UserRepository {

    fun getById(id: Int): User?
    fun getByUsername(username: String): User?
    fun update(user: User)
    fun insert(user: User): String
    fun getTopPlayers(size : Int, offset : Int): List<User>
    fun doesUserExist(username: String) : Boolean
    fun insertToken(token : Token)

    fun updateToken(token : Token, now : Instant)
    fun getTokenWithUsername(username : String) : Token?
    fun getByToken(token : String): User?
}