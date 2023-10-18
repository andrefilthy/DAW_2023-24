package com.isel.daw.gomoku.http.pipelines

import com.isel.daw.gomoku.domain.User
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthenticationInterceptor(private val authorizationHeaderProcessor: AuthorizationHeaderProcessor) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(handler is HandlerMethod && handler.methodParameters.any{ it.parameterType == User::class.java}
        ) {
            val user = authorizationHeaderProcessor.process(request.getHeader(NAME_AUTHORIZATION_HEAD))
            if(user == null) {
                response.status = 401
                return false
            } else {
                UserArgumentResolver.addUserTo(user, request)
                return true
            }
        }
        return true
    }

    companion object {
        private const val NAME_AUTHORIZATION_HEAD = "Authorization"

    }
}