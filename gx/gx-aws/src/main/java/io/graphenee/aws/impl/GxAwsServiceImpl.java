package io.graphenee.aws.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import io.graphenee.aws.api.GxAwsService;

@Service
public class GxAwsServiceImpl implements GxAwsService {

	@Autowired
	AWSCredentialsProvider awsCredentialProvider;

	@Override
	public String sendTransactionalSMSMessage(String phone, String message) {
		AmazonSNSClient snsClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard().withCredentials(awsCredentialProvider).build();
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue("93223").withDataType("String"));
		smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue("0.50").withDataType("Number"));
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));
		PublishResult publish = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phone).withMessageAttributes(smsAttributes));
		if (publish != null)
			return publish.getMessageId();
		return null;
	}

	@Override
	public String sendPromotionalSMSMessage(String phone, String message) {
		AmazonSNSClient snsClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard().withCredentials(awsCredentialProvider).build();
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue("93223").withDataType("String"));
		smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue("0.50").withDataType("Number"));
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));
		PublishResult publish = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phone).withMessageAttributes(smsAttributes));
		if (publish != null)
			return publish.getMessageId();
		return null;
	}

}
