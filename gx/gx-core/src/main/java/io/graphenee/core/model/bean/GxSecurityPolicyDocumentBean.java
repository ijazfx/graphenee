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
import java.text.SimpleDateFormat;

import io.graphenee.core.model.BeanFault;

public class GxSecurityPolicyDocumentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private Integer oid;
	private String tag;
	private String documentJson;
	private Boolean isDefault = false;
	private BeanFault<Integer, GxSecurityPolicyBean> securityPolicyBeanFault;

	public GxSecurityPolicyDocumentBean() {
		tag = SDF.format(new java.util.Date());
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDocumentJson() {
		return documentJson;
	}

	public void setDocumentJson(String documentJson) {
		this.documentJson = documentJson;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public BeanFault<Integer, GxSecurityPolicyBean> getSecurityPolicyBeanFault() {
		return securityPolicyBeanFault;
	}

	public void setSecurityPolicyBeanFault(BeanFault<Integer, GxSecurityPolicyBean> securityPolicyBeanFault) {
		this.securityPolicyBeanFault = securityPolicyBeanFault;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		GxSecurityPolicyDocumentBean other = (GxSecurityPolicyDocumentBean) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return tag;
	}

}
