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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "gx_access_log")
@NamedQuery(name = "GxAccessLog.findAll", query = "SELECT g FROM GxAccessLog g")
public class GxAccessLog implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;
	@ManyToOne
	@JoinColumn(name = "oid_access_key")
	private GxAccessKey gxAccessKey;
	@ManyToOne
	@JoinColumn(name = "oid_resource")
	private GxResource gxResource;
	@Column(name = "access_time")
	private Timestamp accessTime;
	@Column(name = "is_success")
	private Boolean isSuccess;
	@Column(name = "access_type")
	private Integer accessType;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public GxAccessKey getGxAccessKey() {
		return gxAccessKey;
	}

	public void setGxAccessKey(GxAccessKey gxAccessKey) {
		this.gxAccessKey = gxAccessKey;
	}

	public GxResource getGxResource() {
		return gxResource;
	}

	public void setGxResource(GxResource gxResource) {
		this.gxResource = gxResource;
	}

	public Timestamp getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Integer getAccessType() {
		return accessType;
	}

	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}

}
