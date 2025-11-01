/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.sms;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import io.graphenee.core.GrapheneeCoreConfiguration;
import io.graphenee.core.GxDataService;
import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.model.entity.GxSmsProvider;
import io.graphenee.sms.impl.AwsSmsServiceImpl;
import io.graphenee.sms.impl.EoceanSmsServiceImpl;
import io.graphenee.sms.impl.TwilioSmsServiceImpl;

@EnableScheduling
@Configuration
@ConditionalOnClass(GrapheneeCoreConfiguration.class)
@ComponentScan(GrapheneeSmsConfiguration.COMPONENT_SCAN_BASE_PACKAGE)
public class GrapheneeSmsConfiguration {

	public static final String COMPONENT_SCAN_BASE_PACKAGE = "io.graphenee.sms";

	@Autowired
	GxDataService dataService;

	@EventListener(ApplicationReadyEvent.class)
	@Order(200)
	public void initialize() {
		// add sms providers...
		GxSmsProvider provider = dataService.findSmsProviderByProvider(SmsProvider.AWS);
		if (provider == null) {
			provider = new GxSmsProvider();
			provider.setConfigData(null);
			provider.setImplementationClass(AwsSmsServiceImpl.class.getName());
			provider.setIsActive(true);
			provider.setIsPrimary(true);
			provider.setProviderName(SmsProvider.AWS.getProviderName());
			dataService.save(provider);
		}

		provider = dataService.findSmsProviderByProvider(SmsProvider.EOCEAN);
		if (provider == null) {
			provider = new GxSmsProvider();
			provider.setConfigData(null);
			provider.setImplementationClass(EoceanSmsServiceImpl.class.getName());
			provider.setIsActive(true);
			provider.setIsPrimary(false);
			provider.setProviderName(SmsProvider.EOCEAN.getProviderName());
			dataService.save(provider);
		}

		provider = dataService.findSmsProviderByProvider(SmsProvider.TWILIO);
		if (provider == null) {
			provider = new GxSmsProvider();
			provider.setConfigData(null);
			provider.setImplementationClass(TwilioSmsServiceImpl.class.getName());
			provider.setIsActive(true);
			provider.setIsPrimary(false);
			provider.setProviderName(SmsProvider.TWILIO.getProviderName());
			dataService.save(provider);
		}
	}

}
