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

import java.util.Base64;
import java.util.Base64.Encoder;

import com.lambdaworks.crypto.PBKDF;

/**
 * PBKDF2 implementation.
 *
 * @author ijazfx
 */
public class PBKDF2HashProvider extends AbstractHashProvider {

	public static final String ENCRYPTION = "PBKDF2";

	private String pbkdf2Salt;

	public PBKDF2HashProvider(String salt) {
		this.pbkdf2Salt = salt;
	}

	@Override
	public String createHash(String prefix, String input, String signingKey) {
		try {
			Encoder encoder = Base64.getEncoder();
			byte[] temp = (prefix + input).getBytes();
			byte[] hash = PBKDF.pbkdf2("HmacSHA1", temp, signingKey.getBytes(), 1000, 48);
			return encoder.encodeToString(hash);
		} catch (Exception e) {
			log.warning(e.getMessage());
			return super.createHash(prefix, input, signingKey);
		}
	}

	@Override
	public String createPasswordHash(String input) {
		return createHash(PASSWORD_PREFIX, input, pbkdf2Salt);
	}

	@Override
	public String encryption() {
		return ENCRYPTION;
	}

}
