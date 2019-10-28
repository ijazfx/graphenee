package io.graphenee.aws.api;

public interface GxKmsService {

	byte[] encrypt(byte[] content);

	byte[] decrypt(byte[] encryptedContent);

}
