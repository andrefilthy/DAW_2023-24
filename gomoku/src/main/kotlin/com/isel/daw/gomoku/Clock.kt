package com.isel.daw.gomoku

import org.springframework.stereotype.Component
import java.time.Instant

interface Clock {
    fun now(): Instant
}

@Component
object RealClock : Clock {
    // To only have second precision
    override fun now(): Instant = Instant.ofEpochSecond(Instant.now().epochSecond)
}