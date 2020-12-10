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
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.graphenee.core.enums.AccessKeyType;
import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;

public class GxAccessKeyBean implements Serializable {

	private static final Logger L = LoggerFactory.getLogger(GxAccessKeyBean.class);

	private static final long serialVersionUID = 1L;
	private Integer oid;
	private UUID accessKey = UUID.randomUUID();
	private String secret;
	private Boolean isActive = true;
	private Integer accessKeyType;
	private BeanCollectionFault<GxSecurityGroupBean> securityGroupCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxSecurityPolicyBean> securityPolicyCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanFault<Integer, GxUserAccountBean> userAccountBeanFault;

	private Map<String, Set<String>> grantMap;
	private Map<String, Set<String>> revokeMap;

	public GxAccessKeyBean() {
		secret = RandomStringUtils.randomAlphanumeric(64);
	}

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

		return getUserAccountBeanFault() != null && getUserAccountBeanFault().getBean().canDoAction(resource, action, false);
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

	@Override
	public String toString() {
		return getAccessKey().toString();
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
		GxAccessKeyBean other = (GxAccessKeyBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

	public AccessKeyType getAccessKeyType() {
		if (accessKeyType != null)
			switch (accessKeyType) {
			case 0:
				return AccessKeyType.RETINASCAN;
			case 1:
				return AccessKeyType.FINGERPRINT;
			default:
				return AccessKeyType.CARD;
			}
		return null;
	}

	public void setAccessKeyType(AccessKeyType v) {
		accessKeyType = v.typeCode();
	}

	public BeanFault<Integer, GxUserAccountBean> getUserAccountBeanFault() {
		return userAccountBeanFault;
	}

	public void setUserAccountBeanFault(BeanFault<Integer, GxUserAccountBean> userAccountBeanFault) {
		this.userAccountBeanFault = userAccountBeanFault;
	}

}
