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
package io.graphenee.core.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;

public class DataSourceUtil {

	public static DataSource createDataSource(String url, String username, String password) {
		return DataSourceBuilder.create().url(url).username(username).password(password).build();
	}

	public static String determineDbVendor(DataSource dataSource) {
		String vendor = null;
		try (Connection c = dataSource.getConnection()) {
			vendor = c.getMetaData().getDatabaseProductName().toLowerCase();
		} catch (SQLException e) {
			vendor = "unknown";
		}
		return vendor;
	}

	public static Dialect determineDialect(DataSource dataSource) {
		String vendor = null;
		try (Connection c = dataSource.getConnection()) {
			vendor = c.getMetaData().getDatabaseProductName().toLowerCase();
			String hibernateDialect = null;
			switch (vendor) {
			case "postgresql":
				hibernateDialect = "org.hibernate.dialect.PostgreSQL82Dialect";
			break;
			case "sqlserver":
				hibernateDialect = "org.hibernate.dialect.SQLServerDialect";
			break;
			case "h2":
				hibernateDialect = "org.hibernate.dialect.H2Dialect";
			break;
			default:
				throw new Error("Unable to determine dialect from datasource.");
			}
			Properties props = new Properties();
			props.put(Environment.DIALECT, hibernateDialect);
			return Dialect.getDialect(props);
		} catch (SQLException e) {
			throw new Error("Unable to determine hibernate dialect for " + vendor);
		}
	}

}
