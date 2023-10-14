package isel.daw.proj

import isel.daw.proj.http.controllers.PathTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import isel.daw.proj.http.pipeline.AuthenticationInterceptor
import isel.daw.proj.http.pipeline.UserArgumentResolver
import isel.daw.proj.repositories.jdbi.mappers.*
import isel.daw.proj.utils.Sha256TokenEncoder
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class ProjApplication{


	//docker build -t battleships:1
	//docker run -d -p 8080:8080 --env JDBC_DATABASE_URL="jdbc:postgresql://host.docker.internal/postgres?user=postgres&password=postgres"  battleships:1
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
	runApplication<ProjApplication>(*args)
}
