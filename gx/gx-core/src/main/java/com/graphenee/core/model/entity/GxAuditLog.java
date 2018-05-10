/*******************************************************************************
 * Copyright (c) 2016, 2017, Graphenee
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
package com.graphenee.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the gx_audit_log database table.
 * 
 */
@Entity
@Table(name="gx_audit_log")
@NamedQuery(name="GxAuditLog.findAll", query="SELECT g FROM GxAuditLog g")
public class GxAuditLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer oid;

	@Column(name="audit_date")
	private Timestamp auditDate;

	@Column(name="audit_entity")
	private String auditEntity;

	@Column(name="audit_event")
	private String auditEvent;

	@Column(name="oid_audit_entity")
	private Integer oidAuditEntity;

	//bi-directional many-to-one association to GxUserAccount
	@ManyToOne
	@JoinColumn(name="oid_user_account")
	private GxUserAccount gxUserAccount;

	public GxAuditLog() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Timestamp getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditEntity() {
		return this.auditEntity;
	}

	public void setAuditEntity(String auditEntity) {
		this.auditEntity = auditEntity;
	}

	public String getAuditEvent() {
		return this.auditEvent;
	}

	public void setAuditEvent(String auditEvent) {
		this.auditEvent = auditEvent;
	}

	public Integer getOidAuditEntity() {
		return this.oidAuditEntity;
	}

	public void setOidAuditEntity(Integer oidAuditEntity) {
		this.oidAuditEntity = oidAuditEntity;
	}

	public GxUserAccount getGxUserAccount() {
		return this.gxUserAccount;
	}

	public void setGxUserAccount(GxUserAccount gxUserAccount) {
		this.gxUserAccount = gxUserAccount;
	}

}