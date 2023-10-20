package com.isel.daw.gomoku.dtos

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.net.URI

// TODO(Remove ProblemOutputModel and create other problem classes: Game, Info...)
data class ProblemOutputModel(
    val error : String,
    val message : String,
    val instance : String?
)

class UserProblem(
    val error : String,
    val message : String,
    val reference : URI?
){


    companion object{
        fun response(status: Int, problem: UserProblem): ResponseEntity<*> {
            return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem)
        }


        val userNotFound = UserProblem(
            "UserNotFound",
            "User with given Username was not found",
            URI(
            "https://google.com"            )
        )

        val wrongPassword = UserProblem(
            "WrongPassword",
            "User password does not match",
            URI(
            "https://google.com"            )
        )

        val userNameAlreadyExists = UserProblem(
            "UserAlreadyExists",
            "Username already exists",
            URI(
                "https://google.com"            )
        )

        val weakPassword = UserProblem(
            "WeakPasswordError",
            "Your password is too weak",
            URI("https://google.com")
        )


    }



}