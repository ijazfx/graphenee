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
import java.util.Base64;
import java.util.Formatter;

import io.graphenee.core.hash.TRHashProvider;

/**
 * @author ijazfx
 */
abstract public class AbstractHashProvider implements TRHashProvider {

	@Override
	public String createPasswordHash(String input) {
		return createHash(PASSWORD_PREFIX, input, "");
	}

	@Override
	public String createHash(String prefix, String input, String signingKey) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA");
			String finalInput = prefix + input;
			byte[] hash = digest.digest(finalInput.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			log.warning(e.getMessage());
		}
		return null;
	}

	@Override
	public String createHash(String input, String signingKey) {
		return createHash("", input, signingKey);
	}

	@Override
	public boolean checkPasswordHash(String input, String hashed) {
		String inputHash = createPasswordHash(input);
		return inputHash != null && inputHash.equals(hashed);
	}

	protected static String toHexString(byte[] bytes) {
		try (Formatter formatter = new Formatter()) {
			for (byte b : bytes) {
				formatter.format("%02x", b);
			}
			return formatter.toString();
		}
	}

	protected boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

}
