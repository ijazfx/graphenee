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

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;
import io.graphenee.util.enums.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
	private String preferences = "{}";
	private BeanCollectionFault<GxAuditLogBean> auditLogCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxSecurityGroupBean> securityGroupCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxSecurityPolicyBean> securityPolicyCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxAccessKeyBean> accessKeyCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanFault<Integer, GxNamespaceBean> namespaceFault;

	private Map<String, Set<String>> grantMap;
	private Map<String, Set<String>> revokeMap;

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

	@SuppressWarnings("unchecked")
	public <T> T getPreference(String key) {
		JSONObject json = new JSONObject(this.preferences);
		return (T) json.get(key);
	}

	public <T> void setPreference(String key, T value) {
		JSONObject json = new JSONObject(this.preferences);
		json.put(key, value);
		this.preferences = json.toString();
	}

	public void clearPreference(String key) {
		JSONObject json = new JSONObject(preferences);
		json.remove(key);
	}

	public void createAllPreferences() {
		preferences = "{}";
	}

}
