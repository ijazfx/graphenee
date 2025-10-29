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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.google.common.base.Strings;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.model.GxMappedSuperclass;
import io.graphenee.core.model.bean.GxSecurityPolicyStatement;
import io.graphenee.security.GxSecurityPolicyParser;
import io.graphenee.security.GxSecurityPolicyParserFactory;
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
public class GxUserAccount extends GxMappedSuperclass implements GxAuthenticatedUser {

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
	@JoinTable(name = "gx_user_account_security_group_join", joinColumns = {
			@JoinColumn(name = "oid_user_account") }, inverseJoinColumns = {
					@JoinColumn(name = "oid_security_group") })
	private Set<GxSecurityGroup> securityGroups = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "gx_user_account_security_policy_join", joinColumns = {
			@JoinColumn(name = "oid_user_account") }, inverseJoinColumns = {
					@JoinColumn(name = "oid_security_policy") })
	private Set<GxSecurityPolicy> securityPolicies = new HashSet<>();

	@OneToMany
	@JoinTable(name = "gx_user_account_access_key_join", joinColumns = {
			@JoinColumn(name = "oid_user_account") }, inverseJoinColumns = { @JoinColumn(name = "oid_access_key") })
	private Set<GxAccessKey> accessKeys = new HashSet<>();

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
	private Map<String, Map<String, GxSecurityPolicyStatement>> grantMap;

	@Transient
	private Map<String, Map<String, GxSecurityPolicyStatement>> revokeMap;

	public boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap) {
		return canDoAction(resource, action, keyValueMap, false);
	}

	public boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap, boolean forceRefresh) {
		if (keyValueMap == null)
			keyValueMap = new HashMap<>();
		if (forceRefresh) {
			loadMaps();
		}

		resource = resource != null ? resource.toLowerCase() : "all";
		action = action.toLowerCase();
		Map<String, GxSecurityPolicyStatement> grantActionSet = grantMap().get(resource);
		Map<String, GxSecurityPolicyStatement> revokeActionSet = revokeMap().get(resource);

		if (revokeActionSet != null && revokeActionSet.containsKey(action))
			return false;

		if (revokeActionSet != null && revokeActionSet.containsKey("all"))
			return false;

		if (grantActionSet != null && grantActionSet.containsKey(action)) {
			GxSecurityPolicyStatement spstmt = grantActionSet.get(action);
			return spstmt.evaluate(keyValueMap);
		}

		if (grantActionSet != null && grantActionSet.containsKey("all")) {
			GxSecurityPolicyStatement spstmt = grantActionSet.get("all");
			return spstmt.evaluate(keyValueMap);
		}

		if (resource.contains("/")) {
			resource = resource.substring(0, resource.lastIndexOf('/') - 1);
			return canDoAction(resource, action, keyValueMap, false);
		}

		grantActionSet = grantMap().get("all");
		revokeActionSet = revokeMap().get("all");

		if (revokeActionSet != null && revokeActionSet.containsKey("all"))
			return false;

		if (grantActionSet != null && grantActionSet.containsKey("all")) {
			GxSecurityPolicyStatement spstmt = grantActionSet.get("all");
			return spstmt.evaluate(keyValueMap);
		}

		return false;
	}

	protected Map<String, Map<String, GxSecurityPolicyStatement>> grantMap() {
		if (grantMap == null) {
			loadMaps();
		}
		return grantMap;
	}

	protected Map<String, Map<String, GxSecurityPolicyStatement>> revokeMap() {
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
				return doc1.getSecurityPolicy().getPriority().intValue() < doc2.getSecurityPolicy().getPriority()
						.intValue() ? -1 : 1;
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

		GxSecurityPolicyParser parser = GxSecurityPolicyParserFactory.defaultParser();

		documents.forEach(document -> {
			String documentJson = document.getDocumentJson();
			String[] statements = documentJson.split("(;|\n)");
			for (String statement : statements) {

				GxSecurityPolicyStatement spstmt = parser.parse(statement);

				// initialize action set for grants
				String resourceName = spstmt.getResource();
				Map<String, GxSecurityPolicyStatement> grantActionSet = grantMap.get(resourceName);
				if (grantActionSet == null) {
					grantActionSet = new HashMap<>();
					grantMap.put(resourceName, grantActionSet);
				}
				// initialize action set for revokes
				Map<String, GxSecurityPolicyStatement> revokeActionSet = revokeMap.get(resourceName);
				if (revokeActionSet == null) {
					revokeActionSet = new HashMap<>();
					revokeMap.put(resourceName, revokeActionSet);
				}
				// update grants and revokes such that if statement starts
				// with grant, add to grants map and remove from revokes map
				// and if statement starts with revoke, add to revokes map
				// and remove from grants map.
				if (spstmt.isGrant()) {
					for (String action : spstmt.getActions()) {
						grantActionSet.put(action, spstmt);
						revokeActionSet.remove(action);
					}
				} else if (spstmt.isRevoke()) {
					for (String action : spstmt.getActions()) {
						revokeActionSet.put(action, spstmt);
						grantActionSet.remove(action);
					}
				} else {
					log.warn(String.format("%s is not a valid permission type.", statement));
				}
			}
		});

	}

	public boolean isMember(GxSecurityGroup group) {
		return securityGroups.contains(group);
	}

	@Override
	public byte[] getProfilePhoto() {
		return profileImage;
	}

	@Override
	public String getMobileNumber() {
		return null;
	}

	@Override
	public String getName() {
		if (!Strings.isNullOrEmpty(getEmail())) {
			return getEmail();
		}
		return getUsername();
	}
}