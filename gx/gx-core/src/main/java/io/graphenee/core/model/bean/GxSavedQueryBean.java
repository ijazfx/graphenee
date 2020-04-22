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
package io.graphenee.core.model.bean;

import java.io.Serializable;

public class GxSavedQueryBean implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer oid;
	String queryName;
	String targetUser;
	String queryBeanJson;
	String queryBeanClassName;
	String additionalInfo;

	public GxSavedQueryBean() {
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public String getQueryBeanJson() {
		return queryBeanJson;
	}

	public void setQueryBeanJson(String queryBeanJson) {
		this.queryBeanJson = queryBeanJson;
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
