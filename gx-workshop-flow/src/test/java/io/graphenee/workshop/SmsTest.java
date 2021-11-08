package io.graphenee.workshop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.graphenee.core.enums.SmsProvider;
import io.graphenee.sms.GxSmsResponse;
import io.graphenee.sms.GxSmsSendException;
import io.graphenee.sms.api.GxSmsProviderService;
import io.graphenee.sms.api.GxSmsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsTest {

	@Autowired
	GxSmsProviderService service;

	@Test
	public void testSms() {
		GxSmsService provider = service.getSmsProviderServiceBySmsProvider(SmsProvider.AWS);
		try {
			GxSmsResponse sms = provider.sendTransactionalMessage("+923412233330", "This message is sent using Java code.");
			System.err.println(sms.getDetail());
			System.err.println(sms.getSmsCount());
		} catch (GxSmsSendException e) {
			e.printStackTrace();
		}
	}

}
