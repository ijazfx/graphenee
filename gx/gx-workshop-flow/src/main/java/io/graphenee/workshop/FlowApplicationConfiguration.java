package io.graphenee.workshop;

import java.io.File;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.graphenee.core.GrapheneeMigration;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.FileStorageFactory;

@Configuration
public class FlowApplicationConfiguration {

	@Bean
	FileStorage fileStorage(Environment env) {
		FileStorage storage = null;
		String fsType = env.getProperty("gx.fs-type", "local");
		switch (fsType) {
			case "s3":
				String awsKey = env.getProperty("gx.fs-aws-key", env.getProperty("gx.aws-key"));
				String awsSecret = env.getProperty("gx.fs-aws-secret", env.getProperty("gx.aws-secret"));
				String s3Bucket = env.getProperty("gx.fs-s3-bucket", "graphenee");
				storage = FileStorageFactory.createS3FileStorage(awsKey, awsSecret, s3Bucket);
				break;
			case "local":
			default:
				String fsPath = env.getProperty("gx.fs-path",
						System.getProperty("user.home") + File.separatorChar + ".graphenee");
				storage = FileStorageFactory.createLocalFileStorage(new File(fsPath));
				break;
		}
		return storage;
	}

	@ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true", matchIfMissing = false)
	@Bean
	FlywayMigrationStrategy migrationStrategy(GrapheneeMigration graphenee, Flyway flyway) {
		return new FlywayMigrationStrategy() {

			@Override
			public void migrate(Flyway f) {
				graphenee.migrate(f);
				// f.migrate();
			}
		};
	}

}
