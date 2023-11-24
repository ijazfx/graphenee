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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the gx_term database table.
 * 
 */
@Entity
@Table(name = "gx_term")
@NamedQuery(name = "GxTerm.findAll", query = "SELECT g FROM GxTerm g")
public class GxTerm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_protected")
	private Boolean isProtected;

	@Column(name = "term_key")
	private String termKey;

	@Column(name = "term_plural")
	private String termPlural;

	@Column(name = "term_singular")
	private String termSingular;

	//bi-directional many-to-one association to GxNamespace
	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;

	//bi-directional many-to-one association to GxSupportedLocale
	@ManyToOne
	@JoinColumn(name = "oid_supported_locale")
	private GxSupportedLocale gxSupportedLocale;

	public GxTerm() {
		isActive = true;
		isProtected = false;
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

	public String getTermKey() {
		return this.termKey;
	}

	public void setTermKey(String termKey) {
		this.termKey = termKey;
	}

	public String getTermPlural() {
		return this.termPlural;
	}

	public void setTermPlural(String termPlural) {
		this.termPlural = termPlural;
	}

	public String getTermSingular() {
		return this.termSingular;
	}

	public void setTermSingular(String termSingular) {
		this.termSingular = termSingular;
	}

	public GxNamespace getGxNamespace() {
		return this.gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

	public GxSupportedLocale getGxSupportedLocale() {
		return this.gxSupportedLocale;
	}

	public void setGxSupportedLocale(GxSupportedLocale gxSupportedLocale) {
		this.gxSupportedLocale = gxSupportedLocale;
	}

	public String getLanguage() {
		return gxSupportedLocale != null ? gxSupportedLocale.getLocaleName() : null;
	}

}