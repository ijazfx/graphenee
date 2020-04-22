package io.graphenee.aws.api;

public interface GxKmsService {

	byte[] encrypt(byte[] content);

	String encryptAsBase64String(byte[] content);

	byte[] decrypt(byte[] encryptedContent);

	String decryptAsString(byte[] encryptedContent);

	String decryptBase64AsString(byte[] encryptedContentBase64);

	byte[] decryptBase64(byte[] encryptedContentBase64);

}
