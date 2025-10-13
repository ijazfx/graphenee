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
package io.graphenee.core.model.entity;

import com.google.protobuf.InvalidProtocolBufferException;

import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.model.GxMappedSuperclass;
import io.graphenee.sms.proto.GxSmsConfigProtos.AwsSmsConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_sms_provider")
public class GxSmsProvider extends GxMappedSuperclass {

	private String providerName;
	private String implementationClass;
	private byte[] configData;
	private Boolean isPrimary;
	private Boolean isActive;

	@Transient
	private String senderId;

	public String getSenderId() {
		if (senderId == null && providerName != null && configData != null) {
			if (providerName.equals(SmsProvider.AWS.getProviderName())) {
				try {
					AwsSmsConfig cfg = AwsSmsConfig.parseFrom(configData);
					senderId = cfg.getSenderId();
				} catch (InvalidProtocolBufferException e) {
					// ignore.
				}
			} else if (providerName.equals(SmsProvider.TWILIO.getProviderName())) {
				try {
					AwsSmsConfig cfg = AwsSmsConfig.parseFrom(configData);
					senderId = cfg.getSenderId();
				} catch (InvalidProtocolBufferException e) {
					// ignore.
				}
			} else if (providerName.equals(SmsProvider.EOCEAN.getProviderName())) {
				try {
					AwsSmsConfig cfg = AwsSmsConfig.parseFrom(configData);
					senderId = cfg.getSenderId();
				} catch (InvalidProtocolBufferException e) {
					// ignore.
				}
			}
		}
		return senderId;
	}

}