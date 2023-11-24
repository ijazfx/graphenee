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
 * The persistent class for the gx_city database table.
 */
@Entity
@Table(name = "gx_city")
@NamedQuery(name = "GxCity.findAll", query = "SELECT g FROM GxCity g")
public class GxCity extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "city_name")
	private String cityName;

	@Column(name = "is_active")
	private Boolean isActive;

	// bi-directional many-to-one association to GxCountry
	@ManyToOne
	@JoinColumn(name = "oid_gx_country")
	private GxCountry gxCountry;

	// bi-directional many-to-one association to GxState
	@ManyToOne
	@JoinColumn(name = "oid_gx_state")
	private GxState gxState;

	public GxCity() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public GxCountry getGxCountry() {
		return this.gxCountry;
	}

	public void setGxCountry(GxCountry gxCountry) {
		this.gxCountry = gxCountry;
	}

	public GxState getGxState() {
		return this.gxState;
	}

	public void setGxState(GxState gxState) {
		this.gxState = gxState;
	}

}