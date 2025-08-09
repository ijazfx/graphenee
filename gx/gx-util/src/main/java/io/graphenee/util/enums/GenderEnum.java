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
package io.graphenee.util.enums;

import java.util.stream.Stream;

/**
 * An enum that represents the gender of a user.
 */
public enum GenderEnum {
	/**
	 * Male.
	 */
	Male("M"),
	/**
	 * Female.
	 */
	Female("F"),
	/**
	 * Undisclosed.
	 */
	Undisclosed("X");

	private String genderCode;

	private GenderEnum(String genderCode) {
		this.genderCode = genderCode;
	}

	/**
	 * Gets the gender code.
	 * @return The gender code.
	 */
	public String getGenderCode() {
		return genderCode;
	}

	/**
	 * Gets the gender by gender code.
	 * @param genderCode The gender code.
	 * @return The gender.
	 */
	public static GenderEnum genderByGenderCode(String genderCode) {
		return Stream.of(values()).filter(v -> v.getGenderCode().equals(genderCode)).findFirst().orElse(null);
	}

}