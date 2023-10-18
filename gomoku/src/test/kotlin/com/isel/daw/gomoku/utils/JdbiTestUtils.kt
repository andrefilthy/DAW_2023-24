package com.isel.daw.gomoku.utils

import com.isel.daw.gomoku.repositories.Transaction
import com.isel.daw.gomoku.repositories.TransactionManager
import com.isel.daw.gomoku.repositories.jdbi.JdbiTransaction
import com.isel.daw.gomoku.repositories.jdbi.mappers.BoardMapper
import com.isel.daw.gomoku.repositories.jdbi.mappers.InstantMapper
import com.isel.daw.gomoku.repositories.jdbi.mappers.TokenValidationInfoMapper
import com.isel.daw.gomoku.repositories.jdbi.mappers.PasswordValidationMapper
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.postgresql.ds.PGSimpleDataSource

private val jdbi = Jdbi.create(
    PGSimpleDataSource().apply {
        setUrl("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres")
        //setURL("jdbc:postgresql://localhost:8000/postgres?user=postgres&password=password")

    }
).installPlugin(KotlinPlugin())
    .registerColumnMapper(BoardMapper())
    //.registerColumnMapper(RuleSetMapper())
    //.registerColumnMapper(PlayerLogicMapper())
    .registerColumnMapper(InstantMapper())
    .registerColumnMapper(PasswordValidationMapper())
    .registerColumnMapper(TokenValidationInfoMapper())

fun testWithHandleAndRollback(block : (Handle) -> Unit) = jdbi.useTransaction<Exception> { handle ->
    block(handle)
    handle.rollback()
}

fun testWithTransactionManagerAndRollback(block : (TransactionManager) -> Unit) = jdbi.useTransaction<Exception> { handle ->
    val transaction = JdbiTransaction(handle)

    val transactionManager = object : TransactionManager {
        override fun <R> run(block: (Transaction) -> R): R {
            return block(transaction)
        }
    }
    block(transactionManager)
    handle.rollback()

}

fun executeScript(script : String) = jdbi.withHandleUnchecked { handle ->
    handle.createScript(script)
        .execute()
}

fun clearDB() = jdbi.withHandleUnchecked { handle ->
    handle.createScript("DELETE FROM dbo.game; DELETE FROM dbo.user; DELETE FROM dbo.token")
        .execute()

}