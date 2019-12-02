package io.graphenee.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import io.graphenee.aws.api.GxSnsService;
import io.graphenee.aws.impl.GxSnsServiceImpl;

public class GxSnsServiceBuilder {

	private AWSCredentialsProvider credentialsProvider;
	private String region;

	public static GxSnsServiceBuilder newBuilder() {
		return new GxSnsServiceBuilder();
	}

	public GxSnsServiceBuilder withCredentials(String accessKey, String secretKey) {
		credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
		return this;
	}

	public GxSnsServiceBuilder withCredentialsProvider(AWSCredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
		return this;
	}

	public GxSnsServiceBuilder withRegion(String region) {
		this.region = region;
		return this;
	}

	public GxSnsService build() {
		if (credentialsProvider == null)
			throw new IllegalStateException("Please provide aws credentials.");
		return new GxSnsServiceImpl(credentialsProvider, region);
	}

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Required <accessKey> <secretKey> <phone> <message>");
			System.exit(-1);
		}
		String accessKey = args[0];
		String secretKey = args[1];
		String phone = args[2];
		String message = args[3];

		GxSnsService service = GxSnsServiceBuilder.newBuilder().withCredentials(accessKey, secretKey).build();
		String messageId1 = service.sendTransactionalSMSMessage(phone, message);
		System.err.println(messageId1);
		String messageId2 = service.sendPromotionalSMSMessage(phone, message);
		System.err.println(messageId2);
	}

}