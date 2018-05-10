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
 * The persistent class for the gx_country database table.
 */
@Entity
@Table(name = "gx_country")
@NamedQuery(name = "GxCountry.findAll", query = "SELECT g FROM GxCountry g")
public class GxCountry extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "alpha3_code")
	private String alpha3Code;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "numeric_code")
	private Integer numericCode;

	// bi-directional many-to-one association to GxCity
	@OneToMany(mappedBy = "gxCountry")
	private List<GxCity> gxCities = new ArrayList<>();

	// bi-directional many-to-one association to GxState
	@OneToMany(mappedBy = "gxCountry")
	private List<GxState> gxStates = new ArrayList<>();

	public GxCountry() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getAlpha3Code() {
		return this.alpha3Code;
	}

	public void setAlpha3Code(String alpha3Code) {
		this.alpha3Code = alpha3Code;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getNumericCode() {
		return this.numericCode;
	}

	public void setNumericCode(Integer numericCode) {
		this.numericCode = numericCode;
	}

	public List<GxCity> getGxCities() {
		return this.gxCities;
	}

	public void setGxCities(List<GxCity> gxCities) {
		this.gxCities = gxCities;
	}

	public GxCity addGxCity(GxCity gxCity) {
		getGxCities().add(gxCity);
		gxCity.setGxCountry(this);

		return gxCity;
	}

	public GxCity removeGxCity(GxCity gxCity) {
		getGxCities().remove(gxCity);
		gxCity.setGxCountry(null);

		return gxCity;
	}

	public List<GxState> getGxStates() {
		return this.gxStates;
	}

	public void setGxStates(List<GxState> gxStates) {
		this.gxStates = gxStates;
	}

	public GxState addGxState(GxState gxState) {
		getGxStates().add(gxState);
		gxState.setGxCountry(this);

		return gxState;
	}

	public GxState removeGxState(GxState gxState) {
		getGxStates().remove(gxState);
		gxState.setGxCountry(null);

		return gxState;
	}

}