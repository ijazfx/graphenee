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

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;

public class GxSecurityPolicyBean implements Serializable {

	public static final String DEFAULT = "Default";

	private static final long serialVersionUID = 1L;
	private Integer oid;
	private String securityPolicyName;
	private String securityPolicyDescription;
	private Integer priority = 0;
	private Boolean isActive = true;
	private Boolean isProtected = false;
	private BeanFault<Integer, GxNamespaceBean> namespaceFault;
	private BeanCollectionFault<GxSecurityGroupBean> securityGroupCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxUserAccountBean> userAccountCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxSecurityPolicyDocumentBean> securityPolicyDocumentCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxAccessKeyBean> accessKeyCollectionFault = BeanCollectionFault.emptyCollectionFault();

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getSecurityPolicyName() {
		return securityPolicyName;
	}

	public void setSecurityPolicyName(String securityPolicyName) {
		this.securityPolicyName = securityPolicyName;
	}

	public String getSecurityPolicyDescription() {
		return securityPolicyDescription;
	}

	public void setSecurityPolicyDescription(String securityPolicyDescription) {
		this.securityPolicyDescription = securityPolicyDescription;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
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

	public BeanFault<Integer, GxNamespaceBean> getNamespaceFault() {
		return namespaceFault;
	}

	public void setNamespaceFault(BeanFault<Integer, GxNamespaceBean> namespaceFault) {
		this.namespaceFault = namespaceFault;
	}

	public BeanCollectionFault<GxSecurityGroupBean> getSecurityGroupCollectionFault() {
		return securityGroupCollectionFault;
	}

	public void setSecurityGroupCollectionFault(BeanCollectionFault<GxSecurityGroupBean> securityGroupCollectionFault) {
		this.securityGroupCollectionFault = securityGroupCollectionFault;
	}

	public BeanCollectionFault<GxUserAccountBean> getUserAccountCollectionFault() {
		return userAccountCollectionFault;
	}

	public void setUserAccountCollectionFault(BeanCollectionFault<GxUserAccountBean> userAccountCollectionFault) {
		this.userAccountCollectionFault = userAccountCollectionFault;
	}

	public BeanCollectionFault<GxSecurityPolicyDocumentBean> getSecurityPolicyDocumentCollectionFault() {
		return securityPolicyDocumentCollectionFault;
	}

	public void setSecurityPolicyDocumentCollectionFault(BeanCollectionFault<GxSecurityPolicyDocumentBean> securityPolicyDocumentCollectionFault) {
		this.securityPolicyDocumentCollectionFault = securityPolicyDocumentCollectionFault;
	}

	public GxSecurityPolicyDocumentBean getDefaultSecurityPolicyDocumentBean() {
		GxSecurityPolicyDocumentBean spd = null;
		for (GxSecurityPolicyDocumentBean document : getSecurityPolicyDocumentCollectionFault().getBeans()) {
			if (document.getIsDefault()) {
				spd = document;
				break;
			}
		}
		return spd;
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
		GxSecurityPolicyBean other = (GxSecurityPolicyBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return securityPolicyName;
	}

	public BeanCollectionFault<GxAccessKeyBean> getAccessKeyCollectionFault() {
		return accessKeyCollectionFault;
	}

	public void setAccessKeyCollectionFault(BeanCollectionFault<GxAccessKeyBean> accessKeyCollectionFault) {
		this.accessKeyCollectionFault = accessKeyCollectionFault;
	}

}
