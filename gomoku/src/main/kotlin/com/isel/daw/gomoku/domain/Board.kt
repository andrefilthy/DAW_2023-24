package com.isel.daw.gomoku.domain

import kotlin.math.sqrt

enum class CellState(val char: Char) {
    EMPTYCELL('-'),
    WHITEPIECE('W'),
    BLACKPIECE('B');

    companion object {
        fun fromChar(c: Char) = when (c) {
            '-' -> EMPTYCELL
            'W' -> WHITEPIECE
            'B' -> BLACKPIECE
            else -> throw IllegalArgumentException("Invalid value for Board.State")
        }
    }
}

data class Board(private val cells : Array<Array<CellState>>, val boardSize: Int){
    fun get(l : Int, c : Int) = cells[l][c]
    fun mutate(state : CellState, playLine : Int, playCol : Int) : Board{
        val newBoardCells = Array(boardSize) { l-> Array(boardSize) { c-> cells[l][c] } }
        newBoardCells[playLine][playCol]=state
        return Board(newBoardCells, boardSize)
    }

    //EMPTY BOARD CREATION
    fun toNoPieceBoard() : Board{
        val newBoardCells = Array(boardSize) { l-> Array(boardSize) { c-> cells[l][c] } }
        for(l in 0 until boardSize) {
            for (c in 0 until boardSize) {
                if (this.get(l, c) == CellState.WHITEPIECE || this.get(l, c) == CellState.BLACKPIECE) {
                    newBoardCells[l][c] = CellState.EMPTYCELL //removes piece from board
                } else {
                    newBoardCells[l][c] = this.get(l, c) //leave it as it is
                }
            }
        }
            return Board(newBoardCells, boardSize)
    }

    //If cell is not empty, then we can't put any piece on it
    fun isPlayable(l : Int, c : Int) = cells[l][c] != CellState.BLACKPIECE && cells[l][c] != CellState.WHITEPIECE

    fun hasWon(cell : CellState) : Boolean {
        if(cell == CellState.WHITEPIECE){
            TODO()
        }
        else if(cell == CellState.BLACKPIECE){
            TODO()
        }
        return false
    }

    companion object {
        fun create(boardSize: Int) = Board(Array(boardSize) { Array(boardSize) { CellState.EMPTYCELL } }, boardSize = boardSize)

        fun fromString(s: String) : Board {
            val boardSize = sqrt(s.length.toDouble()).toInt()
            val b : Board = create(boardSize)
            var l = 0
            var c = 0
            for(ch in s){
                b.cells[l][c] = CellState.fromChar(ch)
                if(c == boardSize-1){
                    c = -1
                    l++
                }
                c++
            }
            return b
        }

    }


    override fun toString() : String = cells.flatMap { row ->
        row.map { it.char }
    }.joinToString("")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (!cells.contentDeepEquals(other.cells)) return false
        if (boardSize != other.boardSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cells.contentDeepHashCode()
        result = 31 * result + boardSize
        return result
    }
}

data class Play(val l : Int, val c: Int)