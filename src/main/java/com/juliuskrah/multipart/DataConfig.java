/*
* Copyright 2016, Julius Krah
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/package com.juliuskrah.multipart;

import java.util.Properties;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.hikaricp.internal.HikariConfigurationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@EnableTransactionManagement
public class DataConfig {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Inject
	private Environment env;

	private Properties properties() {
		Properties props = new Properties();
		props.setProperty("hibernate.connection.provider_class", env.getRequiredProperty("hibernate.connection.provider_class"));
		props.setProperty("hibernate.hikari.dataSourceClassName", env.getRequiredProperty("hibernate.hikari.dataSourceClassName"));
		props.setProperty("hibernate.hikari.dataSource.url", env.getRequiredProperty("hibernate.hikari.dataSource.url"));
		props.setProperty("hibernate.hikari.dataSource.user", env.getRequiredProperty("hibernate.hikari.dataSource.user"));
		props.setProperty("hibernate.hikari.dataSource.password", env.getRequiredProperty("hibernate.hikari.dataSource.password"));
		props.setProperty("hibernate.hikari.maximumPoolSize", env.getRequiredProperty("hibernate.hikari.maximumPoolSize"));
		props.setProperty("hibernate.hikari.idleTimeout", env.getRequiredProperty("hibernate.hikari.idleTimeout"));
		props.setProperty("hibernate.hikari.minimumIdle", env.getRequiredProperty("hibernate.hikari.minimumIdle"));

		return props;
	}

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		log.debug("Starting datasource driver...");
		HikariConfig config = HikariConfigurationUtil.loadConfiguration(properties());

		return new HikariDataSource(config);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
		log.debug("Starting EntityManager...");
		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setPackagesToScan("com.juliuskrah.multipart.entity");
		entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManager.setDataSource(dataSource);
		entityManager.setJpaProperties(properties());
		entityManager.setPersistenceUnitName("julius");

		return entityManager;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		log.debug("Starting TransactionManager...");

		return new JpaTransactionManager(emf);
	}

	@Bean
	public SpringLiquibase liquibase(DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:db/master.yaml");
		liquibase.setContexts(env.getRequiredProperty("liquibase.contexts"));
		// liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
		liquibase.setDropFirst(env.getRequiredProperty("liquabase.is-drop-first", Boolean.class));
		liquibase.setShouldRun(env.getRequiredProperty("liquabase.is-enabled", Boolean.class));
		log.debug("Configuring Liquibase");

		return liquibase;
	}

}
