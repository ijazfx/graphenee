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

public enum SmsProvider {
	AWS("AWS"),
	EOCEAN("Eocean"),
	TWILIO("Twilio");

	SmsProvider(String providerName) {
		this.providerName = providerName;
	}

	private String providerName;

	public static SmsProvider smsProvider(String providerName) {
		if (providerName.equals("AWS"))
			return SmsProvider.AWS;
		if (providerName.equals("Eocean"))
			return SmsProvider.EOCEAN;
		if (providerName.equals("Twilio"))
			return SmsProvider.TWILIO;
		return null;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

}
