package com.demo.fulfillment.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
//import org.flywaydb.core.Flyway;
//import org.flywaydb.core.api.MigrationVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("production")
@Configuration
public class DataSourceConfig {
    @Value("${JDBC_DATABASE_URL}")
    public String JDBC_DATABASE_URL;

    // Default to false for environment deployments to not auto-migrate
    // set to true for local test suite
    @Value("${spring.flyway.enabled:false}")
    public Boolean FLYWAY_ENABLED;

    @Value("${flyway.createSchemas:true}")
    public Boolean CREATE_SCHEMAS_FLAG;

    @Value("${flyway.defaultSchema:public}")
    public String DEFAULT_SCHEMA;

    @Value("${logging.level.root:}")
    public String LOG_SQL;

    // comma-separated list of names, can be asked to separate schemas.
    // Not Recommended to use multiple schemas without a specific business need
    @Value("${flyway.schemas:public}")
    public String SCHEMAS_NEEDED;

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

//    @Bean(initMethod = "migrate")
//    @DependsOn({"dataSource"})
//    Flyway flyway(DataSource dataSource) {
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .defaultSchema(DEFAULT_SCHEMA)
//                .createSchemas(CREATE_SCHEMAS_FLAG)
//                .schemas(SCHEMAS_NEEDED)
//                .baselineOnMigrate(true)
//                .target(MigrationVersion.LATEST)
//                .load();
//
//        return flyway;
//    }
}
