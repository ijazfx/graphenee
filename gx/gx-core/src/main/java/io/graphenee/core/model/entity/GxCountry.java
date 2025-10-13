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

import java.util.ArrayList;
import java.util.List;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the gx_country database table.
 */
@Getter
@Setter
@Entity
@Table(name = "gx_country")
public class GxCountry extends GxMappedSuperclass {

	@Column(name = "alpha3_code")
	private String alpha3Code;

	private String countryName;
	private Boolean isActive = true;
	private Integer numericCode;

	// bi-directional many-to-one association to GxCity
	@OneToMany(mappedBy = "country")
	private List<GxCity> cities = new ArrayList<>();

	// bi-directional many-to-one association to GxState
	@OneToMany(mappedBy = "country")
	private List<GxState> states = new ArrayList<>();

}