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
package io.graphenee.core.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import io.graphenee.core.hash.TRHashFactory;
import io.graphenee.core.hash.TRHashProviderException;

/**
 * This class contains basic utility methods to be used
 *
 * @author ijazfx
 */
public class CryptoUtil {

	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	public static String decodeHex(String input) {
		if (input == null)
			return input;
		try {
			byte[] decodedHex = Hex.decodeHex(input.toCharArray());
			return new String(decodedHex, "utf8");
		} catch (Exception e) {
			return input;
		}
	}

	public static String encode(String input) {
		return Hex.encodeHexString(java.util.Base64.getEncoder().encode(input.getBytes()));
	}

	public static String decode(String input) {
		if (input == null)
			return input;
		try {
			byte[] decodedHex = Hex.decodeHex(input.toCharArray());
			byte[] decodedString = Base64.decodeBase64(decodedHex);
			return new String(decodedString, "utf8");
		} catch (Exception e) {
			return input;
		}
	}

	public static String createPasswordHash(String password) {
		if (password == null)
			return password;
		try {
			String hash = TRHashFactory.getInstance().createPasswordHash(password);
			return Hex.encodeHexString(hash.getBytes());
		} catch (TRHashProviderException e) {
			return password;
		}
	}

}
