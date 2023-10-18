package com.isel.daw.gomoku.utils

import com.isel.daw.gomoku.domain.TokenValidationInfo

interface TokenEncoder {
    fun createValidationInformation(token: String): TokenValidationInfo
    fun validate(validationInfo: TokenValidationInfo, token: String): Boolean
}