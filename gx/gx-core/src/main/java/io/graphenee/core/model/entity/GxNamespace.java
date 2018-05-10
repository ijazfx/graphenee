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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the gx_namespace database table.
 */
@Entity
@Table(name = "gx_namespace")
@NamedQuery(name = "GxNamespace.findAll", query = "SELECT g FROM GxNamespace g")
public class GxNamespace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_protected")
	private Boolean isProtected;

	private String namespace;

	@Column(name = "namespace_description")
	private String namespaceDescription;

	// bi-directional many-to-one association to GxEmailTemplate
	@OneToMany(mappedBy = "gxNamespace")
	private List<GxEmailTemplate> gxEmailTemplates = new ArrayList<>();

	// bi-directional many-to-one association to GxNamespaceProperty
	@OneToMany(mappedBy = "gxNamespace")
	private List<GxNamespaceProperty> gxNamespaceProperties = new ArrayList<>();

	// bi-directional many-to-one association to GxSecurityGroup
	@OneToMany(mappedBy = "gxNamespace")
	private List<GxSecurityGroup> gxSecurityGroups = new ArrayList<>();

	// bi-directional many-to-one association to GxSecurityPolicy
	@OneToMany(mappedBy = "gxNamespace")
	private List<GxSecurityPolicy> gxSecurityPolicies = new ArrayList<>();

	// bi-directional many-to-one association to GxTerm
	@OneToMany(mappedBy = "gxNamespace")
	private List<GxTerm> gxTerms = new ArrayList<>();

	public GxNamespace() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsProtected() {
		return this.isProtected;
	}

	public void setIsProtected(Boolean isProtected) {
		this.isProtected = isProtected;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespaceDescription() {
		return this.namespaceDescription;
	}

	public void setNamespaceDescription(String namespaceDescription) {
		this.namespaceDescription = namespaceDescription;
	}

	public List<GxEmailTemplate> getGxEmailTemplates() {
		return this.gxEmailTemplates;
	}

	public void setGxEmailTemplates(List<GxEmailTemplate> gxEmailTemplates) {
		this.gxEmailTemplates = gxEmailTemplates;
	}

	public GxEmailTemplate addGxEmailTemplate(GxEmailTemplate gxEmailTemplate) {
		getGxEmailTemplates().add(gxEmailTemplate);
		gxEmailTemplate.setGxNamespace(this);

		return gxEmailTemplate;
	}

	public GxEmailTemplate removeGxEmailTemplate(GxEmailTemplate gxEmailTemplate) {
		getGxEmailTemplates().remove(gxEmailTemplate);
		gxEmailTemplate.setGxNamespace(null);

		return gxEmailTemplate;
	}

	public List<GxNamespaceProperty> getGxNamespaceProperties() {
		return this.gxNamespaceProperties;
	}

	public void setGxNamespaceProperties(List<GxNamespaceProperty> gxNamespaceProperties) {
		this.gxNamespaceProperties = gxNamespaceProperties;
	}

	public GxNamespaceProperty addGxNamespaceProperty(GxNamespaceProperty gxNamespaceProperty) {
		getGxNamespaceProperties().add(gxNamespaceProperty);
		gxNamespaceProperty.setGxNamespace(this);

		return gxNamespaceProperty;
	}

	public GxNamespaceProperty removeGxNamespaceProperty(GxNamespaceProperty gxNamespaceProperty) {
		getGxNamespaceProperties().remove(gxNamespaceProperty);
		gxNamespaceProperty.setGxNamespace(null);

		return gxNamespaceProperty;
	}

	public List<GxSecurityGroup> getGxSecurityGroups() {
		return this.gxSecurityGroups;
	}

	public void setGxSecurityGroups(List<GxSecurityGroup> gxSecurityGroups) {
		this.gxSecurityGroups = gxSecurityGroups;
	}

	public GxSecurityGroup addGxSecurityGroup(GxSecurityGroup gxSecurityGroup) {
		getGxSecurityGroups().add(gxSecurityGroup);
		gxSecurityGroup.setGxNamespace(this);

		return gxSecurityGroup;
	}

	public GxSecurityGroup removeGxSecurityGroup(GxSecurityGroup gxSecurityGroup) {
		getGxSecurityGroups().remove(gxSecurityGroup);
		gxSecurityGroup.setGxNamespace(null);

		return gxSecurityGroup;
	}

	public List<GxSecurityPolicy> getGxSecurityPolicies() {
		return this.gxSecurityPolicies;
	}

	public void setGxSecurityPolicies(List<GxSecurityPolicy> gxSecurityPolicies) {
		this.gxSecurityPolicies = gxSecurityPolicies;
	}

	public GxSecurityPolicy addGxSecurityPolicy(GxSecurityPolicy gxSecurityPolicy) {
		getGxSecurityPolicies().add(gxSecurityPolicy);
		gxSecurityPolicy.setGxNamespace(this);

		return gxSecurityPolicy;
	}

	public GxSecurityPolicy removeGxSecurityPolicy(GxSecurityPolicy gxSecurityPolicy) {
		getGxSecurityPolicies().remove(gxSecurityPolicy);
		gxSecurityPolicy.setGxNamespace(null);

		return gxSecurityPolicy;
	}

	public List<GxTerm> getGxTerms() {
		return this.gxTerms;
	}

	public void setGxTerms(List<GxTerm> gxTerms) {
		this.gxTerms = gxTerms;
	}

	public GxTerm addGxTerm(GxTerm gxTerm) {
		getGxTerms().add(gxTerm);
		gxTerm.setGxNamespace(this);

		return gxTerm;
	}

	public GxTerm removeGxTerm(GxTerm gxTerm) {
		getGxTerms().remove(gxTerm);
		gxTerm.setGxNamespace(null);

		return gxTerm;
	}

}