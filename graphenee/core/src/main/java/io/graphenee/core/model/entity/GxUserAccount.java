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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the gx_user_account database table.
 */
@Entity
@Table(name = "gx_user_account")
@NamedQuery(name = "GxUserAccount.findAll", query = "SELECT g FROM GxUserAccount g")
public class GxUserAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "account_activation_date")
	private Timestamp accountActivationDate;

	@Column(name = "count_login_failed")
	private Integer countLoginFailed;

	private String email;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "full_name_native")
	private String fullNameNative;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_locked")
	private Boolean isLocked;

	@Column(name = "is_password_change_required")
	private Boolean isPasswordChangeRequired;

	@Column(name = "is_protected")
	private Boolean isProtected;

	@Column(name = "last_login_date")
	private Timestamp lastLoginDate;

	@Column(name = "last_login_failed_date")
	private Timestamp lastLoginFailedDate;

	@Column(name = "last_name")
	private String lastName;

	private String password;

	@Column(name = "profile_image")
	private byte[] profileImage;

	private String username;

	@Column(name = "verification_token")
	private String verificationToken;

	@Column(name = "verification_token_expiry_date")
	private Timestamp verificationTokenExpiryDate;

	// bi-directional many-to-one association to GxAuditLog
	@OneToMany(mappedBy = "gxUserAccount")
	private List<GxAuditLog> gxAuditLogs = new ArrayList<>();

	// bi-directional many-to-one association to GxGender
	@ManyToOne
	@JoinColumn(name = "oid_gender")
	private GxGender gxGender;

	// bi-directional many-to-many association to GxSecurityGroup
	@ManyToMany
	@JoinTable(name = "gx_user_account_security_group_join", joinColumns = { @JoinColumn(name = "oid_user_account") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_group") })
	private List<GxSecurityGroup> gxSecurityGroups = new ArrayList<>();

	// bi-directional many-to-many association to GxSecurityPolicy
	@ManyToMany
	@JoinTable(name = "gx_user_account_security_policy_join", joinColumns = { @JoinColumn(name = "oid_user_account") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_policy") })
	private List<GxSecurityPolicy> gxSecurityPolicies = new ArrayList<>();

	@OneToMany
	@JoinTable(name = "gx_user_account_access_key_join", joinColumns = { @JoinColumn(name = "oid_user_account") }, inverseJoinColumns = { @JoinColumn(name = "oid_access_key") })
	private List<GxAccessKey> gxAccessKeys = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;

	public GxUserAccount() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Timestamp getAccountActivationDate() {
		return this.accountActivationDate;
	}

	public void setAccountActivationDate(Timestamp accountActivationDate) {
		this.accountActivationDate = accountActivationDate;
	}

	public Integer getCountLoginFailed() {
		return this.countLoginFailed;
	}

	public void setCountLoginFailed(Integer countLoginFailed) {
		this.countLoginFailed = countLoginFailed;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFullNameNative() {
		return this.fullNameNative;
	}

	public void setFullNameNative(String fullNameNative) {
		this.fullNameNative = fullNameNative;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Boolean getIsPasswordChangeRequired() {
		return this.isPasswordChangeRequired;
	}

	public void setIsPasswordChangeRequired(Boolean isPasswordChangeRequired) {
		this.isPasswordChangeRequired = isPasswordChangeRequired;
	}

	public Boolean getIsProtected() {
		return this.isProtected;
	}

	public void setIsProtected(Boolean isProtected) {
		this.isProtected = isProtected;
	}

	public Timestamp getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Timestamp getLastLoginFailedDate() {
		return this.lastLoginFailedDate;
	}

	public void setLastLoginFailedDate(Timestamp lastLoginFailedDate) {
		this.lastLoginFailedDate = lastLoginFailedDate;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getProfileImage() {
		return this.profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVerificationToken() {
		return this.verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

	public Timestamp getVerificationTokenExpiryDate() {
		return this.verificationTokenExpiryDate;
	}

	public void setVerificationTokenExpiryDate(Timestamp verificationTokenExpiryDate) {
		this.verificationTokenExpiryDate = verificationTokenExpiryDate;
	}

	public List<GxAuditLog> getGxAuditLogs() {
		return this.gxAuditLogs;
	}

	public void setGxAuditLogs(List<GxAuditLog> gxAuditLogs) {
		this.gxAuditLogs = gxAuditLogs;
	}

	public GxAuditLog addGxAuditLog(GxAuditLog gxAuditLog) {
		getGxAuditLogs().add(gxAuditLog);
		gxAuditLog.setGxUserAccount(this);

		return gxAuditLog;
	}

	public GxAuditLog removeGxAuditLog(GxAuditLog gxAuditLog) {
		getGxAuditLogs().remove(gxAuditLog);
		gxAuditLog.setGxUserAccount(null);

		return gxAuditLog;
	}

	public GxGender getGxGender() {
		return this.gxGender;
	}

	public void setGxGender(GxGender gxGender) {
		this.gxGender = gxGender;
	}

	public List<GxSecurityGroup> getGxSecurityGroups() {
		return this.gxSecurityGroups;
	}

	public void setGxSecurityGroups(List<GxSecurityGroup> gxSecurityGroups) {
		this.gxSecurityGroups = gxSecurityGroups;
	}

	public List<GxSecurityPolicy> getGxSecurityPolicies() {
		return this.gxSecurityPolicies;
	}

	public void setGxSecurityPolicies(List<GxSecurityPolicy> gxSecurityPolicies) {
		this.gxSecurityPolicies = gxSecurityPolicies;
	}

	public List<GxAccessKey> getGxAccessKeys() {
		return gxAccessKeys;
	}

	public void setGxAccessKeys(List<GxAccessKey> gxAccessKeys) {
		this.gxAccessKeys = gxAccessKeys;
	}

	public GxNamespace getGxNamespace() {
		return gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

}