package com.isel.daw.gomoku.domain


enum class Variant (
    val variantCode : Int
) {

    FREESTULE(1),
    SWAP(2),
    CARO(3),
    OMOK(4),
    NINUKI_RENJU(5);

    companion object {
        fun fromString(variantCode: Int) = when (variantCode) {
            1 -> FREESTULE
            2 -> SWAP
            3 -> CARO
            4 -> OMOK
            5 -> NINUKI_RENJU
            else -> throw IllegalArgumentException("Invalid Variant Name")
        }

        fun toString(v: Variant) = when (v) {
            FREESTULE -> "freestyle"
            SWAP -> "swap"
            CARO -> "caro"
            OMOK -> "omok"
            NINUKI_RENJU -> "ninuki-renju"
        }
    }
}