package io.graphenee.workshop;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
