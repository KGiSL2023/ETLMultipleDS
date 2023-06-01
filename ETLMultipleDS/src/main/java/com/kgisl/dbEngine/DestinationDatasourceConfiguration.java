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
@EnableJpaRepositories(
        entityManagerFactoryRef = "destEntityManagerFactory",
        transactionManagerRef = "destTransactionManager",
        basePackages = { "com.kgisl.dbEngine.dest.dao" })
public class DestinationDatasourceConfiguration {
	
	@Value("${spring.dest.jpa.properties.hibernate.dialect}")
	private String hibernateDialect;
	
	@Bean(name="destProperties")
    @ConfigurationProperties("spring.dest-datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name="destDatasource")
    @ConfigurationProperties(prefix = "spring.dest-datasource")
    public DataSource datasource(@Qualifier("destProperties") DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }
    
	
	@Bean(name="destEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
	        (EntityManagerFactoryBuilder builder, @Qualifier("destDatasource") DataSource dataSource){

		 HashMap<String, Object> props = new HashMap<>();
		    props.put("hibernate.dialect", hibernateDialect);
		    
	    return builder.dataSource(dataSource).properties(props)
	            .packages("com.kgisl.dbEngine.model.dest")
	            .build();
	}
	
	@Bean(name = "destTransactionManager")
	@ConfigurationProperties("spring.jpa")
	public PlatformTransactionManager transactionManager(
	        @Qualifier("destEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

	    return new JpaTransactionManager(entityManagerFactory);
	}

}
