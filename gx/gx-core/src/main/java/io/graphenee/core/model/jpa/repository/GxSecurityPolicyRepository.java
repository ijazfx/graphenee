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

import io.graphenee.core.model.entity.GxAccessKey;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxSecurityPolicyRepository extends GxJpaRepository<GxSecurityPolicy, Integer> {

	List<GxSecurityPolicy> findAllByIsActive(Boolean isActive, Sort sort);

	List<GxSecurityPolicy> findAllByNamespaceAndIsActive(GxNamespace namespace, Boolean isActive, Sort sort);

	List<GxSecurityPolicy> findAllByNamespace(GxNamespace namespace, Sort sort);

	List<GxSecurityPolicy> findAllBySecurityGroupsEquals(GxSecurityGroup securityGroup, Sort sort);

	List<GxSecurityPolicy> findAllBySecurityGroupsEqualsAndIsActive(GxSecurityGroup securityGroup, Boolean isActive, Sort sort);

	List<GxSecurityPolicy> findAllByUserAccountsEquals(GxUserAccount userAccount, Sort sort);

	List<GxSecurityPolicy> findAllByUserAccountsEqualsAndIsActive(GxUserAccount userAccount, Boolean isActive, Sort sort);

	GxSecurityPolicy findAllBySecurityPolicyNameAndNamespace(String policyName, GxNamespace namespace);

	List<GxSecurityPolicy> findAllByAccessKeysEquals(GxAccessKey accessKey, Sort sort);

	GxSecurityPolicy findByAccessKeysAccessKeyAndAccessKeysIsActiveTrueAndIsActiveTrue(UUID accessKey);

	List<GxSecurityPolicy> findByNamespace(GxNamespace namespace, Sort sort);

}
