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

import java.sql.Connection;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.callback.BaseFlywayCallback;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.graphenee.core.util.DataSourceUtil;

@Configuration
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", matchIfMissing = false)
@ComponentScan(GrapheneeCoreConfiguration.COMPONENT_SCAN_BASE_PACKAGE)
public class GrapheneeCoreConfiguration {

	public static final String COMPONENT_SCAN_BASE_PACKAGE = "io.graphenee.core";
	public static final String ENTITY_SCAN_BASE_PACKAGE = "io.graphenee.core.model.entity";
	public static final String JPA_REPOSITORIES_BASE_PACKAGE = "io.graphenee.core.model.jpa.repository";

	@Bean
	public FlywayMigrationInitializer flywayInitializer(DataSource dataSource, Flyway flyway) {
		flyway.setCallbacks(new BaseFlywayCallback() {

			@Override
			public void beforeMigrate(Connection connection) {
				Flyway flyway = new Flyway();
				flyway.setDataSource(dataSource);
				String dbVendor = DataSourceUtil.determineDbVendor(dataSource);
				flyway.setLocations("classpath:db/graphenee/migration/" + dbVendor);
				flyway.setTable("graphenee_schema_version");
				flyway.setBaselineOnMigrate(true);
				flyway.setBaselineVersionAsString("0");
				flyway.migrate();
				super.beforeMigrate(connection);
			}
		});
		return new FlywayMigrationInitializer(flyway);
	}

}
