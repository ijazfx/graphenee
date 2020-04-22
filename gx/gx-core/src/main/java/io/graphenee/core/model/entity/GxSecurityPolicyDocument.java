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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the gx_security_policy_document database table.
 */
@Entity
@Table(name = "gx_security_policy_document")
@NamedQuery(name = "GxSecurityPolicyDocument.findAll", query = "SELECT g FROM GxSecurityPolicyDocument g")
public class GxSecurityPolicyDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	private String tag;

	@Column(name = "document_json")
	private String documentJson;

	@Column(name = "is_default")
	private Boolean isDefault;

	// bi-directional many-to-one association to GxSecurityPolicy
	@ManyToOne
	@JoinColumn(name = "oid_security_policy")
	private GxSecurityPolicy gxSecurityPolicy;

	public GxSecurityPolicyDocument() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDocumentJson() {
		return this.documentJson;
	}

	public void setDocumentJson(String documentJson) {
		this.documentJson = documentJson;
	}

	public Boolean getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public GxSecurityPolicy getGxSecurityPolicy() {
		return this.gxSecurityPolicy;
	}

	public void setGxSecurityPolicy(GxSecurityPolicy gxSecurityPolicy) {
		this.gxSecurityPolicy = gxSecurityPolicy;
	}

}