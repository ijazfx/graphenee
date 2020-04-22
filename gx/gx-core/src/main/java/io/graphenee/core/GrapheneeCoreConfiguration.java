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

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.graphenee.core.util.DataSourceUtil;

@Configuration
@ConditionalOnClass(DataSource.class)
//@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", matchIfMissing = false)
@EnableJpaRepositories({ GrapheneeCoreConfiguration.JPA_REPOSITORIES_BASE_PACKAGE })
@EntityScan({ GrapheneeCoreConfiguration.ENTITY_SCAN_BASE_PACKAGE })
@ComponentScan(GrapheneeCoreConfiguration.COMPONENT_SCAN_BASE_PACKAGE)
public class GrapheneeCoreConfiguration {

	public static final String COMPONENT_SCAN_BASE_PACKAGE = "io.graphenee.core";
	public static final String ENTITY_SCAN_BASE_PACKAGE = "io.graphenee.core.model.entity";
	public static final String JPA_REPOSITORIES_BASE_PACKAGE = "io.graphenee.core.model.jpa.repository";

	public GrapheneeCoreConfiguration(DataSource dataSource) {
		String dbVendor = DataSourceUtil.determineDbVendor(dataSource);
		Flyway fw = Flyway.configure().dataSource(dataSource).locations("classpath:db/graphenee/migration/" + dbVendor).table("graphenee_schema_version").baselineOnMigrate(true)
				.baselineVersion("0").load();
		fw.migrate();
	}

}
