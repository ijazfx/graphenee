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

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import io.graphenee.core.util.DataSourceUtil;

@Configuration
@ConditionalOnClass({ GrapheneeProperties.class, DataSource.class })
@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", matchIfMissing = false)
@EnableJpaRepositories(entityManagerFactoryRef = "grapheneeEntityManagerFactory", transactionManagerRef = "transactionManager")
@ComponentScan("io.graphenee.core")
public class GrapheneeCoreConfiguration {

	@Value("${spring.datasource.url}")
	String dbUrl;

	@Value("${spring.datasource.username}")
	String dbUsername;

	@Value("${spring.datasource.password}")
	String dbPassword;

	@Autowired(required = false)
	GrapheneeProperties grapheneeProperties;

	@Autowired
	@Qualifier("grapheneeDataSource")
	DataSource grapheneeDataSource;

	@PostConstruct
	public void init() {
		if (grapheneeProperties.isFlywayMigrationEnabled()) {
			Flyway flyway = new Flyway();
			flyway.setDataSource(grapheneeDataSource);
			flyway.setLocations("classpath:db/graphenee/migration/" + grapheneeProperties.driverName());
			flyway.setTable("graphenee_schema_version");
			flyway.setBaselineOnMigrate(true);
			flyway.setBaselineVersionAsString("0");
			flyway.migrate();
		}
	}

	@Bean("transactionManager")
	@ConditionalOnMissingBean(JtaTransactionManager.class)
	public JpaTransactionManager grapheneeTransactionManager(@Qualifier("grapheneeEntityManagerFactory") EntityManagerFactory grapheneeEntityManagerFactory) {
		JpaTransactionManager jpa = new JpaTransactionManager(grapheneeEntityManagerFactory);
		return jpa;
	}

	@Autowired
	@Bean("grapheneeEntityManagerFactory")
	@ConditionalOnBean(JtaTransactionManager.class)
	public EntityManagerFactory grapheneeJtaEntityManagerFactory(@Qualifier("grapheneeDataSource") DataSource grapheneeDataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setJtaDataSource(grapheneeDataSource);
		em.setPersistenceUnitName("persistence.graphenee");
		em.setPackagesToScan(GrapheneeCoreConfiguration.class.getPackage().getName());
		em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		em.setJpaDialect(new HibernateJpaDialect());
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.getJpaPropertyMap().put("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.BitronixJtaPlatform");
		em.afterPropertiesSet();
		return em.getObject();
	}

	@Autowired
	@Bean("grapheneeEntityManagerFactory")
	@ConditionalOnMissingBean(JtaTransactionManager.class)
	public EntityManagerFactory grapheneeEntityManagerFactory(@Qualifier("grapheneeDataSource") DataSource grapheneeDataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(grapheneeDataSource);
		em.setPersistenceUnitName("persistence.graphenee");
		em.setPackagesToScan(GrapheneeCoreConfiguration.class.getPackage().getName());
		em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		em.setJpaDialect(new HibernateJpaDialect());
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.afterPropertiesSet();
		return em.getObject();
	}

	@Bean("grapheneeDataSource")
	@ConditionalOnBean(JtaTransactionManager.class)
	public DataSource grapheneeXaDataSource() {
		GrapheneeProperties gp = grapheneeProperties();
		return DataSourceUtil.createXaDataSource("dsGraphenee", gp.getUrl(), gp.getUsername(), gp.getPassword(), 5);
	}

	@Bean("grapheneeDataSource")
	@ConditionalOnMissingBean(JtaTransactionManager.class)
	public DataSource grapheneeDataSource() {
		GrapheneeProperties gp = grapheneeProperties();
		return DataSourceUtil.createDataSource(gp.getUrl(), gp.getUsername(), gp.getPassword());
	}

	public GrapheneeProperties grapheneeProperties() {
		if (grapheneeProperties == null) {
			grapheneeProperties = new GrapheneeProperties().withUrl(dbUrl).withUsername(dbUsername).withPassword(dbPassword).withFlywayMigrationEnabled(true);
		}
		return grapheneeProperties;
	}

}
