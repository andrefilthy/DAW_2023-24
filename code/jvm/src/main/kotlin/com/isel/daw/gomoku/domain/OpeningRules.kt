package com.isel.daw.gomoku.domain

abstract class OpeningRules {

    open fun isPlayable(){}

    open fun nextPlayerToPlay(player1 : User, player2 : User) {}


    companion object {
        fun fromString(code: Int) : OpeningRules = when (code)  {
            1 -> FreestyleOpeningRules(code)
            2 -> SwapOpeningRules(code)
            3 -> CairoOpeningRules(code)
            4 -> OmokOpeningRules(code)
            5 -> NinukiOpeningRules(code)
            else -> throw IllegalArgumentException("Invalid Opening Rule Name")
        }
    }
}

data class FreestyleOpeningRules(val code : Int) : OpeningRules() {
    override fun isPlayable() {
    }

    override fun nextPlayerToPlay(player1 : User, player2 : User){
    }
}
data class SwapOpeningRules(val code : Int) : OpeningRules() {
    override fun isPlayable() {
    }

    override fun nextPlayerToPlay(player1 : User, player2 : User){
    }
}
data class OmokOpeningRules(val code : Int) : OpeningRules() {
    override fun isPlayable() {
    }

    override fun nextPlayerToPlay(player1 : User, player2 : User){
    }
}
data class CairoOpeningRules(val code : Int) : OpeningRules() {
    override fun isPlayable() {
    }

    override fun nextPlayerToPlay(player1 : User, player2 : User){
    }
}
data class NinukiOpeningRules(val code : Int) : OpeningRules() {
    override fun isPlayable() {
    }

    override fun nextPlayerToPlay(player1 : User, player2 : User){
    }
}