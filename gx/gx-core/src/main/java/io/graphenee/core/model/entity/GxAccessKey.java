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
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
	private String secret = RandomStringUtils.secureStrong().nextAlphanumeric(64);
	private Boolean isActive = true;
	private Integer accessKeyType;

	@ManyToMany
	@JoinTable(name = "gx_access_key_security_group_join", joinColumns = {
			@JoinColumn(name = "oid_access_key") }, inverseJoinColumns = {
					@JoinColumn(name = "oid_security_group") })
	private List<GxSecurityGroup> securityGroups = new ArrayList<>();
	@ManyToMany
	@JoinTable(name = "gx_access_key_security_policy_join", joinColumns = {
			@JoinColumn(name = "oid_access_key") }, inverseJoinColumns = {
					@JoinColumn(name = "oid_security_policy") })
	private List<GxSecurityPolicy> securityPolicies = new ArrayList<>();

	@ManyToOne
	@JoinTable(name = "gx_user_account_access_key_join", joinColumns = {
			@JoinColumn(name = "oid_access_key") }, inverseJoinColumns = { @JoinColumn(name = "oid_user_account") })
	private GxUserAccount userAccount;

	public boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap) {
		return canDoAction(resource, action, keyValueMap, false);
	}

	public boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap, boolean forceRefresh) {
		return userAccount != null && userAccount.canDoAction(resource, action, keyValueMap, forceRefresh);
	}

}
