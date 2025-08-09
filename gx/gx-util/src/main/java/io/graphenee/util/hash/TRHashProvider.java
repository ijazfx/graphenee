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
package io.graphenee.util.hash;

import java.util.logging.Logger;

/**
 * A provider for creating and checking hashes.
 *
 * @author ijazfx
 */
public interface TRHashProvider {

	Logger log = Logger.getLogger(TRHashProvider.class.getName());

	String PASSWORD_PREFIX = "icB";

	/**
	 * Creates a hash of a prefixed string provided a signing key.
	 *
	 * @param prefix - A prefix e.g. icB
	 * @param input - source content
	 * @param signingKey - signing key
	 * @return - generated hash
	 */
	String createHash(String prefix, String input, String signingKey);

	/**
	 * Return a password hash of string prefixed by the password prefixed
	 * defined by PASSWORD_PREFIX constant. Signing Key is retrieved from
	 * properties depending on the hashing algorithm.
	 *
	 * @param input - password
	 * @return - generated hash
	 */
	String createPasswordHash(String input);

	/**
	 * Creates a hash of a non-prefixed string provided a signing key.
	 * properties depending on the hashing algorithm.
	 *
	 * @param input - source content
	 * @param signingKey - signing key
	 * @return - generated hash
	 */
	String createHash(String input, String signingKey);

	/**
	 * Checks if the given input matches the hashed value.
	 * @param input the input to check.
	 * @param hashed the hashed value to check against.
	 * @return true if the input matches the hashed value, false otherwise.
	 */
	boolean checkPasswordHash(String input, String hashed);

	/**
	 * Returns the encryption algorithm used by this provider.
	 * @return the encryption algorithm.
	 */
	String encryption();

}
