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
package io.graphenee.core;

import java.util.Collection;

import org.springframework.core.io.InputStreamSource;

import io.graphenee.core.exception.SendMailFailedException;

public interface GxMailService {

	void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList, String bccEmailList, Collection<GxMailAttachment> attachments)
			throws SendMailFailedException;

	void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList, String bccEmailList) throws SendMailFailedException;

	void sendEmail(String subject, String content, String senderEmail, String recipientEmail, String ccEmailList) throws SendMailFailedException;

	void sendEmail(String subject, String content, String senderEmail, String recipientEmail) throws SendMailFailedException;

	public static interface GxMailAttachment {

		String fileName();

		InputStreamSource streamSource();

		String contentType();
	}
}
