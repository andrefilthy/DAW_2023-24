package com.isel.daw.gomoku.repository.jdbi.mappers

import com.isel.daw.gomoku.domain.PasswordValidationInfo
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.SQLException

class PasswordValidationInfoMapper : ColumnMapper<PasswordValidationInfo> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): PasswordValidationInfo =
        PasswordValidationInfo(r.getString(columnNumber))
}
