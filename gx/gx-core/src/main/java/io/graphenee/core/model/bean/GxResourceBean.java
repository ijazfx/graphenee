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

public class GxResourceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer oid;
	private String resourceName;
	private String resourceDescription;
	private Boolean isActive = true;
	private BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public BeanFault<Integer, GxNamespaceBean> getGxNamespaceBeanFault() {
		return gxNamespaceBeanFault;
	}

	public void setGxNamespaceBeanFault(BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault) {
		this.gxNamespaceBeanFault = gxNamespaceBeanFault;
	}

	public String getResourceDescription() {
		return resourceDescription;
	}

	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}
}
