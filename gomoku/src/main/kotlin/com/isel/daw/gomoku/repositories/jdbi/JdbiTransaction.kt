package com.isel.daw.gomoku.repositories.jdbi

import com.isel.daw.gomoku.repositories.Transaction
import com.isel.daw.gomoku.repositories.UserRepository
import org.jdbi.v3.core.Handle

class JdbiTransaction(
    private val handle: Handle,
) : Transaction {

    //override val gamesRepository: GamesRepository by lazy { JdbiGameRepository(handle) }

    override val userRepository: UserRepository by lazy { JdbiUserRepository(handle) }

    override fun rollback() {
        handle.rollback()
    }

}