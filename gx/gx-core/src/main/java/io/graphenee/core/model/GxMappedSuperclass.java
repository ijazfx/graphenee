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

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
public class GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	// @Version
	// protected Integer version;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Transient
	private UUID uuid = UUID.randomUUID();

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		GxMappedSuperclass other = (GxMappedSuperclass) obj;

		if (this.oid != null && other.oid != null) {
			return this.oid.equals(other.oid);
		}

		if (this.uuid != null && other.uuid != null) {
			return this.uuid.equals(other.uuid);
		}

		return false;
	}

	@Override
	public int hashCode() {
		if (oid != null) {
			return oid.hashCode();
		}
		if (uuid != null) {
			return uuid.hashCode();
		}
		return System.identityHashCode(this);
	}

}
