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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * HMAC-SHA1 implementations
 *
 * @author ijazfx
 */
public class HMACSHA1HashProvider extends AbstractHashProvider {

	public static final String ENCRYPTION = "HMAC-SHA1";

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	@Override
	public String encryption() {
		return ENCRYPTION;
	}

	@Override
	public String createHash(String prefix, String input, String signingKey) {
		if (!isNullOrEmpty(signingKey)) {
			SecretKeySpec secretKey = new SecretKeySpec(signingKey.getBytes(), HMAC_SHA1_ALGORITHM);
			Mac mac;
			try {
				mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
				mac.init(secretKey);
				String data = prefix + input;
				byte[] hash = mac.doFinal(data.getBytes("UTF-8"));
				return toHexString(hash);
			} catch (Exception e) {
				log.warning(e.getMessage());
			}
		}
		return null;
	}

}
