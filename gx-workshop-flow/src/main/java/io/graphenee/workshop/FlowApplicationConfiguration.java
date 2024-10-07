package io.graphenee.workshop;

import java.io.File;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

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
	public Config hazelCastConfig() {
		Config config = new Config();
		config.getMapConfig("user-session").setTimeToLiveSeconds(300);
		return config;
	}

	@Bean
	public HazelcastInstance hazelcastInstance(Config hazelCastConfig) {
		return Hazelcast.newHazelcastInstance(hazelCastConfig);
	}

	@Bean
	public Map<String, Boolean> sessionMap(HazelcastInstance hazelcastInstance) {
		return hazelcastInstance.getMap("user-session");
	}

}
