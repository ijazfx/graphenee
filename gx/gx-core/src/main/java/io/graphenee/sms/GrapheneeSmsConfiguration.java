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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import io.graphenee.core.GrapheneeCoreConfiguration;
import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.entity.GxSmsProvider;
import io.graphenee.sms.impl.AwsSmsServiceImpl;
import io.graphenee.sms.impl.EoceanSmsServiceImpl;
import io.graphenee.sms.impl.TwilioSmsServiceImpl;
import jakarta.annotation.PostConstruct;

@Configuration
@ConditionalOnClass(GrapheneeCoreConfiguration.class)
@ComponentScan(GrapheneeSmsConfiguration.COMPONENT_SCAN_BASE_PACKAGE)
public class GrapheneeSmsConfiguration {

	public static final String COMPONENT_SCAN_BASE_PACKAGE = "io.graphenee.sms";

	@Autowired
	GxDataService dataService;

	@Autowired
	PlatformTransactionManager transactionManager;

	@PostConstruct
	public void initialize() {
		TransactionTemplate tran = new TransactionTemplate(transactionManager);
		tran.execute(status -> {
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

			return null;
		});
	}

}
