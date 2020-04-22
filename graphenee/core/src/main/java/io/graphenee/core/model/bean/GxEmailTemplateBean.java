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
package io.graphenee.core.model.bean;

import java.io.Serializable;

import io.graphenee.core.model.BeanFault;

public class GxEmailTemplateBean implements Serializable {

	private static final long serialVersionUID = 6239781437082703091L;

	private Integer oid;
	private String bccList;
	private String body, smsBody;
	private String ccList;
	private Boolean isActive = true;
	private Boolean isProtected = false;
	private String subject;
	private String templateName;
	private String templateCode;
	private BeanFault<Integer, GxNamespaceBean> namespaceBeanFault;
	private String senderEmailAddress;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getBccList() {
		return bccList;
	}

	public void setBccList(String bccList) {
		this.bccList = bccList;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSmsBody() {
		return smsBody;
	}

	public void setSmsBody(String smsBody) {
		this.smsBody = smsBody;
	}

	public String getCcList() {
		return ccList;
	}

	public void setCcList(String ccList) {
		this.ccList = ccList;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsProtected() {
		return isProtected;
	}

	public void setIsProtected(Boolean isProtected) {
		this.isProtected = isProtected;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public BeanFault<Integer, GxNamespaceBean> getNamespaceBeanFault() {
		return namespaceBeanFault;
	}

	public void setNamespaceBeanFault(BeanFault<Integer, GxNamespaceBean> namespaceBeanFault) {
		this.namespaceBeanFault = namespaceBeanFault;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GxEmailTemplateBean other = (GxEmailTemplateBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

	public String getNamespace() {
		if (namespaceBeanFault != null)
			return namespaceBeanFault.getBean().getNamespace();
		return null;
	}

	public String getSenderEmailAddress() {
		return senderEmailAddress;
	}

	public void setSenderEmailAddress(String senderEmailAddress) {
		this.senderEmailAddress = senderEmailAddress;
	}

}