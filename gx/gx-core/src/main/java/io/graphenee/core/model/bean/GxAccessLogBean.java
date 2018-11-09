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
import java.sql.Timestamp;

import io.graphenee.core.model.BeanFault;

public class GxAccessLogBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer oid;
	private BeanFault<Integer, GxAccessKeyBean> gxAccessKeyBeanFault;
	private BeanFault<Integer, GxResourceBean> gxResourceBeanFault;
	private Timestamp accessTime;
	private Boolean isSuccess;
	private Integer accessType;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public BeanFault<Integer, GxAccessKeyBean> getGxAccessKeyBeanFault() {
		return gxAccessKeyBeanFault;
	}

	public void setGxAccessKeyBeanFault(BeanFault<Integer, GxAccessKeyBean> gxAccessKeyBeanFault) {
		this.gxAccessKeyBeanFault = gxAccessKeyBeanFault;
	}

	public BeanFault<Integer, GxResourceBean> getGxResourceBeanFault() {
		return gxResourceBeanFault;
	}

	public void setGxResourceBeanFault(BeanFault<Integer, GxResourceBean> gxResourceBeanFault) {
		this.gxResourceBeanFault = gxResourceBeanFault;
	}

	public Timestamp getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
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
		GxAccessLogBean other = (GxAccessLogBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

	public Integer getAccessType() {
		return accessType;
	}

	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}

}
