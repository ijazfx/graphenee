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

public class GxAuditLogBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer oid;
	private Timestamp auditDate;
	private String auditEntity;
	private String auditEvent;
	private Integer oidAuditEntity;
	private byte[] additionalData;

	private BeanFault<Integer, GxUserAccountBean> gxUserAccountBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Timestamp getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditEntity() {
		return auditEntity;
	}

	public void setAuditEntity(String auditEntity) {
		this.auditEntity = auditEntity;
	}

	public String getAuditEvent() {
		return auditEvent;
	}

	public void setAuditEvent(String auditEvent) {
		this.auditEvent = auditEvent;
	}

	public Integer getOidAuditEntity() {
		return oidAuditEntity;
	}

	public void setOidAuditEntity(Integer oidAuditEntity) {
		this.oidAuditEntity = oidAuditEntity;
	}

	public byte[] getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(byte[] additionalData) {
		this.additionalData = additionalData;
	}

	public BeanFault<Integer, GxUserAccountBean> getGxUserAccountBeanFault() {
		return gxUserAccountBeanFault;
	}

	public void setGxUserAccountBeanFault(BeanFault<Integer, GxUserAccountBean> gxUserAccountBeanFault) {
		this.gxUserAccountBeanFault = gxUserAccountBeanFault;
	}

	public String getUsername() {
		return getGxUserAccountBeanFault() != null ? getGxUserAccountBeanFault().getBean().getUsername() : null;
	}

	public String getFullName() {
		return getGxUserAccountBeanFault() != null ? getGxUserAccountBeanFault().getBean().getFullName() : null;
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
		GxAuditLogBean other = (GxAuditLogBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

}
