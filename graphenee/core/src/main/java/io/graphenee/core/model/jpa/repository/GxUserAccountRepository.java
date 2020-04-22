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

import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxUserAccountRepository extends GxJpaRepository<GxUserAccount, Integer> {

	List<GxUserAccount> findAllByGxNamespaceOid(Integer oidNamespace);

	List<GxUserAccount> findAllByGxSecurityGroupsOidEquals(Integer oidSecurityGroup);

	List<GxUserAccount> findAllByGxSecurityPoliciesOidEquals(Integer oidSecurityPolicy);

	GxUserAccount findByUsernameAndGxNamespaceOid(String username, Integer oidNamespace);

	GxUserAccount findByUsernameAndGxNamespaceIsNull(String username);

	GxUserAccount findByUsername(String username);

	GxUserAccount findByGxAccessKeysAccessKeyAndGxAccessKeysIsActiveTrue(UUID accessKey);

	GxUserAccount findByUsernameAndPassword(String username, String password);

	GxUserAccount findByUsernameAndPasswordAndGxNamespaceOid(String username, String password, Integer oidNamespace);

	GxUserAccount findByUsernameAndPasswordAndGxNamespaceIsNull(String username, String password);

}
