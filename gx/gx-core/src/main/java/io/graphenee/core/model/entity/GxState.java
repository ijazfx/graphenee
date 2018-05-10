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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the gx_state database table.
 */
@Entity
@Table(name = "gx_state")
@NamedQuery(name = "GxState.findAll", query = "SELECT g FROM GxState g")
public class GxState extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "state_code")
	private String stateCode;

	@Column(name = "state_name")
	private String stateName;

	// bi-directional many-to-one association to GxCity
	@OneToMany(mappedBy = "gxState")
	private List<GxCity> gxCities = new ArrayList<>();

	// bi-directional many-to-one association to GxCountry
	@ManyToOne
	@JoinColumn(name = "oid_gx_country")
	private GxCountry gxCountry;

	public GxState() {
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

	public String getStateCode() {
		return this.stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return this.stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<GxCity> getGxCities() {
		return this.gxCities;
	}

	public void setGxCities(List<GxCity> gxCities) {
		this.gxCities = gxCities;
	}

	public GxCity addGxCity(GxCity gxCity) {
		getGxCities().add(gxCity);
		gxCity.setGxState(this);

		return gxCity;
	}

	public GxCity removeGxCity(GxCity gxCity) {
		getGxCities().remove(gxCity);
		gxCity.setGxState(null);

		return gxCity;
	}

	public GxCountry getGxCountry() {
		return this.gxCountry;
	}

	public void setGxCountry(GxCountry gxCountry) {
		this.gxCountry = gxCountry;
	}

}