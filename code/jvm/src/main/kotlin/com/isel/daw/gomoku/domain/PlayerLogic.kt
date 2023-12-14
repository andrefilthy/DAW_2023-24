package com.isel.daw.gomoku.domain

data class PlayerLogic(
    var isReady : Boolean,
    val playerNumber : Type
) {

    enum class Type(val player : Int){
        PLAYER1(1),
        PLAYER2(2);

        companion object {
            fun fromInt(player : Int) = when (player) {
                1 -> PLAYER1
                2 -> PLAYER2
                else -> throw IllegalArgumentException("Invalid value for PlayerLogic")
            }
        }
    }
    fun changeTurn(game : Game, player: User) : Game.State{
        return if(game.isPlayer1(player)){
            Game.State.NEXT_PLAYER2
        } else if(game.isPlayer2(player)){
            Game.State.NEXT_PLAYER1
        } else game.currentState
    }

    fun isTurn(game : Game, player: User) : Boolean{
        return (playerNumber == Type.PLAYER1 && game.isPlayer1(player))
                || (playerNumber == Type.PLAYER2 && game.isPlayer2(player))
    }

    override fun toString() : String {
        val readyInt = if(isReady) 1
        else 0
        return "$readyInt;${playerNumber.player}"
    }
    companion object {
        fun create(player : Int) : PlayerLogic = PlayerLogic(false, Type.fromInt(player));

        fun fromString(s : String) : PlayerLogic{
            val valuesArray = s.split(";")
            val isReadyString = valuesArray[0]
            val playerString = valuesArray[1]

            val readyBool : Boolean = isReadyString == "1"
            val playerNumber = Type.fromInt(playerString.toInt())

            return PlayerLogic(readyBool, playerNumber)
        }


    }
}

