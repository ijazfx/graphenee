package io.graphenee.sms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.graphenee.sms.GxSmsResponse;
import io.graphenee.sms.GxSmsSendException;
import io.graphenee.sms.api.GxSmsService;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.sms.proto.GxSmsConfigProtos.EoceanConfig;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EoceanSmsServiceImpl implements GxSmsService {

	private static final Logger L = LoggerFactory.getLogger(EoceanSmsServiceImpl.class);

	private EoceanService eoceanService;
	private EoceanConfig smsConfig;
	private final int perSMSMaxLength = 160;

	public EoceanSmsServiceImpl(GxSmsConfigProtos.EoceanConfig smsConfig) {
		this.smsConfig = smsConfig;
		eoceanService = new Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create())
				.baseUrl(smsConfig.getBaseUrl()).build().create(EoceanService.class);
	}

	@Override
	public GxSmsResponse sendTransactionalMessage(String phone, String message) throws GxSmsSendException {
		return sendTransactionalMessage(null, phone, message);
	}

	@Override
	public GxSmsResponse sendPromotionalMessage(String phone, String message) throws GxSmsSendException {
		return sendPromotionalMessage(null, phone, message);
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
		if (Strings.isNullOrEmpty(senderId))
			senderId = smsConfig.getSenderId();
		Call<String> call = eoceanService.requestAPI(smsConfig.getUser(), smsConfig.getPassword(), senderId, phone, message);
		try {
			Response<String> response = call.execute();
			if (response.isSuccessful()) {
				GxSmsResponse gxSmsResponse = new GxSmsResponse();
				gxSmsResponse.setDetail(response.message());
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
