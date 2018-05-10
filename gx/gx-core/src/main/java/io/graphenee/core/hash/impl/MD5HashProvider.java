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
package io.graphenee.core.hash.impl;

import java.security.MessageDigest;

/**
 * Modified MD5 Hash Provider. The only change here is that the secret key is
 * used in the hash process. So the string that is hashed is: prefix + input +
 * secret key
 *
 * @author ijazfx
 */
public class MD5HashProvider extends AbstractHashProvider {

	public static final String ENCRYPTION = "MD5";

	@Override
	public String encryption() {
		return ENCRYPTION;
	}

	@Override
	public String createHash(String prefix, String input, String signingKey) {
		// create the md5 hash and UTF-8 encode it
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			String data = prefix + input + signingKey;
			byte[] hash = md5.digest(data.getBytes("UTF-8"));
			return toHexString(hash);
		} catch (Exception e) {
			log.warning(e.getMessage());
		}
		return null;
	}

}
