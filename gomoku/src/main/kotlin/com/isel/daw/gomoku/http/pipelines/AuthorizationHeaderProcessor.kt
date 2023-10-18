package com.isel.daw.gomoku.http.pipelines

import com.isel.daw.gomoku.domain.User
import com.isel.daw.gomoku.services.UserServices
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class AuthorizationHeaderProcessor(
    private val userServices: UserServices
) {

    fun process(authorizationValue : String?): User?{
        if(authorizationValue == null)
            return null
        val parts = authorizationValue.trim().split(" ")
        if(parts.size != 2){
            return null
        }
        if(parts[0].lowercase() != SCHEME){
            return null
        }
        return userServices.getPlayerByToken(parts[1])
    }

    companion object{
        private val logger = LoggerFactory.getLogger(AuthorizationHeaderProcessor :: class.java)
        const val SCHEME = "bearer"
    }
}