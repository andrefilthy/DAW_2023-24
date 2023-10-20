package com.isel.daw.gomoku.http.controllers

import com.isel.daw.gomoku.dtos.ProblemOutputModel
import com.isel.daw.gomoku.dtos.TokenOutputModel
import com.isel.daw.gomoku.dtos.UserInputModel
import com.isel.daw.gomoku.services.UserCreationError
import com.isel.daw.gomoku.services.UserLoginError
import com.isel.daw.gomoku.services.UserServices
import com.isel.daw.gomoku.utils.Either
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PathTemplate.indexController)
class UserController(val userServices: UserServices) {
    @PostMapping(PathTemplate.register)
    fun register(
        @RequestBody userInput : UserInputModel
    ) : ResponseEntity<*> {

        // TODO("HEADERS: LOCATION")
        val res = userServices.createUser(userInput.username, userInput.password)
        return when (res){
            is Either.Success -> {
                val token = res.value
                return ResponseEntity.status(201)
                    .body(
                        TokenOutputModel(
                            "Bearer",
                            token.tokenValidation.validationInfo,
                            token.expires_on.epochSecond
                        )
                    )
            }
            is Either.Error -> when(res.value){
                is UserCreationError.UserAlreadyExists-> ResponseEntity.status(409).body(ProblemOutputModel("UserAlreadyExists", "Username already exists", null))
                is UserCreationError.WeakPasswordError-> ResponseEntity.status(400).body(ProblemOutputModel("WeakPasswordError", "Your password is too weak", null))
                is UserCreationError.TokenCreationWentWrong-> ResponseEntity.status(500).body(ProblemOutputModel("TokenCreationWentWrong", "Oops...Token Creation went wrong", null))
            }
        }




    }

    @PostMapping(PathTemplate.login)
    fun login(
        @RequestBody userInput: UserInputModel
    ) : ResponseEntity<*> {
        val res = userServices.checkCredentials(userInput.username, userInput.password)
        return when (res) {
            is Either.Success ->{
                val token = res.value
                return ResponseEntity.status(200)
                    .body(
                        TokenOutputModel(
                            "Bearer",
                            token.tokenValidation.validationInfo,
                            token.expires_on.epochSecond
                        )
                    )
            }
            is Either.Error -> {
                when (res.value){
                    is UserLoginError.UserNotFound -> ResponseEntity.status(404).body(ProblemOutputModel("UserNotFound", "User with given Username was not found", null))
                    is UserLoginError.WrongPassword -> ResponseEntity.status(401).body(ProblemOutputModel("WrongPassword", "User password does not match", null))
                }
            }
        }
    }
}