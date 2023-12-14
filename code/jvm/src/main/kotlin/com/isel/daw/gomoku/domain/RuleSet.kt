package com.isel.daw.gomoku.domain

data class RuleSet( //TODO("variant and opening rules classes")
    val boardSize : Int,
    //val variant : Variant,
    //val openingRules : OpeningRules,
    val placingTime: Int
){
    override fun toString(): String {
        return "$boardSize/$placingTime"
    }

    companion object {
        fun fromString(s : String) : RuleSet{
            val valuesArray = s.split("/")
            return RuleSet(
                boardSize = valuesArray[0].toInt(),
                //variant = Variant.fromString(valuesArray[1].toInt()),
                //openingRules = OpeningRules.fromString(valuesArray[1].toInt()),
                placingTime = valuesArray[1].toInt()
            )
        }
    }
}