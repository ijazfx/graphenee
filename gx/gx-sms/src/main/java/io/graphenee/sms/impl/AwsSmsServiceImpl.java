package io.graphenee.sms.impl;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.common.base.Strings;

import io.graphenee.sms.GxSmsResponse;
import io.graphenee.sms.GxSmsSendException;
import io.graphenee.sms.api.GxSmsService;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.sms.proto.GxSmsConfigProtos.AwsSmsConfig;

public class AwsSmsServiceImpl implements GxSmsService {

	private AwsSmsConfig smsConfig;
	private final int perSMSMaxLength = 160;

	public AwsSmsServiceImpl(GxSmsConfigProtos.AwsSmsConfig smsConfig) {
		this.smsConfig = smsConfig;
	}

	@Override
	public GxSmsResponse sendPromotionalMessage(String phone, String message) throws GxSmsSendException {
		return sendPromotionalMessage(smsConfig.getSenderId(), phone, message);
	}

	@Override
	public GxSmsResponse sendTransactionalMessage(String phone, String message) throws GxSmsSendException {
		return sendTransactionalMessage(smsConfig.getSenderId(), phone, message);
	}

	@Override
	public GxSmsResponse sendTransactionalMessage(String senderId, String phone, String message) throws GxSmsSendException {
		AmazonSNSClient snsClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard().withRegion(smsConfig.getAwsRegion()).withCredentials(getAwsCredentialProvider()).build();
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		if (!Strings.isNullOrEmpty(senderId)) {
			senderId = senderId.trim();
			smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue(senderId).withDataType("String"));
		}
		smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue("0.50").withDataType("Number"));
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));
		try {
			PublishResult publish = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phone).withMessageAttributes(smsAttributes));
			if (publish != null) {
				GxSmsResponse gxSmsResponse = new GxSmsResponse();
				gxSmsResponse.setDetail(publish.getMessageId());
				int smsCount = message.length() / perSMSMaxLength;
				if (message.length() % perSMSMaxLength != 0)
					smsCount += 1;
				gxSmsResponse.setSmsCount(smsCount);
				return gxSmsResponse;
			}
		} catch (Exception ex) {
			throw new GxSmsSendException(ex.getMessage(), ex);
		}
		return null;
	}

	@Override
	public GxSmsResponse sendPromotionalMessage(String senderId, String phone, String message) throws GxSmsSendException {
		AmazonSNSClient snsClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard().withRegion(smsConfig.getAwsRegion()).withCredentials(getAwsCredentialProvider()).build();
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		if (!Strings.isNullOrEmpty(senderId)) {
			senderId = senderId.trim();
			smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue(senderId).withDataType("String"));
		}
		smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue("0.50").withDataType("Number"));
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Promotional").withDataType("String"));
		try {
			PublishResult publish = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phone).withMessageAttributes(smsAttributes));
			if (publish != null) {
				GxSmsResponse gxSmsResponse = new GxSmsResponse();
				gxSmsResponse.setDetail(publish.getMessageId());
				int smsCount = message.length() / perSMSMaxLength;
				if (message.length() % perSMSMaxLength != 0)
					smsCount += 1;
				gxSmsResponse.setSmsCount(smsCount);
				return gxSmsResponse;
			}
		} catch (Exception ex) {
			throw new GxSmsSendException(ex.getMessage(), ex);
		}
		return null;
	}

	public AWSCredentialsProvider getAwsCredentialProvider() {

		return new AWSCredentialsProvider() {

			@Override
			public void refresh() {
			}

			@Override
			public AWSCredentials getCredentials() {
				return new AWSCredentials() {

					@Override
					public String getAWSSecretKey() {
						return smsConfig.getAwsSecretKey();
					}

					@Override
					public String getAWSAccessKeyId() {
						return smsConfig.getAwsAccessKeyId();
					}
				};
			}
		};
	}

}
