package com.content_load_sb.config;


import com.content_load_sb.helper.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;


/**
 * Created by ramazancesur on 12/4/17.
 */

@Configuration
@EnableJpaRepositories(basePackages = "com.content_load_sb.persist",
        entityManagerFactoryRef = "protEntityManager",
        transactionManagerRef = "protTransactionManager")
public class HibernateH2Config {

    @Autowired
    private Environment environment;

    @Bean("dataSourceH2")
    @Primary
    public DriverManagerDataSource dataSourceH2() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.h2.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.h2.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.h2.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.h2.password"));
        return dataSource;
    }


    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean protEntityManager() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSourceH2());
        factoryBean.setPackagesToScan("com.content_load_sb.persist");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJpaProperties(getHibernateProperties());
        return factoryBean;
    }

    private Properties getHibernateProperties() {
        Properties properties = Helper.getInstance().getRestrictProp("application.properties", "hibernate");
        properties.put("hibernate.dialect", environment.getRequiredProperty("spring.datasource.h2.dialect"));
        return properties;
    }


    @Bean
    @Primary
    public PlatformTransactionManager protTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(protEntityManager().getObject());
        DatabasePopulatorUtils.execute(databasePopulatorh2(), dataSourceH2());
        return transactionManager;
    }


    private DatabasePopulator databasePopulatorh2() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(false);
        return databasePopulator;
    }

    @Bean
    @Primary
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
