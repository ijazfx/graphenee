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

import org.springframework.boot.jdbc.DatabaseDriver;

import io.graphenee.core.util.DataSourceUtil.JdbcUrlSplitter;

public class GrapheneeProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean flywayMigrationEnabled;
	// private DataSource dataSource;
	private String url;
	private String username;
	private String password;

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

	// public DataSource getDataSource() {
	// return dataSource;
	// }
	//
	// public void setDataSource(DataSource dataSource) {
	// this.dataSource = dataSource;
	// }
	//
	// public GrapheneeProperties withDataSource(DataSource dataSource) {
	// setDataSource(dataSource);
	// return this;
	// }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public GrapheneeProperties withUrl(String url) {
		setUrl(url);
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public GrapheneeProperties withUsername(String username) {
		setUsername(username);
		return this;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GrapheneeProperties withPassword(String password) {
		setPassword(password);
		return this;
	}

	public String driverClassName() {
		return DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
	}

	public String xaDriverClassName() {
		return DatabaseDriver.fromJdbcUrl(url).getXaDataSourceClassName();

	}

	public String driverName() {
		JdbcUrlSplitter jdbc = new JdbcUrlSplitter(getUrl());
		return jdbc.driverName;
	}

}
