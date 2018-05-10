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
 * The persistent class for the gx_supported_locale database table.
 */
@Entity
@Table(name = "gx_supported_locale")
@NamedQuery(name = "GxSupportedLocale.findAll", query = "SELECT g FROM GxSupportedLocale g")
public class GxSupportedLocale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_left_to_right")
	private Boolean isLeftToRight;

	@Column(name = "is_protected")
	private Boolean isProtected;

	@Column(name = "locale_code")
	private String localeCode;

	@Column(name = "locale_name")
	private String localeName;

	// bi-directional many-to-one association to GxTerm
	@OneToMany(mappedBy = "gxSupportedLocale")
	private List<GxTerm> gxTerms = new ArrayList<>();

	public GxSupportedLocale() {
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

	public Boolean getIsLeftToRight() {
		return this.isLeftToRight;
	}

	public void setIsLeftToRight(Boolean isLeftToRight) {
		this.isLeftToRight = isLeftToRight;
	}

	public Boolean getIsProtected() {
		return this.isProtected;
	}

	public void setIsProtected(Boolean isProtected) {
		this.isProtected = isProtected;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getLocaleName() {
		return this.localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public List<GxTerm> getGxTerms() {
		return this.gxTerms;
	}

	public void setGxTerms(List<GxTerm> gxTerms) {
		this.gxTerms = gxTerms;
	}

	public GxTerm addGxTerm(GxTerm gxTerm) {
		getGxTerms().add(gxTerm);
		gxTerm.setGxSupportedLocale(this);

		return gxTerm;
	}

	public GxTerm removeGxTerm(GxTerm gxTerm) {
		getGxTerms().remove(gxTerm);
		gxTerm.setGxSupportedLocale(null);

		return gxTerm;
	}

}