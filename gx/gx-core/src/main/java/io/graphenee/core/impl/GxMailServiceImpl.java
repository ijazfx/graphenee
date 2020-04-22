/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.core.impl;

import java.util.Date;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;

public class GxMailServiceImpl implements io.graphenee.core.api.GxMailService {

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
