package com.demo.fulfillment.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("production")
@Configuration
public class DataSourceConfig {
    @Value("${JDBC_DATABASE_URL}")
    public String JDBC_DATABASE_URL;

    @Value("${logging.level.root:}")
    public String LOG_SQL;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_DATABASE_URL);
        config.setConnectionTimeout(20000);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);
        config.setIdleTimeout(300000);
        config.setDriverClassName(org.postgresql.Driver.class.getCanonicalName());

        return new HikariDataSource(config);
    }
}
