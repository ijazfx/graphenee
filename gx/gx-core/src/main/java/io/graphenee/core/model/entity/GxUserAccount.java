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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Entity
@Table(name = "gx_user_account")
public class GxUserAccount extends GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	private Timestamp accountActivationDate;
	private Integer countLoginFailed = 0;
	private String email;
	private String firstName;
	private String fullNameNative;
	private Boolean isActive = true;
	private Boolean isLocked = false;
	private Boolean isPasswordChangeRequired = false;
	private Boolean isProtected = false;
	private Timestamp lastLoginDate;
	private Timestamp lastLoginFailedDate;
	private String lastName;
	private String password;
	private byte[] profileImage;
	private String username;
	private String verificationToken;
	private String preferences = "{}";
	private Timestamp verificationTokenExpiryDate;

	@Transient
	private String newPassword;

	@Transient
	private String confirmPassword;

	@OneToMany(mappedBy = "userAccount")
	private List<GxAuditLog> gxAuditLogs = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "oid_gender")
	private GxGender gender;

	@ManyToMany
	@JoinTable(name = "gx_user_account_security_group_join", joinColumns = { @JoinColumn(name = "oid_user_account") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_group") })
	private Set<GxSecurityGroup> securityGroups = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "gx_user_account_security_policy_join", joinColumns = { @JoinColumn(name = "oid_user_account") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_policy") })
	private Set<GxSecurityPolicy> securityPolicies = new HashSet<>();

	@OneToMany
	@JoinTable(name = "gx_user_account_access_key_join", joinColumns = { @JoinColumn(name = "oid_user_account") }, inverseJoinColumns = { @JoinColumn(name = "oid_access_key") })
	private List<GxAccessKey> accessKeys = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace namespace;

	public GxAuditLog addGxAuditLog(GxAuditLog gxAuditLog) {
		getGxAuditLogs().add(gxAuditLog);
		gxAuditLog.setUserAccount(this);

		return gxAuditLog;
	}

	public GxAuditLog removeGxAuditLog(GxAuditLog gxAuditLog) {
		getGxAuditLogs().remove(gxAuditLog);
		gxAuditLog.setUserAccount(null);

		return gxAuditLog;
	}

	@SuppressWarnings("unchecked")
	public <T> T getPreference(String key) {
		JSONObject json = new JSONObject(preferences);
		return (T) json.get(key);
	}

	public <T> void setPreference(String key, T value) {
		JSONObject json = new JSONObject(preferences);
		json.put(key, value);
	}

	public void clearPreference(String key) {
		JSONObject json = new JSONObject(preferences);
		json.remove(key);
	}

	public void createAllPreferences() {
		preferences = "{}";
	}

	@Transient
	private Map<String, Set<String>> grantMap;

	@Transient
	private Map<String, Set<String>> revokeMap;

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
		TreeSet<GxSecurityPolicyDocument> documents = new TreeSet<>(new Comparator<GxSecurityPolicyDocument>() {

			@Override
			public int compare(GxSecurityPolicyDocument doc1, GxSecurityPolicyDocument doc2) {
				return doc1.getSecurityPolicy().getPriority().intValue() < doc2.getSecurityPolicy().getPriority().intValue() ? -1 : 1;
			}
		});

		securityGroups.forEach(group -> {
			group.getSecurityPolicies().forEach(policy -> {
				if (policy.defaultDocument() != null) {
					documents.add(policy.defaultDocument());
				}
			});
		});

		securityPolicies.forEach(policy -> {
			if (policy.defaultDocument() != null) {
				documents.add(policy.defaultDocument());
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
						log.warn(String.format("%s is not a valid permission type.", parts[0]));
					}
				} else {
					log.warn(String.format("[%s] is not a valid statement.", statement));
				}
			}
		});
	}

}