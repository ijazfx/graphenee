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
package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxCity;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxCityRepository extends GxJpaRepository<GxCity, Integer> {

	List<GxCity> findAllByIsActiveTrueOrderByCityNameAsc();

	List<GxCity> findAllByIsActiveTrueAndGxCountryOidOrderByCityNameAsc(Integer oidCountry);

	List<GxCity> findAllByIsActiveTrueAndGxCountryNumericCodeOrderByCityNameAsc(Integer numericCode);

	List<GxCity> findAllByIsActiveTrueAndGxStateOidOrderByCityNameAsc(Integer oidState);

	List<GxCity> findAllByIsActiveTrueAndGxStateStateCodeOrderByCityNameAsc(String stateCode);

	GxCity findOneByCityName(String cityName);
}
