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
 * The persistent class for the gx_gender database table.
 */
@Entity
@Table(name = "gx_gender")
@NamedQuery(name = "GxGender.findAll", query = "SELECT g FROM GxGender g")
public class GxGender implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "gender_code")
	private String genderCode;

	@Column(name = "gender_name")
	private String genderName;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_protected")
	private Boolean isProtected;

	// bi-directional many-to-one association to GxUserAccount
	@OneToMany(mappedBy = "gxGender")
	private List<GxUserAccount> gxUserAccounts = new ArrayList<>();

	public GxGender() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getGenderCode() {
		return this.genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getGenderName() {
		return this.genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
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

	public List<GxUserAccount> getGxUserAccounts() {
		return this.gxUserAccounts;
	}

	public void setGxUserAccounts(List<GxUserAccount> gxUserAccounts) {
		this.gxUserAccounts = gxUserAccounts;
	}

	public GxUserAccount addGxUserAccount(GxUserAccount gxUserAccount) {
		getGxUserAccounts().add(gxUserAccount);
		gxUserAccount.setGxGender(this);

		return gxUserAccount;
	}

	public GxUserAccount removeGxUserAccount(GxUserAccount gxUserAccount) {
		getGxUserAccounts().remove(gxUserAccount);
		gxUserAccount.setGxGender(null);

		return gxUserAccount;
	}

}