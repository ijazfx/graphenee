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

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnClass({ GrapheneeProperties.class, DataSource.class })
@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", matchIfMissing = false)
@EnableJpaRepositories(entityManagerFactoryRef = "gxemf", transactionManagerRef = "gxtm")
// @EnableJpaRepositories(entityManagerFactoryRef = "gxemf")
@ComponentScan("com.graphenee.core")
public class GrapheneeCoreConfiguration {

	@Autowired
	public GrapheneeCoreConfiguration(GrapheneeProperties grapheneeProperties, DataSource dataSource) {
		if (grapheneeProperties.isFlywayMigrationEnabled()) {
			Flyway flyway = new Flyway();
			flyway.setDataSource(dataSource);
			String dbVendor = grapheneeProperties.getDBVendor();
			if (dbVendor == null)
				dbVendor = "postgresql";
			flyway.setLocations("classpath:db/graphenee/migration/" + dbVendor);
			flyway.setTable("graphenee_schema_version");
			flyway.setBaselineOnMigrate(true);
			flyway.setBaselineVersionAsString("0");
			flyway.migrate();
		}
	}

	@Bean(name = "gxemf")
	public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setPersistenceUnitName("persistence.graphenee");
		em.setPackagesToScan(GrapheneeCoreConfiguration.class.getPackage().getName());
		em.setDataSource(dataSource);
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.afterPropertiesSet();
		return em.getObject();
	}

	@Bean(name = "gxtm")
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory(dataSource));
		return transactionManager;
	}

}
