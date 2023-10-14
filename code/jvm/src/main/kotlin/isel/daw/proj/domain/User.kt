package isel.daw.proj.domain

data class User(
    val username : String,
    val pwd : PasswordValidationInfo,
    val numberOfGames : Int,
    val numberOfWins : Int,
)
