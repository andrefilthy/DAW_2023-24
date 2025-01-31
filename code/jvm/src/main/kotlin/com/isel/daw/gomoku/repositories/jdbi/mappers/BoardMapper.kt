package com.isel.daw.gomoku.repositories.jdbi.mappers

import com.isel.daw.gomoku.domain.Board
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.SQLException

class BoardMapper : ColumnMapper<Board> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): Board {
        return Board.fromString(r.getString(columnNumber))
    }
}