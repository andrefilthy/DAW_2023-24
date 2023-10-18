package com.isel.daw.gomoku.domain

import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.security.SecureRandom
import java.util.*

@Component
class UserLogic {

    fun generateToken(): String =
        ByteArray(TOKEN_BYTE_SIZE).let { byteArray ->
            SecureRandom.getInstanceStrong().nextBytes(byteArray)
            Base64.getUrlEncoder().encodeToString(byteArray)
        }

    fun canBeToken(token : String): Boolean =   try{
        Base64.getUrlDecoder().decode(token).size == TOKEN_BYTE_SIZE
    } catch (e : IllegalArgumentException){
        false
    }

    //TODO (Make it better)
    fun isSafePassword(password : String) : Boolean{
        return password.length >=  4
    }



    companion object {
        private const val TOKEN_BYTE_SIZE = 256 / 8
    }
}