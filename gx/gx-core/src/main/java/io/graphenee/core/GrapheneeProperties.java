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
package io.graphenee.core;

import java.io.Serializable;

public class GrapheneeProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean flywayMigrationEnabled;
	private String dbVendor;
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;
	private String dbDriverClassName;

	public boolean isFlywayMigrationEnabled() {
		return flywayMigrationEnabled;
	}

	public void setFlywayMigrationEnabled(boolean flywayMigrationEnabled) {
		this.flywayMigrationEnabled = flywayMigrationEnabled;
	}

	public GrapheneeProperties withFlywayMigrationEnabled(boolean flywayMigrationEnabled) {
		setFlywayMigrationEnabled(flywayMigrationEnabled);
		return this;
	}

	public String getDbVendor() {
		return dbVendor;
	}

	public void setDbVendor(String dbVendor) {
		this.dbVendor = dbVendor;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbDriverClassName() {
		return dbDriverClassName;
	}

	public void setDbDriverClassName(String dbDriverClassName) {
		this.dbDriverClassName = dbDriverClassName;
	}

	public GrapheneeProperties withDbVendor(String dbVendor) {
		setDbVendor(dbVendor);
		return this;
	}

	public GrapheneeProperties withDbDriverClassName(String dbDriverClassName) {
		setDbDriverClassName(dbDriverClassName);
		return this;
	}

	public GrapheneeProperties withDbUrl(String dbUrl) {
		setDbUrl(dbUrl);
		return this;
	}

	public GrapheneeProperties withDbUsername(String dbUsername) {
		setDbUsername(dbUsername);
		return this;
	}

	public GrapheneeProperties withDbPassword(String dbPassword) {
		setDbPassword(dbPassword);
		return this;
	}

}
