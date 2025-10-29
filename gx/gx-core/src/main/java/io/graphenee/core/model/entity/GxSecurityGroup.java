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

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_security_group")
public class GxSecurityGroup extends GxMappedSuperclass implements Principal {

	private static final long serialVersionUID = 1L;

	private Boolean isActive = true;
	private Boolean isProtected = false;
	private Integer priority = 0;
	private String securityGroupName;
	private String securityGroupDescription;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace namespace;

	@ManyToMany
	@JoinTable(name = "gx_security_group_security_policy_join", joinColumns = {
			@JoinColumn(name = "oid_security_group") }, inverseJoinColumns = {
					@JoinColumn(name = "oid_security_policy") })
	private Set<GxSecurityPolicy> securityPolicies = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "gx_user_account_security_group_join", joinColumns = {
			@JoinColumn(name = "oid_security_group") }, inverseJoinColumns = {
					@JoinColumn(name = "oid_user_account") })
	private Set<GxUserAccount> userAccounts = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "gx_access_key_security_group_join", joinColumns = {
			@JoinColumn(name = "oid_security_group") }, inverseJoinColumns = {
					@JoinColumn(name = "oid_access_key") })
	private Set<GxAccessKey> accessKeys = new HashSet<>();

	@Override
	public String getName() {
		return getSecurityGroupName();
	}

	public boolean isMember(GxAuthenticatedUser user) {
		return userAccounts.contains(user);
	}

}