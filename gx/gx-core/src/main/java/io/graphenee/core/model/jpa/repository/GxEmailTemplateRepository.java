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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxEmailTemplate;

@Repository
public interface GxEmailTemplateRepository extends JpaRepository<GxEmailTemplate, Integer> {

	List<GxEmailTemplate> findAllByGxNamespaceOidOrderByTemplateName(Integer oidNamespace);

	List<GxEmailTemplate> findAllByGxNamespaceOidAndIsActiveOrderByTemplateName(Integer oidNamespace, Boolean isActive);

	List<GxEmailTemplate> findAllByIsActiveOrderByTemplateName(Boolean isActive);

	GxEmailTemplate findOneByTemplateNameAndIsActive(String templateName, Boolean isActive);

	GxEmailTemplate findOneByTemplateCodeAndIsActive(String templateCode, Boolean isActive);

	GxEmailTemplate findOneByTemplateNameAndGxNamespaceOidAndIsActive(String templateName, Integer oidNamespace, Boolean isActive);

	GxEmailTemplate findOneByTemplateCodeAndGxNamespaceOidAndIsActive(String templateCode, Integer oidNamespace, Boolean isActive);

}
