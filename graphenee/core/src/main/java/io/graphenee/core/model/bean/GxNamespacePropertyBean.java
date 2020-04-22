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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.graphenee.core.model.BeanFault;

public class GxNamespacePropertyBean implements Serializable {

	private static final Logger L = LoggerFactory.getLogger(GxNamespacePropertyBean.class);

	private static final long serialVersionUID = 1L;
	private Integer oid;
	private String propertyKey;
	private String propertyValue;
	private String propertyDefaultValue;
	private BeanFault<Integer, GxNamespaceBean> namespaceFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public BeanFault<Integer, GxNamespaceBean> getNamespaceFault() {
		return namespaceFault;
	}

	public void setNamespaceFault(BeanFault<Integer, GxNamespaceBean> namespaceFault) {
		this.namespaceFault = namespaceFault;
	}

	public String getPropertyDefaultValue() {
		return propertyDefaultValue;
	}

	public void setPropertyDefaultValue(String propertyDefaultValue) {
		this.propertyDefaultValue = propertyDefaultValue;
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
		GxNamespacePropertyBean other = (GxNamespacePropertyBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

}
