import org.junit.Test;

import io.graphenee.sms.api.GxSmsService;
import io.graphenee.sms.impl.TwilioSmsServiceImpl;
import io.graphenee.sms.proto.GxSmsConfigProtos;
import io.graphenee.sms.proto.GxSmsConfigProtos.TwilioConfig.Builder;

public class TwilioSmsServiceTest {

	String accountSid = "your_account_sid";
	String authToken = "your_auth_token";
	String fromPhoneNumber = "sender_phone_or_sid";
	String receiver = "receiver_phone";

	public TwilioSmsServiceTest() {
	}

	@Test
	public void testSendTransactionalMessageString() {
		Builder builder = GxSmsConfigProtos.TwilioConfig.newBuilder();
		builder.setAccountSid(accountSid).setAuthToken(authToken);

		GxSmsService service = new TwilioSmsServiceImpl(builder.build());
		//String output = service.sendPromotionalMessage(fromPhoneNumber, receiver, "Hello World! This message is from TRIGSOFT, testing Eocean SMS service.");
		//	assert (output != null);
	}

}
