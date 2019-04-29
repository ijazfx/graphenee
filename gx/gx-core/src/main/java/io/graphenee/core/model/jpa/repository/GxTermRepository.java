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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxTerm;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxTermRepository extends GxJpaRepository<GxTerm, Integer> {

	List<GxTerm> findByTermKeyAndGxSupportedLocaleLocaleCodeStartingWith(String termKey, String localeCode);

	GxTerm findTopByTermKeyAndGxSupportedLocaleLocaleCodeStartingWithOrderByOidDesc(String termKey, String localeCode);

	List<GxTerm> findByTermKey(String termKey);

	@Modifying
	@Query("Delete from GxTerm t WHERE t.termKey = :termKey AND t.gxNamespace.oid = :oidNamespace")
	void deleteByTermKeyAndOidNameSpace(@Param("termKey") String termKey, @Param("oidNamespace") Integer oidNamespace);

	Page<GxTerm> findByGxNamespaceOid(Pageable pageable, Integer oidNamespace);

	List<GxTerm> findByGxNamespaceOid(Integer oidNamespace);

	Page<GxTerm> findByGxSupportedLocaleOid(Pageable pageable, Integer oidSupportedLocale);

	List<GxTerm> findByGxSupportedLocaleOid(Integer oidSupportedLocale);

	Page<GxTerm> findByGxNamespaceOidAndGxSupportedLocaleOid(Pageable pageable, Integer oidNamespace, Integer oidSupportedLocale);

	List<GxTerm> findByGxNamespaceOidAndGxSupportedLocaleOid(Integer oidNamespace, Integer oidSupportedLocale);

}
