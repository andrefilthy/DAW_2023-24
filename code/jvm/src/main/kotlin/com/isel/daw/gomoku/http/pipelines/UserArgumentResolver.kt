package com.isel.daw.gomoku.http.pipelines

import com.isel.daw.gomoku.domain.User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) = parameter.parameterType == User::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw IllegalStateException()
        return getUserFrom(request) ?: throw IllegalStateException()
    }

    companion object {
        private const val KEY = "UserArgumentResolver"

        fun addUserTo(user: User, request: HttpServletRequest) {
            return request.setAttribute(KEY, user)
        }

        fun getUserFrom(request: HttpServletRequest): User? {
            return request.getAttribute(KEY)?.let {
                it as? User
            }
        }
    }
}