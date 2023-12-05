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
package io.graphenee.core.model;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class GxMappedSuperclass {

	// @Version
	// protected Integer version;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Transient
	private UUID uuid = UUID.randomUUID();

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		GxMappedSuperclass src = (GxMappedSuperclass) obj;
		if (oid == null && src.oid == null)
			return uuid.equals(src.uuid);
		return oid.equals(src.oid);
	}

	@Override
	public int hashCode() {
		if (oid != null)
			return Objects.hashCode(oid);
		return Objects.hashCode(uuid);
	}

}
