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
package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_audit_log")
@NamedQuery(name = "GxAuditLog.findAll", query = "SELECT g FROM GxAuditLog g")
public class GxAuditLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	private Timestamp auditDate;
	private String auditEntity;
	private String auditEvent;
	private Integer oidAuditEntity;
	private byte[] additionalData;
	private String detail;
	private String username;
	private String remoteAddress;

	@ManyToOne
	@JoinColumn(name = "oid_user_account")
	private GxUserAccount gxUserAccount;

	public String getUsername() {
		if (username == null && gxUserAccount != null) {
			username = gxUserAccount.getUsername();
		}
		return username;
	}

	public Timestamp getTimestamp() {
		return auditDate;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.auditDate = timestamp;
	}

}