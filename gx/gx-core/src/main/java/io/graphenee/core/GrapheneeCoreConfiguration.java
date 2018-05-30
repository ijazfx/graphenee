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

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnClass({ GrapheneeProperties.class, DataSource.class })
@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", matchIfMissing = false)
@EnableJpaRepositories(entityManagerFactoryRef = "gxemf", transactionManagerRef = "gxtm")
@ComponentScan("io.graphenee.core")
public class GrapheneeCoreConfiguration {

	private DataSource _grapheneeDataSource;

	@Autowired
	public GrapheneeCoreConfiguration(GrapheneeProperties grapheneeProperties) {
		if (grapheneeProperties.isFlywayMigrationEnabled()) {
			Flyway flyway = new Flyway();
			flyway.setDataSource(grapheneeDataSource(grapheneeProperties));
			String dbVendor = grapheneeProperties.getDbVendor();
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
	public EntityManagerFactory entityManagerFactory(GrapheneeProperties grapheneeProperties) {
		LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
		lemfb.setDataSource(grapheneeDataSource(grapheneeProperties));
		lemfb.setPersistenceUnitName("persistence.graphenee");
		lemfb.setPackagesToScan(GrapheneeCoreConfiguration.class.getPackage().getName());
		lemfb.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		lemfb.setJpaDialect(new HibernateJpaDialect());
		lemfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		lemfb.afterPropertiesSet();
		return lemfb.getObject();
	}

	@Bean(name = "gxtm")
	public PlatformTransactionManager transactionManager(GrapheneeProperties grapheneeProperties) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory(grapheneeProperties));
		return transactionManager;
	}

	private DataSource grapheneeDataSource(GrapheneeProperties grapheneeProperties) {
		if (_grapheneeDataSource == null) {
			synchronized (GrapheneeCoreConfiguration.class) {
				if (_grapheneeDataSource == null) {
					_grapheneeDataSource = DataSourceBuilder.create().url(grapheneeProperties.getDbUrl()).username(grapheneeProperties.getDbUsername())
							.password(grapheneeProperties.getDbPassword()).driverClassName(grapheneeProperties.getDbDriverClassName()).build();
				}
			}
		}
		return _grapheneeDataSource;
	}

}
