package com.isel.daw.gomoku.repositories

interface TransactionManager {
    fun <R> run(block : (Transaction) -> R): R
}