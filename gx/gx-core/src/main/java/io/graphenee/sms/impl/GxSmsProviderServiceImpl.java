package io.graphenee.sms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.core.GxDataService;
import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.model.entity.GxSmsProvider;
import io.graphenee.sms.GxSmsProviderService;
import io.graphenee.sms.GxSmsService;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.sms.proto.GxSmsConfigProtos.AwsSmsConfig;
import io.graphenee.sms.proto.GxSmsConfigProtos.EoceanConfig;
import io.graphenee.sms.proto.GxSmsConfigProtos.TwilioConfig;

@Service
@Transactional
public class GxSmsProviderServiceImpl implements GxSmsProviderService {

	private static final Logger L = LoggerFactory.getLogger(GxSmsProviderServiceImpl.class);

	@Autowired
	GxDataService dataService;

	@Override
	public GxSmsService getSmsProviderServiceBySmsProvider(SmsProvider provider) {
		return getSmsProviderServiceBySmsProviderName(provider.getProviderName());
	}

	@Override
	public GxSmsService getSmsProviderServiceBySmsProviderName(String providerName) {
		GxSmsProvider bean = dataService.findSmsProviderByProviderName(providerName);
		if (bean == null) {
			L.warn("No SMS provider found with name " + providerName + ".");
			throw new IllegalStateException("No SMS provider found with name " + providerName + ".");
		}
		return makeSmsProviderService(bean);
	}

	@Override
	public GxSmsService getPrimarySmsProviderService() {
		GxSmsProvider bean = dataService.findSmsProviderPrimary();
		if (bean == null) {
			L.warn("No primary SMS provider found.");
			throw new IllegalStateException("No primary SMS provider found.");
		}
		return makeSmsProviderService(bean);
	}

	@Override
	public GxSmsService makeSmsProviderService(GxSmsProvider smsProvider) {
		// Aws
		if (smsProvider.getProviderName().equals(SmsProvider.AWS.getProviderName())) {
			AwsSmsConfig config;
			try {
				config = GxSmsConfigProtos.AwsSmsConfig.parseFrom(smsProvider.getConfigData());
				return new AwsSmsServiceImpl(config);
			} catch (Exception e) {
				L.warn(e.getMessage(), e);
			}
		}
		// Twilio
		if (smsProvider.getProviderName().equals(SmsProvider.TWILIO.getProviderName())) {
			TwilioConfig config;
			try {
				config = GxSmsConfigProtos.TwilioConfig.parseFrom(smsProvider.getConfigData());
				return new TwilioSmsServiceImpl(config);
			} catch (Exception e) {
				L.warn(e.getMessage(), e);
			}
		}
		// Eocean
		if (smsProvider.getProviderName().equals(SmsProvider.EOCEAN.getProviderName())) {
			EoceanConfig config;
			try {
				config = GxSmsConfigProtos.EoceanConfig.parseFrom(smsProvider.getConfigData());
				return new EoceanSmsServiceImpl(config);
			} catch (Exception e) {
				L.warn(e.getMessage(), e);
			}
		}
		// No service.
		throw new IllegalStateException(smsProvider.getProviderName() + " does not provide SMS service implementation.");
	}

}
