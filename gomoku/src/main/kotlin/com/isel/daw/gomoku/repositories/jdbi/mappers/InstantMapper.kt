package com.isel.daw.gomoku.repositories.jdbi.mappers

import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.time.Instant

class InstantMapper : ColumnMapper<Instant> {
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): Instant? {
        if(r.getObject(columnNumber) == null) return null
        return Instant.ofEpochSecond(r.getLong(columnNumber))
    }
}