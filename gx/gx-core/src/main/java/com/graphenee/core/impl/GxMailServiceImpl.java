package com.graphenee.core.impl;

import java.util.Date;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;

public class GxMailServiceImpl implements com.graphenee.core.api.GxMailService {

	private JavaMailSender javaMailSender;

	@Override
	public void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList, String bccEmailList) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(subject);
		message.setText(content);
		message.setTo(recipientEmail);
		message.setFrom(senderEmail);
		message.setSentDate(new Date());
		if (!StringUtils.isEmpty(bccEmailList))
			message.setBcc(bccEmailList);
		if (!StringUtils.isEmpty(ccEmailList))
			message.setCc(ccEmailList);
		getJavaMailSender().send(message);
	}

	@Override
	public void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList) {
		sendEmail(subject, content, senderEmail, recipientEmail, ccEmailList, null);
	}

	@Override
	public void sendEmail(String subject, String content, String senderEmail, String recipientEmail) {
		sendEmail(subject, content, senderEmail, recipientEmail, null, null);
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

}
