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

/**
 * The persistent class for the gx_security_group database table.
 */
@Entity
@Table(name = "gx_security_group")
@NamedQuery(name = "GxSecurityGroup.findAll", query = "SELECT g FROM GxSecurityGroup g")
public class GxSecurityGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_protected")
	private Boolean isProtected;

	private Integer priority;

	@Column(name = "security_group_name")
	private String securityGroupName;

	@Column(name = "security_group_description")
	private String securityGroupDescription;

	// bi-directional many-to-one association to GxNamespace
	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;

	@ManyToMany
	@JoinTable(name = "gx_security_group_security_policy_join", joinColumns = { @JoinColumn(name = "oid_security_group") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_policy") })
	private List<GxSecurityPolicy> gxSecurityPolicies = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "gx_user_account_security_group_join", joinColumns = { @JoinColumn(name = "oid_security_group") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_user_account") })
	private List<GxUserAccount> gxUserAccounts = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "gx_access_key_security_group_join", joinColumns = { @JoinColumn(name = "oid_security_group") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_access_key") })
	private List<GxAccessKey> gxAccessKeys = new ArrayList<>();

	public GxSecurityGroup() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsProtected() {
		return this.isProtected;
	}

	public void setIsProtected(Boolean isProtected) {
		this.isProtected = isProtected;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getSecurityGroupName() {
		return this.securityGroupName;
	}

	public void setSecurityGroupName(String securityGroupName) {
		this.securityGroupName = securityGroupName;
	}

	public String getSecurityGroupDescription() {
		return securityGroupDescription;
	}

	public void setSecurityGroupDescription(String securityGroupDescription) {
		this.securityGroupDescription = securityGroupDescription;
	}

	public GxNamespace getGxNamespace() {
		return this.gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

	public List<GxSecurityPolicy> getGxSecurityPolicies() {
		return this.gxSecurityPolicies;
	}

	public void setGxSecurityPolicies(List<GxSecurityPolicy> gxSecurityPolicies) {
		this.gxSecurityPolicies = gxSecurityPolicies;
	}

	public List<GxUserAccount> getGxUserAccounts() {
		return this.gxUserAccounts;
	}

	public void setGxUserAccounts(List<GxUserAccount> gxUserAccounts) {
		this.gxUserAccounts = gxUserAccounts;
	}

	public List<GxAccessKey> getGxAccessKeys() {
		return gxAccessKeys;
	}

	public void setGxAccessKeys(List<GxAccessKey> gxAccessKeys) {
		this.gxAccessKeys = gxAccessKeys;
	}

}