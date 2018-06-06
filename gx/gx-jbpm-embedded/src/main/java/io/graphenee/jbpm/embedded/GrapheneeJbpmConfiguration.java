package io.graphenee.jbpm.embedded;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import io.graphenee.core.GrapheneeProperties;

@Configuration
@ConfigurationProperties
@ConditionalOnClass({ GrapheneeProperties.class })
@EnableJpaRepositories(entityManagerFactoryRef = "gxjbpmemf", transactionManagerRef = "gxjbpmtm")
@ComponentScan("io.graphenee.jbpm.embedded")
@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", matchIfMissing = false)
@AutoConfigureAfter(io.graphenee.core.GrapheneeCoreConfiguration.class)
public class GrapheneeJbpmConfiguration {

	@Bean(name = "gxjbpmtm")
	@Qualifier("gxjbpmtm")
	public JpaTransactionManager getJbpmTransactionManager() {
		JpaTransactionManager jtManager = new JpaTransactionManager(getJbpmEntityManagerFactory());
		return jtManager;
	}

	@Bean(name = "gxjbpmemf")
	@Qualifier("gxjbpmemf")
	public EntityManagerFactory getJbpmEntityManagerFactory() {
		DataSource ds = DataSourceBuilder.create().url("jdbc:h2:~/jbpm/jbpmdb").username("sa").password(null).driverClassName("org.h2.Driver").build();
		LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
		lemfb.setDataSource(ds);
		lemfb.setPackagesToScan("org.jbpm", "org.drools");
		lemfb.setPersistenceUnitName("org.jbpm.persistence.jpa");
		lemfb.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		lemfb.setJpaDialect(new HibernateJpaDialect());
		lemfb.setMappingResources("META-INF/JBPMorm.xml", "META-INF/Taskorm.xml", "META-INF/TaskAuditorm.xml");
		lemfb.getJpaPropertyMap().put("hibernate.max_fetch_depth", "3");
		lemfb.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "update");
		lemfb.getJpaPropertyMap().put("hibernate.show_sql", "true");
		lemfb.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		lemfb.getJpaPropertyMap().put("hibernate.id.new_generator_mappings", "false");
		lemfb.getJpaPropertyMap().put("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.BitronixJtaPlatform");
		lemfb.afterPropertiesSet();
		return lemfb.getObject();
	}

}
