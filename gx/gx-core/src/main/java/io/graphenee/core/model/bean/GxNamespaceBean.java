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
import java.util.Optional;

import com.google.common.base.Strings;

import io.graphenee.core.model.BeanCollectionFault;

public class GxNamespaceBean implements Serializable {

	public static final String SYSTEM = "io.graphenee.system";

	private static final long serialVersionUID = 1L;
	private Integer oid;
	private String namespace;
	private String namespaceDescription;
	private Boolean isActive = true;
	private Boolean isProtected = false;

	private BeanCollectionFault<GxNamespacePropertyBean> namespacePropertyBeanCollectionFault = BeanCollectionFault.emptyCollectionFault();

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespaceDescription() {
		return namespaceDescription;
	}

	public void setNamespaceDescription(String namespaceDescription) {
		this.namespaceDescription = namespaceDescription;
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

	public BeanCollectionFault<GxNamespacePropertyBean> getNamespacePropertyBeanCollectionFault() {
		return namespacePropertyBeanCollectionFault;
	}

	public void setNamespacePropertyBeanCollectionFault(BeanCollectionFault<GxNamespacePropertyBean> namespacePropertyBeanCollectionFault) {
		this.namespacePropertyBeanCollectionFault = namespacePropertyBeanCollectionFault;
	}

	public GxNamespacePropertyBean getNamespaceProperty(String propertyKey) {
		Optional<GxNamespacePropertyBean> findFirst = getNamespacePropertyBeanCollectionFault().getBeans().stream().filter(pb -> pb.getPropertyKey().equals(propertyKey))
				.findFirst();
		if (findFirst.isPresent())
			return findFirst.get();
		return null;
	}

	public String getNamespacePropertyValue(String propertyKey) {
		GxNamespacePropertyBean property = getNamespaceProperty(propertyKey);
		if (property != null) {
			if (Strings.isNullOrEmpty(property.getPropertyValue()))
				return property.getPropertyDefaultValue();
			return property.getPropertyValue();
		}
		return null;
	}

	public String getNamespacePropertyValue(String propertyKey, boolean refresh) {
		getNamespacePropertyBeanCollectionFault().invalidate();
		return getNamespacePropertyValue(propertyKey);
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
		GxNamespaceBean other = (GxNamespaceBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return namespace;
	}

}
