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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "gx_access_key")
@NamedQuery(name = "GxAccessKey.findAll", query = "select g from GxAccessKey g")
public class GxAccessKey extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "access_key")
	private UUID accessKey;

	private String secret;

	@Column(name = "is_Active")
	private Boolean isActive;

	@Column(name = "access_key_type")
	private Integer accessKeyType;

	@ManyToMany
	@JoinTable(name = "gx_access_key_security_group_join", joinColumns = { @JoinColumn(name = "oid_access_key") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_group") })
	private List<GxSecurityGroup> gxSecurityGroups = new ArrayList<>();
	@ManyToMany
	@JoinTable(name = "gx_access_key_security_policy_join", joinColumns = { @JoinColumn(name = "oid_access_key") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_policy") })
	private List<GxSecurityPolicy> gxSecurityPolicys = new ArrayList<>();

	@ManyToOne
	@JoinTable(name = "gx_user_account_access_key_join", joinColumns = { @JoinColumn(name = "oid_access_key") }, inverseJoinColumns = { @JoinColumn(name = "oid_user_account") })
	private GxUserAccount gxUserAccount;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public UUID getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(UUID accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getAccessKeyType() {
		return accessKeyType;
	}

	public void setAccessKeyType(Integer accessKeyType) {
		this.accessKeyType = accessKeyType;
	}

	public List<GxSecurityGroup> getGxSecurityGroups() {
		return gxSecurityGroups;
	}

	public void setGxSecurityGroups(List<GxSecurityGroup> gxSecurityGroups) {
		this.gxSecurityGroups = gxSecurityGroups;
	}

	public List<GxSecurityPolicy> getGxSecurityPolicys() {
		return gxSecurityPolicys;
	}

	public void setGxSecurityPolicys(List<GxSecurityPolicy> gxSecurityPolicys) {
		this.gxSecurityPolicys = gxSecurityPolicys;
	}

	public GxUserAccount getGxUserAccount() {
		return gxUserAccount;
	}

	public void setGxUserAccount(GxUserAccount gxUserAccount) {
		this.gxUserAccount = gxUserAccount;
	}

}
