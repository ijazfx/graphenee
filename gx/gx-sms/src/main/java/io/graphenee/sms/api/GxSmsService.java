package io.graphenee.sms.api;

public interface GxSmsService {

	String sendTransactionalMessage(String phone, String message);

	String sendPromotionalMessage(String phone, String message);

	String sendTransactionalMessage(String senderId, String phone, String message);

	String sendPromotionalMessage(String senderId, String phone, String message);

}
