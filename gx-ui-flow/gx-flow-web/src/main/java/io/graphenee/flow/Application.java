package io.graphenee.flow;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import io.graphenee.core.GrapheneeCoreConfiguration;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public FlywayMigrationStrategy flywayMigrationStrategy(GrapheneeCoreConfiguration graphenee) {
		return new FlywayMigrationStrategy() {

			@Override
			public void migrate(Flyway flyway) {
				flyway.migrate();
			}
		};
	}

}
