package com.kgisl.dbEngine;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "sourceEntityManagerFactory", transactionManagerRef = "sourceTransactionManager", basePackages = {
		"com.kgisl.dbEngine.src.dao" })
public class SourceDatasourceConfiguration {

	@Value("${spring.jpa.properties.hibernate.dialect}")
	private String hibernateDialect;

	@Primary
	@Bean(name = "sourceProperties")
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties dataSourceProperties() {

		return new DataSourceProperties();
	}

	@Primary
	@Bean(name="sourceDatasource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource datasource(@Qualifier("sourceProperties") DataSourceProperties properties){
	    return properties.initializeDataSourceBuilder().build();
	}

	@Primary
	@Bean(name = "sourceEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
			@Qualifier("sourceDatasource") DataSource dataSource) {

		 HashMap<String, Object> props = new HashMap<>();
		    props.put("hibernate.dialect", hibernateDialect);
		    
		return builder.dataSource(dataSource).properties(props).packages("com.kgisl.dbEngine.model.source").build();
	}

	@Primary
	@Bean(name = "sourceTransactionManager")
	@ConfigurationProperties("spring.jpa")
	public PlatformTransactionManager transactionManager(
			@Qualifier("sourceEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

		return new JpaTransactionManager(entityManagerFactory);
	}
}
