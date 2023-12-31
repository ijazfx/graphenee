package io.graphenee.sms;

import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.model.entity.GxSmsProvider;

public interface GxSmsProviderService {

	GxSmsService getPrimarySmsProviderService();

	GxSmsService makeSmsProviderService(GxSmsProvider smsProvider);

	GxSmsService getSmsProviderServiceBySmsProvider(SmsProvider provider);

	GxSmsService getSmsProviderServiceBySmsProviderName(String providerName);

}
