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
package io.graphenee.jbpm.embedded;

import java.io.Serializable;

import javax.sql.DataSource;

import io.graphenee.util.DataSourceUtil;

/**
 * A class that represents the properties for Graphenee jBPM.
 */
public class GrapheneeJbpmProperties implements Serializable {

	/**
	 * Creates a new instance of this class.
	 */
	public GrapheneeJbpmProperties() {
		// a default constructor
	}

	private static final long serialVersionUID = 1L;

	private String h2dbFilePath = System.getProperty("user.home") + "/jbpm/jbpm.db";
	private DataSource dataSource;

	/**
	 * Sets the H2 database file path.
	 * @param dbFilePath The H2 database file path.
	 * @return This instance.
	 */
	public GrapheneeJbpmProperties withH2dbFilePath(String dbFilePath) {
		setH2dbFilePath(dbFilePath);
		return this;
	}

	/**
	 * Gets the data source.
	 * @return The data source.
	 */
	public DataSource getDataSource() {
		if (dataSource == null) {
			synchronized (GrapheneeJbpmProperties.class) {
				if (dataSource == null) {
					dataSource = DataSourceUtil.createDataSource("jdbc:h2:" + getH2dbFilePath(), "sa", null);
				}
				return dataSource;
			}
		}
		return dataSource;
	}

	/**
	 * Sets the data source.
	 * @param dataSource The data source.
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the H2 database file path.
	 * @return The H2 database file path.
	 */
	public String getH2dbFilePath() {
		return h2dbFilePath;
	}

	/**
	 * Sets the H2 database file path.
	 * @param h2dbFilePath The H2 database file path.
	 */
	public void setH2dbFilePath(String h2dbFilePath) {
		this.h2dbFilePath = h2dbFilePath;
	}

}
