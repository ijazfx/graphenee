package io.graphenee.workshop;

import java.io.File;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.graphenee.core.GrapheneeMigration;
import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.FileStorageFactory;

@Configuration
public class FlowApplicationConfiguration {

	@Bean
	FileStorage fileStorage() {
		String homeFolder = System.getProperty("user.home");
		File rootFolder = new File(homeFolder + File.separator + ".gx-workshop-flow");
		FileStorage storage = FileStorageFactory.createLocalFileStorage(rootFolder);
		return storage;
	}

	@Bean
	FlywayMigrationStrategy migrationStrategy(GrapheneeMigration graphenee, Flyway flyway) {
		return new FlywayMigrationStrategy() {

			@Override
			public void migrate(Flyway f) {
				graphenee.migrate(f);
				//f.migrate();
			}
		};
	}

	@Bean
	GxNamespace namespace(GxDataService dataService) {
		return dataService.systemNamespace();
	}

}
