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
package io.graphenee.core.enums;

public enum AccessKeyType {
	RETINASCAN(0),
	FINGERPRINT(1),
	CARD(2);

	private Integer typeCode;

	private AccessKeyType(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Integer typeCode() {
		return this.typeCode;
	}

	public static AccessKeyType accessKeyType(Integer typeCode) {
		if (typeCode == 0)
			return AccessKeyType.RETINASCAN;
		if (typeCode == 1)
			return AccessKeyType.FINGERPRINT;
		if (typeCode == 2)
			return AccessKeyType.CARD;
		return null;
	}

}
