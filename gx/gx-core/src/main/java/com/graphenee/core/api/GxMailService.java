package com.graphenee.core.api;

public interface GxMailService {

	void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList, String bccEmailList);

	void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList);

	void sendEmail(String subject, String content, String senderEmail, String recipientEmail);
}
