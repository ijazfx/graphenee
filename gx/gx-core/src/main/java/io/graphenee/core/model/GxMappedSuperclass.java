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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.graphenee.common.GxAuditable;
import io.graphenee.common.GxSortable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class GxMappedSuperclass implements Serializable, GxSortable, GxAuditable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@CreatedBy
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private LocalDateTime dateCreated;

	@LastModifiedBy
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private LocalDateTime dateModified;

	// @TODO: To be used in future after careful review and modification to the
	// framework.
	// @Version
	private Integer recordVersion;

	private Integer sortOrder = 1;

	@Transient
	private UUID uuid = UUID.randomUUID();

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		GxMappedSuperclass src = (GxMappedSuperclass) obj;
		if (oid != null && src.oid != null)
			return oid.equals(src.oid);
		return uuid.equals(src.uuid);
	}

	@Override
	public int hashCode() {
		if (oid != null)
			return Objects.hashCode(oid);
		return Objects.hashCode(uuid);
	}

}
