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

@Configuration
@EnableJpaRepositories(basePackages = "com.content_load_sb.persistmysqlslave",
        entityManagerFactoryRef = "contentEntityManagerSlaver",
        transactionManagerRef = "contentTransactionManagerSlaver")
@PropertySource(value = {"classpath:application.properties"})
public class HibernateReplicaSlaverDb {
    @Autowired
    private Environment environment;

    @Bean(name = "dataSourceReplicaMySQLSlaver")
    public DriverManagerDataSource dataSourceReplicaMySQLSlaver() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.mysql.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.mysql.slaver.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.mysql.slaver.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.mysql.slaver.password"));
        return dataSource;
    }


    @Bean(name = "contentEntityManagerSlaver")
    public LocalContainerEntityManagerFactoryBean contentEntityManagerSlaver() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSourceReplicaMySQLSlaver());
        factoryBean.setPackagesToScan("com.content_load_sb.persistmysqlslave");
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
    public PlatformTransactionManager contentTransactionManagerSlaver() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(contentEntityManagerSlaver().getObject());
        DatabasePopulatorUtils.execute(databasePopulator(), dataSourceReplicaMySQLSlaver());
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
