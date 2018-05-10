package io.graphenee.core.model.bean;

import java.io.Serializable;

import io.graphenee.core.model.BeanFault;

public class GxEmailTemplateBean implements Serializable {

	private static final long serialVersionUID = 6239781437082703091L;

	private Integer oid;
	private String bccList;
	private String body;
	private String ccList;
	private Boolean isActive = true;
	private Boolean isProtected = false;
	private String subject;
	private String templateName;
	private BeanFault<Integer, GxNamespaceBean> namespaceBeanFault;

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

}