package org.cf.broker.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "db3EntityManagerFactory", basePackages = {
        "org.cf.broker.repository3" })
public class Db3Config {

    //@Primary
    @Bean(name = "db3DataSourceProperties")
    @ConfigurationProperties("db3.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    //@Primary
    @Bean(name = "db3DataSource")
    @ConfigurationProperties("db3.datasource.configuration")
    public DataSource dataSource(@Qualifier("db3DataSourceProperties") DataSourceProperties db3DataSourceProperties) {
        return db3DataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    //@Primary
    @Bean(name = "db3EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("db3DataSource") DataSource db3DataSource) {
        return builder
                .dataSource(db3DataSource)
                .packages("org.cf.broker.model3")
                .persistenceUnit("db3")
                .build();
    }

    //@Primary
    @Bean(name = "db3TransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("db3EntityManagerFactory") EntityManagerFactory db3EntityManagerFactory) {
        return new JpaTransactionManager(db3EntityManagerFactory);
    }

}
