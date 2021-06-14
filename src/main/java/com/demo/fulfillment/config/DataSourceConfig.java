package com.demo.fulfillment.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("production")
@Configuration
public class DataSourceConfig {
    @Autowired
    FulfillmentEnvProperties envProperties;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(envProperties.databaseConnectionString);
        config.setUsername(envProperties.databaseUser);
        config.setPassword(envProperties.databasePassword);
        config.setConnectionTimeout(20000);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);
        config.setIdleTimeout(300000);
        config.setDriverClassName(org.postgresql.Driver.class.getCanonicalName());

        return new HikariDataSource(config);
    }
}
