package com.isel.daw.gomoku.utils

sealed class Either<out L, out R> {
    data class Error<out L>(val value : L) : Either<L, Nothing>()
    data class Success<out R>(val value : R) : Either<Nothing, R>()
}