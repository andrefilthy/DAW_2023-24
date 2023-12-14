package com.isel.daw.gomoku.repositories.jdbi

import com.isel.daw.gomoku.domain.Token
import com.isel.daw.gomoku.domain.User
import com.isel.daw.gomoku.repositories.UserRepository
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.slf4j.LoggerFactory
import java.time.Instant

class JdbiUserRepository(private val handle : Handle) : UserRepository {

    override fun getById(id: Int): User? =
        handle.createQuery("SELECT username, numberOfGames, numberOfWins from dbo.User where UserID =:id")
            .bind("id", id)
            .mapTo(User::class.java)
            .singleOrNull()


    override fun getByUsername(username: String): User? =
        handle.createQuery("SELECT username, pwd, numberOfGames, numberOfWins from dbo.User where username =:username")
            .bind("username", username)
            .mapTo(User::class.java)
            .singleOrNull()

    override fun getByToken(token : String) : User? =
        handle.createQuery("SELECT u.username as username, u.pwd as pwd, u.numberOfGames as numberOfGames, u.numberOfWins as numberOfWins from dbo.token token inner join dbo.User u on token.username = u.username where token_validation = :token")
            .bind("token", token)
            .mapTo(User::class.java)
            .singleOrNull()

    override fun update(user: User) {
        handle.createUpdate("UPDATE dbo.User SET numberOfGames = :numberOfGames, numberOfWins = :numberOfWins WHERE username=:username")
            .bind("numberOfGames", user.numberOfGames)
            .bind("numberOfWins", user.numberOfWins)
            .bind("username", user.username)
            .execute()
    }

    override fun insert(user: User) : String =
        handle.createUpdate(
            "insert into dbo.User(username, pwd) values (:username, :password)")
            .bind("username", user.username)
            .bind("password", user.pwd.validationInfo)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()
            .toString()

    override fun getTopPlayers(size : Int, offset : Int): List<User> =
        handle.createQuery("""
              SELECT u.username, u.pwd, u.numberOfGames, u.numberOfWins FROM 
              (SELECT username, pwd, numberOfGames, numberOfWins, ROUND(numberofWins * 100.0/numberOfGames ,1) as percentage FROM dbo.user) u 
              WHERE u.numberOfGames >= 1
              ORDER BY u.percentage DESC
              OFFSET :offset ROWS
              FETCH NEXT :size ROWS ONLY;
              """)
            .bind("size", size)
            .bind("offset", offset)
            .mapTo<User>()
            .list()

    override fun getNumberOfPlayers(): Int =
        handle.createQuery("""
           SELECT COUNT(us.username) FROM (
                SELECT u.username, u.pwd, u.numberOfGames, u.numberOfWins FROM 
                (SELECT username, pwd, numberOfGames, numberOfWins, ROUND(numberofWins * 100.0/numberOfGames ,1) as percentage FROM dbo.user) u 
                WHERE u.numberOfGames >= 1
              ) as us;
              """)
            .mapTo<Int>()
            .one()

    override fun doesUserExist(username: String): Boolean {
        return handle.createQuery("select count(*) from dbo.User where username = :username")
            .bind("username", username)
            .mapTo<Int>()
            .single() == 1
    }

    override fun insertToken(token: Token) {
        //delete least used token
        val deletions = handle.createUpdate(
            """
            delete from dbo.Token 
            where username = :username 
            """.trimIndent()
        )
            .bind("username", token.userName)
            .execute()

        logger.info("{} tokens deleted when creating new token", deletions)

        handle.createUpdate("insert into dbo.Token(token_validation, username, created_at, last_used_at, expires_on) values (:token, :username, :created_at, :last_used_at, :expires_on)")
            .bind("token", token.tokenValidation.validationInfo)
            .bind("username", token.userName)
            .bind("created_at", token.createdAt.epochSecond)
            .bind("last_used_at", token.lastUsedAt.epochSecond)
            .bind("expires_on", token.expires_on.epochSecond)
            .execute()
    }

    override fun updateToken(token: Token, now : Instant){
        handle.createUpdate(
            """
                update dbo.Token
                set last_used_at = :last_used_at
                where token_validation = :validation_information
            """.trimIndent()
        )
            .bind("last_used_at", now.epochSecond)
            .bind("validation_information", token.tokenValidation.validationInfo)
            .execute()
    }

    override fun getTokenWithUsername(username: String): Token? =
        handle.createQuery("SELECT t.token_validation, t.username, t.created_at, t.last_used_at, t.expires_on from dbo.token t where t.username = :username")
            .bind("username", username)
            .mapTo(Token::class.java)
            .singleOrNull()



    companion object {
        private val logger = LoggerFactory.getLogger(JdbiUserRepository::class.java)
    }
}