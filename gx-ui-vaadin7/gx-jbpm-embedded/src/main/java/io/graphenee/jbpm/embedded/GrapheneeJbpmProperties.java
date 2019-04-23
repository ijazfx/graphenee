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

import io.graphenee.core.util.DataSourceUtil;

public class GrapheneeJbpmProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	private String h2dbFilePath = System.getProperty("user.home") + "/jbpm/jbpm.db";
	private DataSource dataSource;

	public GrapheneeJbpmProperties withH2dbFilePath(String dbFilePath) {
		setH2dbFilePath(dbFilePath);
		return this;
	}

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

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getH2dbFilePath() {
		return h2dbFilePath;
	}

	public void setH2dbFilePath(String h2dbFilePath) {
		this.h2dbFilePath = h2dbFilePath;
	}

}
