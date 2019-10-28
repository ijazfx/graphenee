package io.graphenee.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.Base64;

import io.graphenee.aws.api.GxKmsService;
import io.graphenee.aws.impl.GxKmsServiceImpl;
import io.graphenee.aws.impl.GxKmsServiceImpl.GxKmsKeyProvider;
import io.graphenee.aws.impl.GxKmsServiceImpl.GxKmsSingleKeyProvider;

public class GxKmsServiceBuilder {

	private AWSCredentialsProvider credentialsProvider;
	private String region;
	private GxKmsKeyProvider keyProvider;

	public static GxKmsServiceBuilder newBuilder() {
		return new GxKmsServiceBuilder();
	}

	public GxKmsServiceBuilder withCredentials(String accessKey, String secretKey) {
		credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
		return this;
	}

	public GxKmsServiceBuilder withCredentialsProvider(AWSCredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
		return this;
	}

	public GxKmsServiceBuilder withRegion(String region) {
		this.region = region;
		return this;
	}

	public GxKmsServiceBuilder withKey(String key) {
		this.keyProvider = new GxKmsSingleKeyProvider(key);
		return this;
	}

	public GxKmsServiceBuilder withKeyProvider(GxKmsKeyProvider keyProvider) {
		this.keyProvider = keyProvider;
		return this;
	}

	public GxKmsService build() {
		if (credentialsProvider == null)
			throw new IllegalStateException("Please provide aws credentials.");
		if (keyProvider == null)
			throw new IllegalStateException("Please provide a key for encryption.");
		return new GxKmsServiceImpl(credentialsProvider, keyProvider, region);
	}

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("enc")) {
			if (args.length != 5) {
				System.out.println("Usage <enc> <accessKey> <secretKey> <keyId> <message>");
				System.exit(-1);
			}
			GxKmsService service = GxKmsServiceBuilder.newBuilder().withCredentials(args[1], args[2]).withKey(args[3]).build();
			byte[] encryptedMessage = service.encrypt(args[4].getBytes());
			System.out.println(Base64.encodeAsString(encryptedMessage));
			System.exit(0);
		} else if (args.length > 0 && args[0].equals("dec")) {
			if (args.length != 4) {
				System.out.println("Usage <dec> <accessKey> <secretKey> <base64EncryptedMessage>");
				System.exit(-1);
			}
			GxKmsService service = GxKmsServiceBuilder.newBuilder().withCredentials(args[1], args[2]).withKey("").build();
			byte[] encrypted = Base64.decode(args[3]);
			byte[] message = service.decrypt(encrypted);
			System.out.println(new String(message));
			System.exit(0);
		} else {
			System.out.println("Usage <enc> <accessKey> <secretKey> <keyId> <message>");
			System.out.println("Usage <dec> <accessKey> <secretKey> <base64EncryptedMessage>");
			System.exit(-1);
		}

	}

}