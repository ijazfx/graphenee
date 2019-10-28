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
package io.graphenee.aws.impl;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import io.graphenee.aws.api.GxSnsService;

public class GxSnsServiceImpl implements GxSnsService {

	private AmazonSNS snsClient;

	public GxSnsServiceImpl(AWSCredentialsProvider credentialsProvider, String region) {
		AmazonSNSClientBuilder builder = AmazonSNSClient.builder().withCredentials(credentialsProvider);
		if (region != null) {
			builder.withRegion(region);
		}
		snsClient = builder.build();
	}

	@Override
	public String sendPromotionalSMSMessage(String phone, String message) {
		return sendPromotionalSMSMessage(null, phone, message);
	}

	@Override
	public String sendTransactionalSMSMessage(String phone, String message) {
		return sendTransactionalSMSMessage(null, phone, message);
	}

	@Override
	public String sendTransactionalSMSMessage(String senderId, String phone, String message) {
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		if (senderId != null) {
			senderId = senderId.trim();
			smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue(senderId).withDataType("String"));
		}
		smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue("0.50").withDataType("Number"));
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));
		try {
			PublishResult publish = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phone).withMessageAttributes(smsAttributes));
			if (publish != null)
				return publish.getMessageId();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String sendPromotionalSMSMessage(String senderId, String phone, String message) {
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		if (senderId != null) {
			senderId = senderId.trim();
			smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue(senderId).withDataType("String"));
		}
		smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue("0.50").withDataType("Number"));
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Promotional").withDataType("String"));
		PublishResult publish = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phone).withMessageAttributes(smsAttributes));
		if (publish != null)
			return publish.getMessageId();
		return null;
	}

}
