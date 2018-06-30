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

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DatabaseDriver;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class DataSourceUtil {

	public static DataSource createDataSource(String url, String username, String password) {
		return DataSourceBuilder.create().url(url).username(username).password(password).build();
	}

	public static DataSource createPostgresqlXaDataSource(String uniqueResourceName, String url, String username, String password, Integer poolSize) {
		PoolingDataSource ds = new PoolingDataSource();
		ds.setUniqueName(uniqueResourceName);
		ds.setAllowLocalTransactions(true);
		ds.setAutomaticEnlistingEnabled(true);
		ds.setClassName(DatabaseDriver.POSTGRESQL.getXaDataSourceClassName());
		JdbcUrlSplitter jdbc = new JdbcUrlSplitter(url);
		if (username != null)
			ds.getDriverProperties().put("user", username);
		if (password != null)
			ds.getDriverProperties().put("password", password);
		if (jdbc.database != null)
			ds.getDriverProperties().put("databaseName", jdbc.database);
		if (jdbc.host != null)
			ds.getDriverProperties().put("serverName", jdbc.host);
		if (jdbc.port != null)
			ds.getDriverProperties().put("portNumber", jdbc.port);
		ds.setMaxPoolSize(poolSize);
		ds.init();
		return ds;
	}

	public static DataSource createSqlServerXaDataSource(String uniqueResourceName, String url, String username, String password, Integer poolSize) {
		PoolingDataSource ds = new PoolingDataSource();
		ds.setUniqueName(uniqueResourceName);
		ds.setAllowLocalTransactions(true);
		ds.setAutomaticEnlistingEnabled(true);
		ds.setClassName(DatabaseDriver.SQLSERVER.getXaDataSourceClassName());
		if (username != null)
			ds.getDriverProperties().put("user", username);
		if (password != null)
			ds.getDriverProperties().put("password", password);
		ds.getDriverProperties().put("URL", url);
		ds.setMaxPoolSize(poolSize);
		ds.init();
		return ds;
	}

	public static DataSource createH2XaDataSource(String uniqueResourceName, String url, String username, String password, Integer poolSize) {
		PoolingDataSource ds = new PoolingDataSource();
		ds.setUniqueName(uniqueResourceName);
		ds.setAllowLocalTransactions(true);
		ds.setAutomaticEnlistingEnabled(true);
		ds.setClassName(DatabaseDriver.H2.getXaDataSourceClassName());
		if (username != null)
			ds.getDriverProperties().put("user", username);
		if (password != null)
			ds.getDriverProperties().put("password", password);
		ds.getDriverProperties().put("url", url);
		ds.setMaxPoolSize(poolSize);
		ds.init();
		return ds;
	}

	public static DataSource createLrcXaDataSource(String uniqueResourceName, String url, String username, String password, Integer poolSize) {
		PoolingDataSource ds = new PoolingDataSource();
		ds.setUniqueName(uniqueResourceName);
		ds.setAllowLocalTransactions(true);
		ds.setAutomaticEnlistingEnabled(true);
		ds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
		String driverClassName = DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
		if (driverClassName != null)
			ds.getDriverProperties().put("driverClassName", driverClassName);
		if (url != null)
			ds.getDriverProperties().put("url", url);
		if (username != null)
			ds.getDriverProperties().put("user", username);
		if (password != null)
			ds.getDriverProperties().put("password", password);
		ds.setMaxPoolSize(poolSize);
		ds.init();
		return ds;
	}

	public static DataSource createXaDataSource(String uniqueResourceName, String url, String username, String password, Integer poolSize) {
		PoolingDataSource ds = new PoolingDataSource();
		ds.setUniqueName(uniqueResourceName);
		ds.setAllowLocalTransactions(true);
		ds.setAutomaticEnlistingEnabled(true);
		DatabaseDriver driver = DatabaseDriver.fromJdbcUrl(url);
		if (driver == DatabaseDriver.POSTGRESQL)
			return createPostgresqlXaDataSource(uniqueResourceName, url, username, password, poolSize);
		if (driver == DatabaseDriver.SQLSERVER)
			return createSqlServerXaDataSource(uniqueResourceName, url, username, password, poolSize);
		if (driver == DatabaseDriver.H2)
			return createH2XaDataSource(uniqueResourceName, url, username, password, poolSize);
		ds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
		String driverClassName = DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
		if (driverClassName != null)
			ds.getDriverProperties().put("driverClassName", driverClassName);
		if (url != null)
			ds.getDriverProperties().put("url", url);
		if (username != null)
			ds.getDriverProperties().put("user", username);
		if (password != null)
			ds.getDriverProperties().put("password", password);
		ds.setMaxPoolSize(poolSize);
		ds.init();
		return ds;
	}

	// public static DataSource createXaDataSource(String uniqueResourceName,
	// String url, String username, String password, int poolSize) {
	// com.atomikos.jdbc.AtomikosDataSourceBean ds = new
	// com.atomikos.jdbc.AtomikosDataSourceBean();
	// JdbcUrlSplitter jdbc = new JdbcUrlSplitter(url);
	// if (jdbc.driverName.equalsIgnoreCase("h2"))
	// return createH2XaDataSource(uniqueResourceName, url, username, password,
	// poolSize);
	// if (jdbc.driverName.equalsIgnoreCase("postgresql"))
	// return createPostgresqlXaDataSource(uniqueResourceName, url, username,
	// password, poolSize);
	// if (jdbc.driverName.equalsIgnoreCase("sqlserver"))
	// return createSqlServerDataSource(uniqueResourceName, url, username,
	// password, poolSize);
	// throw new RuntimeException("XA data source for " + jdbc.driverName + " is
	// not yet support.");
	// }
	//
	// public static DataSource createH2XaDataSource(String uniqueResourceName,
	// String url, String username, String password, int poolSize) {
	// String xaDriverClassName = "org.h2.jdbcx.JdbcDataSource";
	// com.atomikos.jdbc.AtomikosDataSourceBean ds = new
	// com.atomikos.jdbc.AtomikosDataSourceBean();
	// ds.setUniqueResourceName(uniqueResourceName);
	// ds.setXaDataSourceClassName(xaDriverClassName);
	// if (url != null)
	// ds.getXaProperties().put("url", url);
	// if (username != null)
	// ds.getXaProperties().put("user", username);
	// if (password != null)
	// ds.getXaProperties().put("password", password);
	// ds.setPoolSize(poolSize);
	// return ds;
	// }
	//
	// public static DataSource createPostgresqlXaDataSource(String
	// uniqueResourceName, String url, String username, String password, int
	// poolSize) {
	// String xaDriverClassName = "org.postgresql.xa.PGXADataSource";
	// JdbcUrlSplitter jdbc = new JdbcUrlSplitter(url);
	// com.atomikos.jdbc.AtomikosDataSourceBean ds = new
	// com.atomikos.jdbc.AtomikosDataSourceBean();
	// ds.setUniqueResourceName(uniqueResourceName);
	// ds.setXaDataSourceClassName(xaDriverClassName);
	// if (jdbc.host != null)
	// ds.getXaProperties().put("serverName", jdbc.host);
	// if (jdbc.port != null)
	// ds.getXaProperties().put("portNumber", jdbc.port);
	// if (jdbc.database != null)
	// ds.getXaProperties().put("databaseName", jdbc.database);
	// if (username != null)
	// ds.getXaProperties().put("user", username);
	// if (password != null)
	// ds.getXaProperties().put("password", password);
	// ds.setPoolSize(poolSize);
	// return ds;
	// }
	//
	// public static DataSource createSqlServerDataSource(String
	// uniqueResourceName, String url, String username, String password, int
	// poolSize) {
	// String xaDriverClassName =
	// "com.microsoft.sqlserver.jdbc.SQLServerXADataSource";
	// JdbcUrlSplitter jdbc = new JdbcUrlSplitter(url);
	// com.atomikos.jdbc.AtomikosDataSourceBean ds = new
	// com.atomikos.jdbc.AtomikosDataSourceBean();
	// ds.setUniqueResourceName(uniqueResourceName);
	// ds.setXaDataSourceClassName(xaDriverClassName);
	// if (jdbc.host != null)
	// ds.getXaProperties().put("serverName", jdbc.host);
	// if (username != null)
	// ds.getXaProperties().put("user", username);
	// if (password != null)
	// ds.getXaProperties().put("password", password);
	// ds.setPoolSize(poolSize);
	// return ds;
	// }

	/**
	 * Split di una url JDBC nei componenti. Estrae i componenti di una uri JDBC
	 * del tipo: <br>
	 * String url =
	 * "jdbc:derby://localhost:1527/netld;collation=TERRITORY_BASED:PRIMARY";
	 * <br>
	 * nelle rispettive variabili pubbliche.
	 * 
	 * @author Nicola De Nisco
	 */
	public static class JdbcUrlSplitter {
		public String driverName, host, port, database, params;

		public JdbcUrlSplitter(String jdbcUrl) {
			int pos, pos1, pos2;
			String connUri;

			if (jdbcUrl == null || !jdbcUrl.startsWith("jdbc:") || (pos1 = jdbcUrl.indexOf(':', 5)) == -1)
				throw new IllegalArgumentException("Invalid JDBC url.");

			driverName = jdbcUrl.substring(5, pos1);
			if ((pos2 = jdbcUrl.indexOf(';', pos1)) == -1) {
				connUri = jdbcUrl.substring(pos1 + 1);
			} else {
				connUri = jdbcUrl.substring(pos1 + 1, pos2);
				params = jdbcUrl.substring(pos2 + 1);
			}

			if (connUri.startsWith("//")) {
				if ((pos = connUri.indexOf('/', 2)) != -1) {
					host = connUri.substring(2, pos);
					database = connUri.substring(pos + 1);

					if ((pos = host.indexOf(':')) != -1) {
						port = host.substring(pos + 1);
						host = host.substring(0, pos);
					}
				}
			} else {
				database = connUri;
			}
		}
	}

}
