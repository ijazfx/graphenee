package io.graphenee.aws.impl;

import java.nio.ByteBuffer;
import java.util.Random;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;

import io.graphenee.aws.api.GxKmsService;

public class GxKmsServiceImpl implements GxKmsService {

	private GxKmsKeyProvider keyProvider;
	private Random randomIndex = new Random();
	private AWSKMS kmsClient;

	public GxKmsServiceImpl(AWSCredentialsProvider credentialProvider, GxKmsKeyProvider keyProvider, String region) {
		this.keyProvider = keyProvider;
		AWSKMSClientBuilder builder = AWSKMSClient.builder().withCredentials(credentialProvider);
		if (region != null) {
			builder.withRegion(region);
		}
		kmsClient = builder.build();
	}

	@Override
	public byte[] encrypt(byte[] content) {
		String keyId = keyProvider.keys()[randomIndex.nextInt(keyProvider.keys().length)];
		EncryptRequest encryptRequest = new EncryptRequest().withKeyId(keyId);
		encryptRequest.setPlaintext(ByteBuffer.wrap(content));
		EncryptResult result = kmsClient.encrypt(encryptRequest);
		return result.getCiphertextBlob().array();
	}

	@Override
	public byte[] decrypt(byte[] encryptedContent) {
		DecryptRequest encryptRequest = new DecryptRequest();
		encryptRequest.setCiphertextBlob(ByteBuffer.wrap(encryptedContent));
		DecryptResult result = kmsClient.decrypt(encryptRequest);
		return result.getPlaintext().array();
	}

	public static interface GxKmsKeyProvider {
		String[] keys();
	}

	public static class GxKmsSingleKeyProvider implements GxKmsKeyProvider {

		private String key;

		public GxKmsSingleKeyProvider(String key) {
			this.key = key;
		}

		@Override
		public String[] keys() {
			return new String[] { key };
		}

	}

}
