package com.demo.fulfillment.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.hibernate.cfg.Environment;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.demo.fulfillment.repositories")
public class GeneralDataConfig {
    // Default to false for environment deployments to not auto-migrate
    // set to true for local test suite
    @Value("${spring.flyway.enabled:false}")
    public Boolean FLYWAY_ENABLED;

    @Value("${flyway.createSchemas:true}")
    public Boolean CREATE_SCHEMAS_FLAG;

    @Value("${flyway.defaultSchema:public}")
    public String DEFAULT_SCHEMA;

    // comma-separated list of names, can be asked to separate schemas.
    // Not Recommended to use multiple schemas without a specific business need
    @Value("${flyway.schemas:public}")
    public String SCHEMAS_NEEDED;

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.put(Environment.GENERATE_STATISTICS, true);
        hibernateProperties.setProperty(Environment.HBM2DDL_AUTO, "validate");
        hibernateProperties.setProperty(Environment.DIALECT, org.hibernate.dialect.PostgreSQL10Dialect.class.getCanonicalName());
        hibernateProperties.setProperty(Environment.SHOW_SQL, "true");
        hibernateProperties.setProperty(Environment.FORMAT_SQL, "true");

        return hibernateProperties;
    }

    @Bean
    @DependsOn("dataSource")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.demo.fulfillment.models");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(org.hibernate.dialect.PostgreSQL10Dialect.class.getCanonicalName());
        vendorAdapter.setGenerateDdl(true);
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        entityManagerFactory.setJpaProperties(additionalProperties());

        return entityManagerFactory;
    }


    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean(initMethod = "migrate")
    @DependsOn({"dataSource"})
    Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema(DEFAULT_SCHEMA)
                .createSchemas(CREATE_SCHEMAS_FLAG)
                .schemas(SCHEMAS_NEEDED)
                .baselineOnMigrate(true)
                .target(MigrationVersion.LATEST)
                .load();

        return flyway;
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public GroupedOpenApi actuatorApi() {
        return GroupedOpenApi.builder()
                .group("actuator")
                .pathsToMatch("/actuator/**")
                .build();
    }

    @Bean
    public GroupedOpenApi fulfillmentApi() {
        return GroupedOpenApi.builder()
                .group("fulfillment")
                .packagesToScan("com.demo.fulfillment.controllers")
                .build();
    }
}