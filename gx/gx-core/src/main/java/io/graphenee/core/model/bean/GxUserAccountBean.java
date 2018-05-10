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
package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;

public class GxUserAccountBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer oid;
	private String firstName;
	private String lastName;
	private String fullNameNative;
	private String email;
	private String username;
	private String password;
	private byte[] profileImage;
	private Boolean isActive = true;
	private Boolean isLocked = false;
	private Boolean isProtected = false;
	private GxGenderBean gender;
	private Timestamp accountActivationDate;
	private Integer countLoginFailed = 0;
	private Boolean isPasswordChangeRequired = true;
	private Timestamp lastLoginDate;
	private Timestamp lastLoginFailedDate;
	private String verificationToken;
	private Timestamp verificationTokenExpiryDate;
	private BeanCollectionFault<GxAuditLogBean> auditLogCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxSecurityGroupBean> securityGroupCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxSecurityPolicyBean> securityPolicyCollectionFault = BeanCollectionFault.emptyCollectionFault();

	private BeanFault<Integer, GxNamespaceBean> namespaceFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullNameNative() {
		return fullNameNative;
	}

	public void setFullNameNative(String fullNameNative) {
		this.fullNameNative = fullNameNative;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Boolean getIsProtected() {
		return isProtected;
	}

	public void setIsProtected(Boolean isProtected) {
		this.isProtected = isProtected;
	}

	public GxGenderBean getGender() {
		return gender;
	}

	public void setGender(GxGenderBean gender) {
		this.gender = gender;
	}

	public Timestamp getAccountActivationDate() {
		return accountActivationDate;
	}

	public void setAccountActivationDate(Timestamp accountActivationDate) {
		this.accountActivationDate = accountActivationDate;
	}

	public Integer getCountLoginFailed() {
		return countLoginFailed;
	}

	public void setCountLoginFailed(Integer countLoginFailed) {
		this.countLoginFailed = countLoginFailed;
	}

	public Boolean getIsPasswordChangeRequired() {
		return isPasswordChangeRequired;
	}

	public void setIsPasswordChangeRequired(Boolean isPasswordChangeRequired) {
		this.isPasswordChangeRequired = isPasswordChangeRequired;
	}

	public Timestamp getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Timestamp getLastLoginFailedDate() {
		return lastLoginFailedDate;
	}

	public void setLastLoginFailedDate(Timestamp lastLoginFailedDate) {
		this.lastLoginFailedDate = lastLoginFailedDate;
	}

	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

	public Timestamp getVerificationTokenExpiryDate() {
		return verificationTokenExpiryDate;
	}

	public void setVerificationTokenExpiryDate(Timestamp verificationTokenExpiryDate) {
		this.verificationTokenExpiryDate = verificationTokenExpiryDate;
	}

	public BeanCollectionFault<GxAuditLogBean> getAuditLogCollectionFault() {
		return auditLogCollectionFault;
	}

	public void setAuditLogCollectionFault(BeanCollectionFault<GxAuditLogBean> auditLogCollectionFault) {
		this.auditLogCollectionFault = auditLogCollectionFault;
	}

	public BeanCollectionFault<GxSecurityGroupBean> getSecurityGroupCollectionFault() {
		return securityGroupCollectionFault;
	}

	public void setSecurityGroupCollectionFault(BeanCollectionFault<GxSecurityGroupBean> securityGroupCollectionFault) {
		this.securityGroupCollectionFault = securityGroupCollectionFault;
	}

	public BeanCollectionFault<GxSecurityPolicyBean> getSecurityPolicyCollectionFault() {
		return securityPolicyCollectionFault;
	}

	public void setSecurityPolicyCollectionFault(BeanCollectionFault<GxSecurityPolicyBean> securityPolicyCollectionFault) {
		this.securityPolicyCollectionFault = securityPolicyCollectionFault;
	}

	public BeanFault<Integer, GxNamespaceBean> getNamespaceFault() {
		return namespaceFault;
	}

	public void setNamespaceFault(BeanFault<Integer, GxNamespaceBean> namespaceFault) {
		this.namespaceFault = namespaceFault;
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
		GxUserAccountBean other = (GxUserAccountBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return username;
	}

}
