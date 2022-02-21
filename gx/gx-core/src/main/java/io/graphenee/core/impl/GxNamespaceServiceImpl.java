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
package io.graphenee.core.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.api.GxNamespaceService;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;

@Service
public class GxNamespaceServiceImpl implements GxNamespaceService {

	private static final String SYSTEM_NAMESPACE = "io.graphenee.system";
	private static final String APPLICATION_NAMESPACE = "io.graphenee.core";

	@Autowired
	GxDataService dataService;

	@Autowired
	GxNamespaceRepository repo;

	@Override
	public GxNamespaceBean systemNamespace() {
		return dataService.findNamespace(SYSTEM_NAMESPACE);
	}

	@Override
	public GxNamespaceBean applicationNamespace() {
		return dataService.findNamespace(APPLICATION_NAMESPACE);
	}

	@Override
	public GxNamespaceBean namespace(String namespace) {
		return dataService.findNamespace(namespace);
	}

	@Override
	public GxNamespaceBean findByOid(Integer oidNamespace) {
		return dataService.findNamespace(oidNamespace);
	}

	@Override
	public GxNamespace systemNamespaceEntity() {
		return repo.findByNamespace(SYSTEM_NAMESPACE);
	}

	@Override
	public GxNamespace applicationNamespaceEntity() {
		return repo.findByNamespace(APPLICATION_NAMESPACE);
	}

	@Override
	public GxNamespace namespaceEntity(String namespace) {
		return repo.findByNamespace(namespace);
	}

	@Override
	public GxNamespace findEntityByOid(Integer oidNamespace) {
		return repo.findOne(oidNamespace);
	}

}
