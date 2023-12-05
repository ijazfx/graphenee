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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_security_policy")
public class GxSecurityPolicy extends GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean isActive = true;
	private Boolean isProtected = false;
	private Integer priority = 0;
	private String securityPolicyName;
	private String securityPolicyDescription;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace namespace;

	@ManyToMany
	@JoinTable(name = "gx_security_group_security_policy_join", joinColumns = { @JoinColumn(name = "oid_security_policy") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_security_group") })
	private Set<GxSecurityGroup> securityGroups = new HashSet<>();

	@OneToMany(mappedBy = "securityPolicy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GxSecurityPolicyDocument> securityPolicyDocuments = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "gx_user_account_security_policy_join", joinColumns = { @JoinColumn(name = "oid_security_policy") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_user_account") })
	private Set<GxUserAccount> userAccounts = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "gx_access_key_security_policy_join", joinColumns = { @JoinColumn(name = "oid_security_policy") }, inverseJoinColumns = {
			@JoinColumn(name = "oid_access_key") })
	private Set<GxAccessKey> accessKeys = new HashSet<>();

	public GxSecurityPolicyDocument addSecurityPolicyDocument(GxSecurityPolicyDocument document) {
		securityPolicyDocuments.add(document);
		document.setSecurityPolicy(this);
		return document;
	}

	public GxSecurityPolicyDocument removeSecurityPolicyDocument(GxSecurityPolicyDocument document) {
		securityPolicyDocuments.add(document);
		document.setSecurityPolicy(null);
		return document;
	}

	public GxSecurityPolicyDocument defaultDocument() {
		return securityPolicyDocuments.stream().filter(d -> d.getIsDefault()).findFirst().orElse(null);
	}

}