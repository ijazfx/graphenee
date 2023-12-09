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
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxUserAccountRepository extends GxJpaRepository<GxUserAccount, Integer> {

	List<GxUserAccount> findAllByIsActive(Boolean isActive, Sort sort);

	List<GxUserAccount> findAllByNamespaceAndIsActive(GxNamespace namespace, Boolean isActive, Sort sort);

	List<GxUserAccount> findAllByNamespace(GxNamespace namespace, Sort sort);

	List<GxUserAccount> findAllBySecurityGroupsEquals(GxSecurityGroup securityGroup, Sort sort);

	List<GxUserAccount> findAllBySecurityGroupsEqualsAndIsActive(GxSecurityGroup securityGroup, Boolean isActive, Sort sort);

	List<GxUserAccount> findAllBySecurityPoliciesEquals(GxSecurityPolicy securityPolicy, Sort sort);

	List<GxUserAccount> findAllBySecurityPoliciesEqualsAndIsActive(GxSecurityPolicy securityPolicy, Boolean isActive, Sort sort);

	GxUserAccount findByUsernameAndNamespace(String username, GxNamespace namespace);

	GxUserAccount findByUsername(String username);

	GxUserAccount findByAccessKeysAccessKeyAndAccessKeysIsActiveTrue(UUID accessKey);

	GxUserAccount findByUsernameAndPassword(String username, String password);

	GxUserAccount findByUsernameAndPasswordAndNamespace(String username, String password, GxNamespace namespace);

	GxUserAccount findByUsernameAndPasswordAndNamespaceIsNull(String username, String password);

}
