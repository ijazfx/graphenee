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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Entity
@Table(name = "gx_access_key")
public class GxAccessKey extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID accessKey = UUID.randomUUID();
	private String secret = RandomStringUtils.randomAlphanumeric(64);
	private Boolean isActive = true;
	private Integer accessKeyType;

	@ManyToMany
	@JoinTable(name = "gx_access_key_security_group_join", joinColumns = { @JoinColumn(name = "oid_access_key") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_group") })
	private List<GxSecurityGroup> securityGroups = new ArrayList<>();
	@ManyToMany
	@JoinTable(name = "gx_access_key_security_policy_join", joinColumns = { @JoinColumn(name = "oid_access_key") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_policy") })
	private List<GxSecurityPolicy> securityPolicies = new ArrayList<>();

	@ManyToOne
	@JoinTable(name = "gx_user_account_access_key_join", joinColumns = { @JoinColumn(name = "oid_access_key") }, inverseJoinColumns = { @JoinColumn(name = "oid_user_account") })
	private GxUserAccount userAccount;

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

		return userAccount != null && userAccount.canDoAction(resource, action, false);
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
