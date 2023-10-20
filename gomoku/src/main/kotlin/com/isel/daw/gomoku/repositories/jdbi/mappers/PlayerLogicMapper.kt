package isel.daw.proj.repositories.jdbi.mappers


import com.isel.daw.gomoku.domain.PlayerLogic
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.SQLException

class PlayerLogicMapper : ColumnMapper<PlayerLogic> {

    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): PlayerLogic {
        return PlayerLogic.fromString(r.getString(columnNumber))
    }
}