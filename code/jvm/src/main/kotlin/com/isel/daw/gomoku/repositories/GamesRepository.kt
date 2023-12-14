package com.isel.daw.gomoku.repositories

import com.isel.daw.gomoku.domain.Game
import com.isel.daw.gomoku.domain.RuleSet
import com.isel.daw.gomoku.domain.User
import isel.daw.proj.dtos.WaitingEntry
import java.util.*

interface GamesRepository {
    fun getByUser(user : User) : Game?
    fun getById(id : UUID) : Game?
    fun update(game : Game)
    fun insert(game : Game)
    fun insertToWaitingList(entry: WaitingEntry)
    fun searchForWaitingEntry(ruleSet: RuleSet) :  WaitingEntry?
    fun deleteEntryFromWaitingList(player : User)
}