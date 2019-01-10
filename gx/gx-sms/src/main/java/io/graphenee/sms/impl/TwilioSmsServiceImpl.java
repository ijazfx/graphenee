package io.graphenee.sms.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import io.graphenee.sms.GxSmsResponse;
import io.graphenee.sms.GxSmsSendException;
import io.graphenee.sms.api.GxSmsService;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.sms.proto.GxSmsConfigProtos.TwilioConfig;

public class TwilioSmsServiceImpl implements GxSmsService {

	private TwilioConfig smsConfig;
	private final int perSMSMaxLength = 160;

	public TwilioSmsServiceImpl(GxSmsConfigProtos.TwilioConfig smsConfig) {
		this.smsConfig = smsConfig;
		Twilio.init(smsConfig.getAccountSid(), smsConfig.getAuthToken());
	}

	@Override
	public GxSmsResponse sendTransactionalMessage(String phone, String message) throws GxSmsSendException {
		return sendMessage(smsConfig.getSenderId(), phone, message);
	}

	@Override
	public GxSmsResponse sendPromotionalMessage(String phone, String message) throws GxSmsSendException {
		return sendMessage(smsConfig.getSenderId(), phone, message);
	}

	@Override
	public GxSmsResponse sendTransactionalMessage(String senderId, String phone, String message) throws GxSmsSendException {
		return sendMessage(senderId, phone, message);
	}

	@Override
	public GxSmsResponse sendPromotionalMessage(String senderId, String phone, String message) throws GxSmsSendException {
		return sendMessage(senderId, phone, message);
	}

	private GxSmsResponse sendMessage(String senderId, String phone, String message) throws GxSmsSendException {
		String messageServiceSid = (senderId != null && !senderId.startsWith("+")) ? senderId : null;
		String fromPhoneNumber = senderId != null && senderId.startsWith("+") ? senderId : null;
		try {
			Message twilioMessage = null;
			if (messageServiceSid != null)
				twilioMessage = Message.creator(new PhoneNumber(phone), messageServiceSid, message).create();
			if (fromPhoneNumber != null)
				twilioMessage = Message.creator(new PhoneNumber(phone), new PhoneNumber(fromPhoneNumber), message).create();
			if (twilioMessage != null) {
				GxSmsResponse gxSmsResponse = new GxSmsResponse();
				gxSmsResponse.setDetail(twilioMessage.getSid());
				int smsCount = message.length() / perSMSMaxLength;
				if (message.length() % perSMSMaxLength != 0)
					smsCount += 1;
				gxSmsResponse.setSmsCount(smsCount);
				return gxSmsResponse;
			}
		} catch (Exception e) {
			throw new GxSmsSendException(e.getMessage(), e);
		}
		return null;
	}

}
