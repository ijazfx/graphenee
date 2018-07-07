package io.graphenee.jbpm.embedded;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import io.graphenee.core.GrapheneeProperties;
import io.graphenee.core.util.DataSourceUtil;

@Configuration
@ConfigurationProperties
@ConditionalOnClass({ GrapheneeProperties.class })
@ComponentScan("io.graphenee.jbpm.embedded")
@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", matchIfMissing = false)
@AutoConfigureAfter(io.graphenee.core.GrapheneeCoreConfiguration.class)
public class GrapheneeJbpmConfiguration {

	@Bean("jbpmTransactionManager")
	public JpaTransactionManager jbpmTransactionManager(@Qualifier("jbpmEntityManagerFactory") EntityManagerFactory jbpmEntityManagerFactory) {
		JpaTransactionManager tm = new JpaTransactionManager(jbpmEntityManagerFactory);
		return tm;
	}

	@Bean("jbpmEntityManagerFactory")
	public EntityManagerFactory jbpmEntityManagerFactory() {
		DataSource ds = DataSourceUtil.createXaDataSource("dbJbpm", "jdbc:h2:/Users/fijaz/jbpm/jbpm.db", "sa", null, 5);
		// DataSource ds =
		// DataSourceBuilder.create().url("jdbc:h2:~/jbpm/jbpmdb").username("sa").password(null).driverClassName("org.h2.Driver").build();
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(ds);
		em.setPersistenceXmlLocation("META-INF/persistence-jbpm.xml");
		em.afterPropertiesSet();
		return em.getObject();

		// DataSource jbpmDataSource =
		// DataSourceUtil.createXaDataSource("dbJbpm", "jdbc:h2:~/jbpm/jbpmdb",
		// "sa", null, 5);
		// LocalContainerEntityManagerFactoryBean em = new
		// LocalContainerEntityManagerFactoryBean();
		// em.setDataSource(jbpmDataSource);
		// em.setPackagesToScan("org.jbpm", "org.drools");
		// em.setPersistenceUnitName("org.jbpm.persistence.jpa");
		// em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		// em.setJpaDialect(new HibernateJpaDialect());
		// em.setMappingResources("META-INF/JBPMorm.xml",
		// "META-INF/Taskorm.xml", "META-INF/TaskAuditorm.xml");
		// em.getJpaPropertyMap().put("hibernate.max_fetch_depth", "3");
		// em.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "update");
		// em.getJpaPropertyMap().put("hibernate.show_sql", "false");
		// em.getJpaPropertyMap().put("hibernate.dialect",
		// "org.hibernate.dialect.H2Dialect");
		// em.getJpaPropertyMap().put("hibernate.id.new_generator_mappings",
		// "false");
		// // em.getJpaPropertyMap().put("hibernate.transaction.jta.platform",
		// //
		// "org.hibernate.service.jta.platform.internal.BitronixJtaPlatform");
		// em.afterPropertiesSet();
		// return em.getObject();
	}

}
