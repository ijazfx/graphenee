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

import java.util.Collection;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import io.graphenee.core.exception.SendMailFailedException;

public class GxMailServiceImpl implements io.graphenee.core.api.GxMailService {

    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String subject, String content, String senderEmail, String toEmailList, String ccEmailList, String bccEmailList, Collection<GxMailAttachment> attachments)
            throws SendMailFailedException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessage;
        try {
            mimeMessage = new MimeMessageHelper(message, true);
            mimeMessage.setFrom(senderEmail);
            mimeMessage.setSentDate(new Date());
            mimeMessage.setSubject(subject);
            mimeMessage.setText(content);
            if (toEmailList != null) {
                String[] emails = toEmailList.replaceAll("\\s", "").split("[;,]");
                mimeMessage.setTo(emails);
            }
            if (StringUtils.hasText(ccEmailList)) {
                String[] emails = ccEmailList.replaceAll("\\s", "").split("[;,]");
                mimeMessage.setCc(emails);
            }
            if (StringUtils.hasText(bccEmailList)) {
                String[] emails = bccEmailList.replaceAll("\\s", "").split("[;,]");
                mimeMessage.setBcc(emails);
            }
            if (attachments != null) {
                for (GxMailAttachment attachment : attachments) {
                    mimeMessage.addAttachment(attachment.fileName(), attachment.streamSource(), attachment.contentType());
                }
            }
            getJavaMailSender().send(message);
        } catch (MessagingException e) {
            throw new SendMailFailedException(e);
        }

    }

    @Override
    public void sendEmail(String subject, String content, String senderEmail, String toEmailList, String ccEmailList, String bccEmailList) throws SendMailFailedException {
        sendEmail(subject, content, senderEmail, toEmailList, ccEmailList, bccEmailList, null);
    }

    @Override
    public void sendEmail(String subject, String content, String senderEmail, String toEmailList, String ccEmailList) throws SendMailFailedException {
        sendEmail(subject, content, senderEmail, toEmailList, ccEmailList, null);
    }

    @Override
    public void sendEmail(String subject, String content, String senderEmail, String toEmailList) throws SendMailFailedException {
        sendEmail(subject, content, senderEmail, toEmailList, null);
    }

    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

}
