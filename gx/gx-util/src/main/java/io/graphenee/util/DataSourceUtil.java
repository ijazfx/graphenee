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
package io.graphenee.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceUtil {

	public static DataSource createDataSource(String url, String username, String password) {
		DriverManagerDataSource ds = new DriverManagerDataSource(url, username, password);
		return ds;
	}

	public static String determineDbVendor(DataSource dataSource) {
		String vendor = null;
		try (Connection c = dataSource.getConnection()) {
			vendor = c.getMetaData().getDatabaseProductName().replaceAll("\\s", "").toLowerCase();
			if (vendor.contains("postgresql"))
				vendor = "postgresql";
			else if (vendor.contains("sqlserver"))
				vendor = "sqlserver";
			else if (vendor.contains("h2"))
				vendor = "h2";
			else
				vendor = c.getMetaData().getDatabaseProductName().toLowerCase();
		} catch (SQLException e) {
			vendor = "unknown";
		}
		return vendor;
	}

	public static Dialect determineDialect(DataSource dataSource) {
		String vendor = null;
		try (Connection c = dataSource.getConnection()) {
			vendor = c.getMetaData().getDatabaseProductName().replaceAll("\\s", "").toLowerCase();
			if (vendor.contains("postgresql")) {
				return new PostgreSQLDialect();
			} else if (vendor.contains("sqlserver")) {
				return new SQLServerDialect();
			} else if (vendor.contains("h2")) {
				return new H2Dialect();
			} else
				throw new Error("Unable to determine dialect from datasource.");
		} catch (SQLException e) {
			throw new Error("Unable to determine hibernate dialect for " + vendor);
		}
	}

}
