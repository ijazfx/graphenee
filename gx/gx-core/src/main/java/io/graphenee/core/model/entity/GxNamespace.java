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

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the gx_namespace database table.
 */
@Getter
@Setter
@Entity
@Table(name = "gx_namespace")
public class GxNamespace extends GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SYSTEM = "io.graphenee.system";

	private Boolean isActive = true;
	private Boolean isProtected = false;

	private String namespace;
	private String namespaceDescription;

	@OneToMany(mappedBy = "namespace", cascade = CascadeType.ALL, orphanRemoval = true)
	List<GxNamespaceProperty> properties = new ArrayList<>();

}