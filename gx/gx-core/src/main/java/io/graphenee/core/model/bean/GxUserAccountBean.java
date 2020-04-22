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
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.graphenee.core.enums.GenderEnum;
import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;

public class GxUserAccountBean implements Serializable {

	private static final Logger L = LoggerFactory.getLogger(GxUserAccountBean.class);

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
	private GenderEnum gender;
	private Timestamp accountActivationDate;
	private Integer countLoginFailed = 0;
	private Boolean isPasswordChangeRequired = false;
	private Timestamp lastLoginDate;
	private Timestamp lastLoginFailedDate;
	private String verificationToken;
	private Timestamp verificationTokenExpiryDate;
	private BeanCollectionFault<GxAuditLogBean> auditLogCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxSecurityGroupBean> securityGroupCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxSecurityPolicyBean> securityPolicyCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxAccessKeyBean> accessKeyCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanFault<Integer, GxNamespaceBean> namespaceFault;

	private Map<String, Set<String>> grantMap;
	private Map<String, Set<String>> revokeMap;

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

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
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

	public boolean canDoAction(String resource, String action) {
		return canDoAction(resource, action, false);
	}

	public boolean canDoAction(String resource, String action, boolean forceRefresh) {
		if (forceRefresh) {
			loadMaps();
		}

		String checkForResource = resource != null ? resource.toLowerCase() : "all";
		String actionLowerCase = action.toLowerCase();
		Set<String> grantActionSet = grantMap().get(checkForResource);
		Set<String> revokeActionSet = revokeMap().get(checkForResource);

		if (revokeActionSet != null && revokeActionSet.contains(actionLowerCase))
			return false;

		if (revokeActionSet != null && revokeActionSet.contains("all"))
			return false;

		if (grantActionSet != null && grantActionSet.contains(actionLowerCase))
			return true;

		if (grantActionSet != null && grantActionSet.contains("all"))
			return true;

		if (resource.contains("/")) {
			resource = resource.substring(0, resource.lastIndexOf('/') - 1);
			return canDoAction(resource, actionLowerCase, false);
		}

		grantActionSet = grantMap().get("all");
		revokeActionSet = revokeMap().get("all");

		if (revokeActionSet != null && revokeActionSet.contains("all"))
			return false;

		if (grantActionSet != null && grantActionSet.contains("all"))
			return true;

		return false;
	}

	protected Map<String, Set<String>> grantMap() {
		if (grantMap == null) {
			loadMaps();
		}
		return grantMap;
	}

	protected Map<String, Set<String>> revokeMap() {
		if (revokeMap == null) {
			loadMaps();
		}
		return revokeMap;
	}

	private void loadMaps() {
		grantMap = new ConcurrentHashMap<>();
		revokeMap = new ConcurrentHashMap<>();
		TreeSet<GxSecurityPolicyDocumentBean> documents = new TreeSet<>(new Comparator<GxSecurityPolicyDocumentBean>() {

			@Override
			public int compare(GxSecurityPolicyDocumentBean doc1, GxSecurityPolicyDocumentBean doc2) {
				return doc1.getSecurityPolicyBeanFault().getBean().getPriority().intValue() < doc2.getSecurityPolicyBeanFault().getBean().getPriority().intValue() ? -1 : 1;
			}
		});

		getSecurityGroupCollectionFault().getBeans().forEach(group -> {
			group.getSecurityPolicyCollectionFault().getBeans().forEach(policy -> {
				if (policy.getDefaultSecurityPolicyDocumentBean() != null) {
					documents.add(policy.getDefaultSecurityPolicyDocumentBean());
				}
			});
		});

		getSecurityPolicyCollectionFault().getBeans().forEach(policy -> {
			if (policy.getDefaultSecurityPolicyDocumentBean() != null) {
				documents.add(policy.getDefaultSecurityPolicyDocumentBean());
			}
		});

		documents.forEach(document -> {
			String documentJson = document.getDocumentJson();
			String[] statements = documentJson.split("(;|\n)");
			for (String statement : statements) {
				String[] parts = statement.trim().toLowerCase().split("\\s");
				if (parts.length == 4) {
					String resourceName = parts[3];
					// initialize action set for grants
					Set<String> grantActionSet = grantMap.get(resourceName);
					if (grantActionSet == null) {
						grantActionSet = new HashSet<>();
						grantMap.put(resourceName, grantActionSet);
					}
					// initialize action set for revokes
					Set<String> revokeActionSet = revokeMap.get(resourceName);
					if (revokeActionSet == null) {
						revokeActionSet = new HashSet<>();
						revokeMap.put(resourceName, revokeActionSet);
					}
					// update grants and revokes such that if statement starts
					// with grant, add to grants map and remove from revokes map
					// and if statement starts with revoke, add to revokes map
					// and remove from grants map.
					String[] actions = parts[1].split(",");
					if (parts[0].equalsIgnoreCase("grant")) {
						for (String action : actions) {
							grantActionSet.add(action);
							revokeActionSet.remove(action);
						}
					} else if (parts[0].equalsIgnoreCase("revoke")) {
						for (String action : actions) {
							revokeActionSet.add(action);
							grantActionSet.remove(action);
						}
					} else {
						L.warn(String.format("%s is not a valid permission type.", parts[0]));
					}
				} else {
					L.warn(String.format("[%s] is not a valid statement.", statement));
				}
			}
		});
	}

	public String getFullName() {
		return String.format("%s %s", getFirstName() != null ? getFirstName().trim() : "", getLastName() != null ? getLastName().trim() : "").trim();
	}

	public String getLastNameFirstName() {
		return String.format("%s %s", getLastName() != null ? getLastName().trim() : "", getFirstName() != null ? getFirstName().trim() : "").trim();
	}

	public BeanCollectionFault<GxAccessKeyBean> getAccessKeyCollectionFault() {
		return accessKeyCollectionFault;
	}

	public void setAccessKeyCollectionFault(BeanCollectionFault<GxAccessKeyBean> accessKeyCollectionFault) {
		this.accessKeyCollectionFault = accessKeyCollectionFault;
	}

}
