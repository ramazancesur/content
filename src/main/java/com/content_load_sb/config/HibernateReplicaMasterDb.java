package com.content_load_sb.config;

import com.content_load_sb.helper.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@EnableJpaRepositories(basePackages = "com.content_load_sb.persistmysql",
        entityManagerFactoryRef = "contentEntityManager",
        transactionManagerRef = "contentTransactionManager")
@PropertySource(value = {"classpath:application.properties"})
public class HibernateReplicaMasterDb {
    @Autowired
    private Environment environment;

    @Bean(name = "dataSourceMySQL")
    public DriverManagerDataSource dataSourceMySQL() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.mysql.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.mysql.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.mysql.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.mysql.password"));
        return dataSource;
    }


    @Bean(name = "contentEntityManager")
    public LocalContainerEntityManagerFactoryBean contentEntityManager() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSourceMySQL());
        factoryBean.setPackagesToScan("com.content_load_sb.persistmysql");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJpaProperties(getHibernateProperties());
        return factoryBean;
    }

    private Properties getHibernateProperties() {
        Properties properties = Helper.getInstance().getRestrictProp("application.properties", "hibernate");
        properties.put("hibernate.dialect", environment.getRequiredProperty("spring.datasource.mysql.dialect"));
        return properties;
    }


    @Bean
    public PlatformTransactionManager contentTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(contentEntityManager().getObject());
        DatabasePopulatorUtils.execute(databasePopulator(), dataSourceMySQL());
        return transactionManager;
    }

    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(false);
        return databasePopulator;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
