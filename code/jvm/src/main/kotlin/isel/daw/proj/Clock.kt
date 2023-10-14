package isel.daw.proj

import org.springframework.stereotype.Component
import java.time.Instant

interface Clock {
    fun now(): Instant
}

@Component
object RealClock : Clock {
    override fun now(): Instant = Instant.ofEpochSecond(Instant.now().epochSecond)
}