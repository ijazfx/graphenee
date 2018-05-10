/*******************************************************************************
 * Copyright (c) 2016, 2017, Graphenee
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
package com.graphenee.core;

import java.io.Serializable;

import javax.sql.DataSource;

public class GrapheneeProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean flywayMigrationEnabled;
	private DataSource dataSource;
	private String dbVendor;

	public boolean isFlywayMigrationEnabled() {
		return flywayMigrationEnabled;
	}

	public void setFlywayMigrationEnabled(boolean flywayMigrationEnabled) {
		this.flywayMigrationEnabled = flywayMigrationEnabled;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public GrapheneeProperties withFlywayMigrationEnabled(boolean flywayMigrationEnabled) {
		setFlywayMigrationEnabled(flywayMigrationEnabled);
		return this;
	}

	public void setDBVendor(String dbVendor) {
		this.dbVendor = dbVendor;
	}

	public String getDBVendor() {
		return dbVendor;
	}

	public GrapheneeProperties withDBVendor(String dbVendor) {
		setDBVendor(dbVendor);
		return this;

	}

}
