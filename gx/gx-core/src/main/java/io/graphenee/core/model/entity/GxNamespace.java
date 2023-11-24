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
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import io.graphenee.core.model.CollectionFault;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the gx_namespace database table.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "gx_namespace")
@NamedQuery(name = "GxNamespace.findAll", query = "SELECT g FROM GxNamespace g")
public class GxNamespace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "is_protected")
	private Boolean isProtected = false;

	@Include
	private String namespace;

	@Column(name = "namespace_description")
	private String namespaceDescription;

	@Transient
	private CollectionFault<GxNamespaceProperty> namespacePropertyCollectionFault = CollectionFault.emptyCollectionFault();

}