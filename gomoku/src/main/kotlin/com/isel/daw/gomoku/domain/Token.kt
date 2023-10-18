package com.isel.daw.gomoku.domain

import java.time.Instant

data class Token(
    val tokenValidation: TokenValidationInfo,
    val userName: String,
    val createdAt: Instant,
    val lastUsedAt: Instant,
    val expires_on : Instant
)

