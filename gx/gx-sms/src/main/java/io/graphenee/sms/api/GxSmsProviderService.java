package io.graphenee.sms.api;

import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.model.bean.GxSmsProviderBean;

public interface GxSmsProviderService {

	GxSmsService getPrimarySmsProviderService();

	GxSmsService makeSmsProviderService(GxSmsProviderBean smsProviderBean);

	GxSmsService getSmsProviderServiceBySmsProvider(SmsProvider provider);

	GxSmsService getSmsProviderServiceBySmsProviderName(String providerName);

}
