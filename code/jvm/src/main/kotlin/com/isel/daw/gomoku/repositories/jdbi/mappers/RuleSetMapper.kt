package isel.daw.proj.repositories.jdbi.mappers

import com.isel.daw.gomoku.domain.RuleSet
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.SQLException

class RuleSetMapper : ColumnMapper<RuleSet> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): RuleSet {
        return RuleSet.fromString(r.getString(columnNumber))
    }
}