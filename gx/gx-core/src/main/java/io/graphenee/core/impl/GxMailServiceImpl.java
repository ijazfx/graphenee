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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import io.graphenee.core.exception.SendMailFailedException;
import io.graphenee.core.model.bean.GxMailAttachmentBean;
import io.graphenee.core.storage.FileStorage;
import io.graphenee.core.storage.ResolveFailedException;

public class GxMailServiceImpl implements io.graphenee.core.api.GxMailService {

	private JavaMailSender javaMailSender;

	private FileStorage fileStorage;

	@Override
	public void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList, String bccEmailList,
			List<GxMailAttachmentBean> attachmentBeans) throws Exception {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessage = new MimeMessageHelper(message, true);
			mimeMessage.setSubject(subject);
			mimeMessage.setText(content);
			mimeMessage.setTo(recipientEmail);
			mimeMessage.setFrom(senderEmail);
			mimeMessage.setSentDate(new Date());
			if (!StringUtils.isEmpty(bccEmailList))
				mimeMessage.setBcc(bccEmailList);
			if (!StringUtils.isEmpty(ccEmailList))
				mimeMessage.setCc(ccEmailList);
			if (attachmentBeans != null)
				for (GxMailAttachmentBean attachmentBean : attachmentBeans) {
					String resourcePath = getFileStorage().resourcePath(attachmentBean.getAttachmentFolder(), attachmentBean.getAttachmentFilePath());
					InputStream inputStream = getFileStorage().resolve(resourcePath);
					mimeMessage.addAttachment(attachmentBean.getAttachmentName(), new ByteArrayResource(org.apache.poi.util.IOUtils.toByteArray(inputStream)));
				}
			javaMailSender.send(message);
		} catch (ResolveFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw new SendMailFailedException(e);
		}	
	}

	@Override
	public void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList) throws Exception {
		sendEmail(subject, content, senderEmail, recipientEmail, ccEmailList, null, new ArrayList<>());
	}

	@Override
	public void sendEmail(String subject, String content, String senderEmail, String recipientEmail) throws Exception {
		sendEmail(subject, content, senderEmail, recipientEmail, null, null, new ArrayList<>());
	}

	@Override
	public void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList, String bccEmailList) throws Exception {
		sendEmail(subject, content, senderEmail, recipientEmail, ccEmailList, null, new ArrayList<>());

	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public FileStorage getFileStorage() {
		return fileStorage;
	}

	public void setFileStorage(FileStorage fileStorage) {
		this.fileStorage = fileStorage;
	}

}
