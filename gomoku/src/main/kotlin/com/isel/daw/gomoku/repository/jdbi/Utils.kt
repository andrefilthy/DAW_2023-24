package com.isel.daw.gomoku.repository.jdbi

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import com.isel.daw.gomoku.repository.jdbi.mappers.InstantMapper
import com.isel.daw.gomoku.repository.jdbi.mappers.PasswordValidationInfoMapper
import com.isel.daw.gomoku.repository.jdbi.mappers.TokenValidationInfoMapper

fun Jdbi.configureWithAppRequirements(): Jdbi {
    installPlugin(KotlinPlugin())
    installPlugin(PostgresPlugin())

    registerColumnMapper(PasswordValidationInfoMapper())
    registerColumnMapper(TokenValidationInfoMapper())
    registerColumnMapper(InstantMapper())

    return this
}