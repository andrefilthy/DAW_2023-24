package com.isel.daw.gomoku.domain

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BoardTests {

    @Test
    fun`test toNoPieceBoard`(){
        val board = Board.fromString("-BBB-W-W---BWWW-")

        val newBoard = board.toNoPieceBoard()
        Assertions.assertEquals("----------------", newBoard.toString())
    }
}