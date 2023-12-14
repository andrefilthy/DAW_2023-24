package isel.daw.proj.dtos

import com.isel.daw.gomoku.domain.RuleSet
import com.isel.daw.gomoku.domain.User

data class WaitingEntry(
    val player1 : User,
    val ruleSet : RuleSet
)