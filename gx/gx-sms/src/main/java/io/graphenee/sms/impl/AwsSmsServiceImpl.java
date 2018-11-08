package io.graphenee.sms.impl;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import io.graphenee.sms.api.GxSmsService;

public class AwsSmsServiceImpl implements GxSmsService {

	private AWSCredentialsProvider awsCredentialProvider;

	@Override
	public String sendPromotionalMessage(String phone, String message) {
		return sendPromotionalMessage(null, phone, message);
	}

	@Override
	public String sendTransactionalMessage(String phone, String message) {
		return sendTransactionalMessage(null, phone, message);
	}

	@Override
	public String sendTransactionalMessage(String senderId, String phone, String message) {
		AmazonSNSClient snsClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard().withRegion("eu-west-1").withCredentials(getAwsCredentialProvider()).build();
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
	public String sendPromotionalMessage(String senderId, String phone, String message) {
		AmazonSNSClient snsClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard().withCredentials(getAwsCredentialProvider()).build();
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

	public AWSCredentialsProvider getAwsCredentialProvider() {
		return awsCredentialProvider;
	}

	public void setAwsCredentialProvider(AWSCredentialsProvider awsCredentialProvider) {
		this.awsCredentialProvider = awsCredentialProvider;
	}

}
