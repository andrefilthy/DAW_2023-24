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

        fun toChar(cellState: CellState) = when (cellState) {
            EMPTYCELL -> '-'
            WHITEPIECE -> 'W'
            BLACKPIECE -> 'B'
        }
    }
}

data class Board(private val cells : Array<Array<CellState>>, val boardSize: Int){
    fun get(l : Int, c : Int) = cells[l][c]
    fun mutate(state: CellState, playLine: Int, playCol: Int): Board {
        val newBoardCells = Array(boardSize) { l-> Array(boardSize) { c-> cells[l][c] } }
        newBoardCells[playLine][playCol] = state
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

    fun hasWon(player : CellState) : Boolean {
        val target = if (player.char == CellState.WHITEPIECE.char) "WWWWW" else "BBBBB"

        // Check horizontal
        for (row in cells) {
            if (row.map { it.char }.joinToString("").contains(target)) {
                return true
            }
        }

        // Check vertical
        for (col in 0 until boardSize) {
            val column = cells.map { it[col].char }
            if (column.joinToString("").contains(target)) {
                return true
            }
        }

        // Check diagonals
        for (i in 0 until boardSize) {
            val diagonal1 = cells.slice(i until boardSize).mapIndexed { index, cell -> cell[index].char }
            val diagonal2 = cells.slice(0 until boardSize - i).mapIndexed { index, cell -> cell[index + i].char }

            if (diagonal1.joinToString("").contains(target) || diagonal2.joinToString("").contains(target)) {
                return true
            }
        }
        return false
    }

    companion object {
        fun create(boardSize: Int) = Board(Array(boardSize) { Array(boardSize) { CellState.EMPTYCELL } }, boardSize = boardSize)

        fun fromString(s: String): Board {
            val boardSize = sqrt(s.length.toDouble()).toInt()
            val b: Board = create(boardSize)
            var l = 0
            var c = 0
            for (ch in s) {
                b.cells[l][c] = CellState.fromChar(ch)
                if (c == boardSize - 1) {
                    c = 0
                    l++
                } else {
                    c++
                }
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