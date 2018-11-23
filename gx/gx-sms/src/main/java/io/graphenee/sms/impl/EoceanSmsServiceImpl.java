package io.graphenee.sms.impl;

import io.graphenee.sms.api.GxSmsService;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.sms.proto.GxSmsConfigProtos.EoceanConfig;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EoceanSmsServiceImpl implements GxSmsService {

	private EoceanService eoceanService;
	private EoceanConfig smsConfig;

	public EoceanSmsServiceImpl(GxSmsConfigProtos.EoceanConfig smsConfig) {
		this.smsConfig = smsConfig;
		eoceanService = new Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create())
				.baseUrl(smsConfig.getBaseUrl()).build().create(EoceanService.class);
	}

	@Override
	public String sendTransactionalMessage(String phone, String message) {
		return sendTransactionalMessage(smsConfig.getSenderId(), phone, message);
	}

	@Override
	public String sendPromotionalMessage(String phone, String message) {
		return sendPromotionalMessage(smsConfig.getSenderId(), phone, message);
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
		Call<String> call = eoceanService.requestAPI(smsConfig.getUser(), smsConfig.getPassword(), senderId, phone, message);
		try {
			Response<String> response = call.execute();
			if (response.isSuccessful()) {
				return "SMS Sent";
			}
			return response.message();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
