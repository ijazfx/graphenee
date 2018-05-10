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
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.lambdaworks.crypto.SCrypt;
import com.lambdaworks.crypto.SCryptUtil;

/**
 * SCrypt implementation.
 *
 * @author ijazfx
 */
public class SCryptHashProvider extends AbstractHashProvider {

	public static final String ENCRYPTION = "scrypt";

	private String scryptSalt;

	public SCryptHashProvider(String salt) {
		this.scryptSalt = salt;
	}

	@Override
	public String createHash(String prefix, String input, String signingKey) {
		try {
			Encoder encoder = Base64.getEncoder();
			byte[] temp = (prefix + input).getBytes();
			byte[] hash = SCrypt.scryptJ(temp, signingKey.getBytes(), 1024, 32, 16, 48);
			// byte[] hash = SCrypt.scryptJ(temp, signingKey.getBytes(), 16, 4, 2, 32);
			return encoder.encodeToString(hash);
		} catch (Exception e) {
			log.warning(e.getMessage());
			return super.createHash(prefix, input, signingKey);
		}
	}

	@Override
	public String createPasswordHash(String input) {
		return createHash(PASSWORD_PREFIX, input, scryptSalt);
	}

	@Override
	public String encryption() {
		return ENCRYPTION;
	}

	@Override
	public boolean checkPasswordHash(String input, String hashed) {
		Decoder decoder = Base64.getDecoder();
		byte[] decodedHash = decoder.decode(hashed);
		String inputHash;
		try {
			inputHash = new String(decodedHash, "UTF-8");
			return SCryptUtil.check(input, inputHash);
		} catch (Exception e) {
			log.warning(e.getMessage());
		}
		return false;
	}

}
