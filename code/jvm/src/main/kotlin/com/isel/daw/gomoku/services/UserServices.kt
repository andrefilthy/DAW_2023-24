package com.isel.daw.gomoku.services

import com.isel.daw.gomoku.Clock
import com.isel.daw.gomoku.domain.PasswordValidationInfo
import com.isel.daw.gomoku.domain.Token
import com.isel.daw.gomoku.domain.User
import com.isel.daw.gomoku.domain.UserLogic
import com.isel.daw.gomoku.repositories.TransactionManager
import com.isel.daw.gomoku.utils.Either
import com.isel.daw.gomoku.utils.TokenEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.Duration

sealed class UserCreationError {
    object WeakPasswordError : UserCreationError()
    object UserAlreadyExists : UserCreationError()
    object TokenCreationWentWrong : UserCreationError()

}
typealias UserCreationResult = Either<UserCreationError, Token>

sealed class UserLoginError {
    object UserNotFound : UserLoginError()
    object WrongPassword : UserLoginError()
    //object TokenCreationWentWrong : UserLoginError()

}
typealias UserLoginResult = Either<UserLoginError, Token>

sealed class TokenCreationError{
    object InvalidUserOrPassword : TokenCreationError()
}
typealias TokenCreationResult = Either<TokenCreationError, Token>

@Component
class UserServices(
    val transactionManager : TransactionManager,
    val passwordEncoder : PasswordEncoder,
    val tokenEncoder : TokenEncoder,
    val userLogic : UserLogic,
    val clock : Clock
) {

    fun getByUsername(username: String) : User {
        val user : User = transactionManager.run {
            val playerRepository = it.userRepository
            playerRepository.getByUsername(username)!!
        }
        return user
    }

    fun removeFromWaitingList(player: User) : GameServiceResult {
        transactionManager.run { it.gamesRepository.deleteEntryFromWaitingList(player) }
        return Either.Success(GameServicesSuccess.RemovedFromWaitingList("Player removed from waiting list", 200))
    }

    fun createUser(username: String, password: String): UserCreationResult {
        if(!userLogic.isSafePassword(password)){
            return Either.Error(UserCreationError.WeakPasswordError)
        }
        val passwordValidationInfo = PasswordValidationInfo(
            passwordEncoder.encode(password)
        )

        val newUser = User(
            username = username,
            pwd = passwordValidationInfo,
            numberOfGames = 0,
            numberOfWins = 0
        )

        return transactionManager.run {
            if(it.userRepository.doesUserExist(newUser.username)){
                Either.Error(UserCreationError.UserAlreadyExists)
            }else{
                val userRepository = it.userRepository
                userRepository.insert(newUser)
                val res = createToken(username, password)
                when(res){
                    is Either.Success -> Either.Success(res.value)
                    is Either.Error -> Either.Error(UserCreationError.TokenCreationWentWrong)
                }

            }
        }
    }

    fun getNumberOfPlayers() : Int {
        return transactionManager.run {
            it.userRepository.getNumberOfPlayers()
        }
    }

    fun createToken(userName : String, password: String ) : TokenCreationResult{
        if(userName.isBlank() || password.isBlank())
            return Either.Error(TokenCreationError.InvalidUserOrPassword)

        return transactionManager.run {
            val userRepository = it.userRepository
            val user :User = userRepository.getByUsername(userName) ?: return@run userNotFound()
            if(!passwordEncoder.matches(password,user.pwd.validationInfo))
                return@run Either.Error(TokenCreationError.InvalidUserOrPassword)
            val token = userLogic.generateToken()
            val now = clock.now()
            val newToken = Token(tokenEncoder.createValidationInformation(token),user.username, now,now, now+TOKEN_TTL)
            userRepository.insertToken(newToken)
            Either.Success(newToken)
        }
    }
    fun getPlayerByToken(userToken : String) : User? {
        var user : User? = null
        transactionManager.run {
            val userRepository = it.userRepository
            user = userRepository.getByToken(userToken)

        }
        return user
    }

    fun checkCredentials(username: String, password: String): UserLoginResult {
        return transactionManager.run {
            val userRepository = it.userRepository
            val user = userRepository.getByUsername(username)?: return@run Either.Error(UserLoginError.UserNotFound)
            if(passwordEncoder.matches(password, user.pwd.validationInfo)){
                val now = clock.now()
                var token = userRepository.getTokenWithUsername(username)
                if(!tokenIsValid(token) || token == null){
                    token = Token(tokenEncoder.createValidationInformation(userLogic.generateToken()),user.username,now,now, now+TOKEN_TTL)
                    userRepository.insertToken(token)
                }else{
                    userRepository.updateToken(token, now)
                }
                Either.Success(token)
            }else{
                Either.Error(UserLoginError.WrongPassword)
            }
        }
    }

    private fun tokenIsValid(token : Token?): Boolean {
        if(token == null) return false
        val now = clock.now()
        return now.isBefore(token.createdAt.plus(TOKEN_TTL))
    }


    private fun userNotFound(): TokenCreationResult {
        passwordEncoder.encode("changeit")
        return Either.Error(TokenCreationError.InvalidUserOrPassword)
    }

    fun getTopPlayers(size : Int, offset: Int) : List<User>{
        return transactionManager.run {
            it.userRepository.getTopPlayers(size,offset)
        }
    }

    companion object {
        val TOKEN_TTL: Duration = Duration.ofDays(1)
    }
}