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

public class GxTermBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer oid;
	private String termKey;
	private String termSingular;
	private String termPlural;
	private Boolean isActive = true;
	private Boolean isProtected = false;
	private BeanFault<Integer, GxSupportedLocaleBean> supportedLocaleFault;
	private BeanFault<Integer, GxNamespaceBean> namespaceFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getTermKey() {
		return termKey;
	}

	public void setTermKey(String termKey) {
		this.termKey = termKey;
	}

	public String getTermSingular() {
		return termSingular;
	}

	public void setTermSingular(String termSingular) {
		this.termSingular = termSingular;
	}

	public String getTermPlural() {
		return termPlural;
	}

	public void setTermPlural(String termPlural) {
		this.termPlural = termPlural;
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

	public BeanFault<Integer, GxSupportedLocaleBean> getSupportedLocaleFault() {
		return supportedLocaleFault;
	}

	public void setSupportedLocaleFault(BeanFault<Integer, GxSupportedLocaleBean> supportedLocaleFault) {
		this.supportedLocaleFault = supportedLocaleFault;
	}

	public BeanFault<Integer, GxNamespaceBean> getNamespaceFault() {
		return namespaceFault;
	}

	public void setNamespaceFault(BeanFault<Integer, GxNamespaceBean> namespaceFault) {
		this.namespaceFault = namespaceFault;
	}

	public String getLanguage() {
		if (getSupportedLocaleFault() != null)
			return getSupportedLocaleFault().getBean().getLocaleName();
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((namespaceFault == null) ? 0 : namespaceFault.hashCode());
		result = prime * result + ((supportedLocaleFault == null) ? 0 : supportedLocaleFault.hashCode());
		result = prime * result + ((termKey == null) ? 0 : termKey.hashCode());
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
		GxTermBean other = (GxTermBean) obj;
		if (namespaceFault == null) {
			if (other.namespaceFault != null)
				return false;
		} else if (!namespaceFault.equals(other.namespaceFault))
			return false;
		if (supportedLocaleFault == null) {
			if (other.supportedLocaleFault != null)
				return false;
		} else if (!supportedLocaleFault.equals(other.supportedLocaleFault))
			return false;
		if (termKey == null) {
			if (other.termKey != null)
				return false;
		} else if (!termKey.equals(other.termKey))
			return false;
		return true;
	}

}
