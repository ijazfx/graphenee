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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the gx_saved_query database table.
 */
@Entity
@Table(name = "gx_saved_query")
@NamedQuery(name = "GxSavedQuery.findAll", query = "SELECT s FROM GxSavedQuery s")
public class GxSavedQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "query_bean_json")
	private String queryBeanJson;

	@Column(name = "query_name")
	private String queryName;

	@Column(name = "target_user")
	private String targetUser;

	@Column(name = "query_bean_class_name")
	private String queryBeanClassName;

	@Column(name = "additional_info")
	private String additionalInfo;

	public GxSavedQuery() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getQueryBeanJson() {
		return this.queryBeanJson;
	}

	public void setQueryBeanJson(String queryBeanJson) {
		this.queryBeanJson = queryBeanJson;
	}

	public String getQueryName() {
		return this.queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getTargetUser() {
		return this.targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public String getQueryBeanClassName() {
		return queryBeanClassName;
	}

	public void setQueryBeanClassName(String queryBeanClassName) {
		this.queryBeanClassName = queryBeanClassName;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

}