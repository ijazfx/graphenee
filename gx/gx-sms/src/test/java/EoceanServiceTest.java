import org.junit.Test;

import io.graphenee.sms.api.GxSmsService;
import io.graphenee.sms.impl.EoceanSmsServiceImpl;
import io.graphenee.sms.proto.EoceanSmsConfigProtos;
import io.graphenee.sms.proto.EoceanSmsConfigProtos.ConfigMessage.Builder;

public class EoceanServiceTest {

	public EoceanServiceTest() {
	}

	@Test
	public void testSendTransactionalMessageString() {
		Builder builder = EoceanSmsConfigProtos.ConfigMessage.newBuilder();
		builder.setBaseUrl("https://pk.eocean.us").setUser("test_user").setPwd("AJc1e2j1sr9cfS9VP48vp6UpS8uJfpYQp5PHM4ckL3U5y8gYhEw5XAUmliu3ItfXOg==").setResponse("string");

		GxSmsService service = new EoceanSmsServiceImpl(builder.build());
		service.sendPromotionalMessage("TRIGSOFT", "+923412233330", "Hello World! This message is from TRIGSOFT, testing Eocean SMS service.");
	}

}
