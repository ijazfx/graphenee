package io.graphenee.sms.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import io.graphenee.sms.api.GxSmsService;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.sms.proto.GxSmsConfigProtos.TwilioConfig;

public class TwilioSmsServiceImpl implements GxSmsService {

	private TwilioConfig smsConfig;

	public TwilioSmsServiceImpl(GxSmsConfigProtos.TwilioConfig smsConfig) {
		this.smsConfig = smsConfig;
		Twilio.init(smsConfig.getAccountSid(), smsConfig.getAuthToken());
	}

	@Override
	public String sendTransactionalMessage(String phone, String message) {
		return sendMessage(smsConfig.getSenderId(), phone, message);
	}

	@Override
	public String sendPromotionalMessage(String phone, String message) {
		return sendMessage(smsConfig.getSenderId(), phone, message);
	}

	@Override
	public String sendTransactionalMessage(String senderId, String phone, String message) {
		return sendMessage(senderId, phone, message);
	}

	@Override
	public String sendPromotionalMessage(String senderId, String phone, String message) {
		return sendMessage(senderId, phone, message);
	}

	private String sendMessage(String senderId, String phone, String message) {
		String messageServiceSid = (senderId != null && !senderId.startsWith("+")) ? senderId : null;
		String fromPhoneNumber = senderId != null && senderId.startsWith("+") ? senderId : null;
		Message twilioMessage = null;
		if (messageServiceSid != null)
			twilioMessage = Message.creator(new PhoneNumber(phone), messageServiceSid, message).create();
		if (fromPhoneNumber != null)
			twilioMessage = Message.creator(new PhoneNumber(phone), new PhoneNumber(fromPhoneNumber), message).create();
		if (twilioMessage != null)
			return twilioMessage.getSid();
		return null;
	}

}
