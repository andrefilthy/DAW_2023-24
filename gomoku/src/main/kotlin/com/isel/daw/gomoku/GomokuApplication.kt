package com.isel.daw.gomoku

import com.isel.daw.gomoku.http.controllers.PathTemplate
import com.isel.daw.gomoku.http.pipelines.AuthenticationInterceptor
import com.isel.daw.gomoku.http.pipelines.UserArgumentResolver
import com.isel.daw.gomoku.repositories.jdbi.mappers.BoardMapper
import com.isel.daw.gomoku.repositories.jdbi.mappers.InstantMapper
import com.isel.daw.gomoku.repositories.jdbi.mappers.TokenValidationInfoMapper
import com.isel.daw.gomoku.utils.Sha256TokenEncoder
import com.isel.daw.gomoku.repositories.jdbi.mappers.PasswordValidationMapper
import isel.daw.proj.repositories.jdbi.mappers.PlayerLogicMapper
import isel.daw.proj.repositories.jdbi.mappers.RuleSetMapper
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class GomokuApplication {
	//TODO("Rules(Variants and OpeningRules")
//docker build -t gomoku:1
//docker run -d -p 8080:8080 --env JDBC_DATABASE_URL="jdbc:postgresql://host.docker.internal/postgres?user=postgres&password=postgres"  gomoku:1
@Bean
fun jdbi() : Jdbi {
	val jdbcDatabaseURL =
		System.getenv("JDBC_DATABASE_URL")
			?: "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres"
	// ?; "jdbc:postgresql://localhost:8000/postgres?user=postgres&password=postgres"
	val dataSource = PGSimpleDataSource()
	dataSource.setURL(jdbcDatabaseURL)

	return Jdbi.create(dataSource)
		.installPlugin(KotlinPlugin())
		.registerColumnMapper(BoardMapper())
		.registerColumnMapper(PlayerLogicMapper())
		.registerColumnMapper(RuleSetMapper())
		.registerColumnMapper(InstantMapper())
		.registerColumnMapper(PasswordValidationMapper())
		.registerColumnMapper(TokenValidationInfoMapper())
	}

@Bean
fun passwordEncoder() = BCryptPasswordEncoder()
@Bean
fun tokenEncoder() = Sha256TokenEncoder()
}

@Configuration
class PipelineConfigurer(
	val authenticationInterceptor: AuthenticationInterceptor,
	val userArgumentResolver: UserArgumentResolver
) : WebMvcConfigurer {

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(authenticationInterceptor)
			.addPathPatterns(PathTemplate.gameController)
			.addPathPatterns(PathTemplate.gameController+"/*")
			.addPathPatterns(PathTemplate.gameController+"/*/*")
	}

	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(userArgumentResolver)
	}
}

fun main(args: Array<String>) {
	runApplication<GomokuApplication>(*args)
}
