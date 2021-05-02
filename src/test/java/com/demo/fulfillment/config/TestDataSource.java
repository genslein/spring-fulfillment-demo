package com.demo.fulfillment.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Profile("test")
@Configuration
public class TestDataSource {

    @Bean
    PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer("postgres:12")
                .withDatabaseName("postgres")
                .withUsername("bart")
                .withPassword("51mp50n");
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer container) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(container.getJdbcUrl());
        config.setUsername(container.getUsername());
        config.setPassword(container.getPassword());
        config.setConnectionTimeout(20000);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);
        config.setIdleTimeout(300000);
        config.setDriverClassName(org.postgresql.Driver.class.getCanonicalName());

        return new HikariDataSource(config);
    }
}
