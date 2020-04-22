/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.core.hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.graphenee.core.hash.impl.BasicHashProvider;
import io.graphenee.core.hash.impl.HMACSHA1HashProvider;
import io.graphenee.core.hash.impl.MD5HashProvider;
import io.graphenee.core.hash.impl.PBKDF2HashProvider;
import io.graphenee.core.hash.impl.SCryptHashProvider;

/**
 * TRHashFactory provides factory method implementation of various hashing
 * techniques. By default Basic, PBKDF2 and SCrypt are provided. If you want to
 * provide your custom hashing technique, implement TRHashProvider interface and
 * register using the {@link #register(String, TRHashProvider)} method.
 *
 * @author ijazfx
 */
public class TRHashFactory {

	protected static final Logger log = Logger.getLogger(TRHashFactory.class.getName());

	private static final String ENCRYPTION_KEY = "io.graphenee.security.encryption";
	private static final String SALT_KEY = "io.graphenee.security.encryption.%s.salt";
	private static final String DEFAULT_SALT = "FEDCBA9876543210";

	private static Map<String, TRHashProvider> providers = new HashMap<>();

	private static TRHashFactory instance;

	private String defaultEncryption;

	static {
		try {
			register(BasicHashProvider.ENCRYPTION, new BasicHashProvider());
		} catch (TRHashProviderRegisterException e) {
			log.warning(e.getMessage());
		}
		try {
			String saltKey = String.format(SALT_KEY, PBKDF2HashProvider.ENCRYPTION) + ".salt";
			String salt = System.getProperty(saltKey, DEFAULT_SALT);
			register(PBKDF2HashProvider.ENCRYPTION, new PBKDF2HashProvider(salt));
		} catch (TRHashProviderRegisterException e) {
			log.warning(e.getMessage());
		}
		try {
			String saltKey = String.format(SALT_KEY, SCryptHashProvider.ENCRYPTION) + ".salt";
			String salt = System.getProperty(saltKey, DEFAULT_SALT);
			register(SCryptHashProvider.ENCRYPTION, new SCryptHashProvider(salt));
		} catch (TRHashProviderRegisterException e) {
			log.warning(e.getMessage());
		}
		try {
			register(HMACSHA1HashProvider.ENCRYPTION, new HMACSHA1HashProvider());
		} catch (TRHashProviderRegisterException e) {
			log.warning(e.getMessage());
		}
		try {
			register(MD5HashProvider.ENCRYPTION, new MD5HashProvider());
		} catch (TRHashProviderRegisterException e) {
			log.warning(e.getMessage());
		}
	}

	private TRHashFactory() {
		this.defaultEncryption = System.getProperty(ENCRYPTION_KEY, "Basic");
	}

	public static TRHashFactory getInstance() {
		if (instance == null) {
			instance = new TRHashFactory();
		}
		return instance;
	}

	/**
	 * Use this method to register your custom hash provider. The method will
	 * throw exception if there is already a provider registered with the
	 * encryption.
	 *
	 * @param encryption - a unique name for encryption e.g. Scrypt
	 * @param provider - an implementation of TRHashProvider
	 * @throws TRHashProviderRegisterException - throws exception if a provider
	 * is already registered for an encryption.
	 */
	public static void register(String encryption, TRHashProvider provider) throws TRHashProviderRegisterException {
		if (!providers.containsKey(encryption)) {
			providers.put(encryption, provider);
			log.info(provider.getClass() + " registered with key " + encryption);
		} else {
			throw new TRHashProviderRegisterException("There is already a provider registered as " + encryption);
		}
	}

	public String createPasswordHash(String input) throws TRHashProviderException {
		return createPasswordHash(defaultEncryption, input);
	}

	public String createPasswordHash(String encryption, String input) throws TRHashProviderException {
		TRHashProvider provider = providers.get(encryption);
		if (provider == null) {
			throw new TRHashProviderException("No hash provider registered with key " + defaultEncryption);
		}
		return provider.createPasswordHash(input);
	}

	public String createHash(String input, String signingKey) throws TRHashProviderException {
		return createHash(defaultEncryption, input, signingKey);
	}

	public String createHash(String encryption, String input, String signingKey) throws TRHashProviderException {
		TRHashProvider provider = providers.get(encryption);
		if (provider == null) {
			throw new TRHashProviderException("No hash provider registered with key " + defaultEncryption);
		}
		return provider.createHash(input, signingKey);
	}

	public static class FDHashProviderCallable implements Callable<String> {

		private TRHashProvider provider;

		private String input;

		public FDHashProviderCallable(TRHashProvider provider, String input) {
			this.provider = provider;
			this.input = input;
		}

		@Override
		public String call() throws Exception {
			return provider.createPasswordHash(input);
		}

	}

	/**
	 * The method is used to generate hash for all available providers in
	 * parallel.
	 *
	 * @param input - source content
	 * @return - generated hash
	 */
	public Set<String> generateHashForAllProviders(String input) {
		ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<Future<String>> futures = new ArrayList<>();
		Iterator<TRHashProvider> iterator = providers.values().iterator();
		while (iterator.hasNext()) {
			futures.add(service.submit(new FDHashProviderCallable(iterator.next(), input)));
		}
		Set<String> hashes = new HashSet<>();
		service.shutdown();
		try {
			if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
				service.shutdownNow();
			}
		} catch (InterruptedException e) {
			log.warning(e.getMessage());
		} finally {
			for (Future<String> future : futures) {
				try {
					String hash = future.get();
					if (hash != null) {
						hashes.add(hash);
					}
				} catch (InterruptedException e) {
					log.warning(e.getMessage());
				} catch (ExecutionException e) {
					log.warning(e.getMessage());
				}
			}
		}
		return hashes;
	}

}
