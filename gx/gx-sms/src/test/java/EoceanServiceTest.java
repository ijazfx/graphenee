import org.junit.Test;

import io.graphenee.sms.api.GxSmsService;
import io.graphenee.sms.impl.EoceanSmsServiceImpl;
import io.graphenee.sms.proto.EoceanSmsConfigProtos;
import io.graphenee.sms.proto.EoceanSmsConfigProtos.ConfigMessage.Builder;

public class EoceanServiceTest {

	String baseUrl = "https://pk.eocean.us";
	String user = "your_account";
	String pwd = "your_password";
	String mask = "your_mask";
	String receiver = "your_number";

	public EoceanServiceTest() {
	}

	@Test
	public void testSendTransactionalMessageString() {
		Builder builder = EoceanSmsConfigProtos.ConfigMessage.newBuilder();
		builder.setBaseUrl(baseUrl).setUser(user).setPwd(pwd);

		GxSmsService service = new EoceanSmsServiceImpl(builder.build());
		String output = service.sendPromotionalMessage(mask, receiver, "Hello World! This message is from TRIGSOFT, testing Eocean SMS service.");
		assert (output.toLowerCase().contains("message"));
	}

}
