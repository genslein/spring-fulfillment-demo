package com.demo.fulfillment.config;

import com.rabbitmq.client.impl.MicrometerMetricsCollector;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.io.IOException;

@Profile("test")
@Configuration
public class TestDataSource {

    @Value("${spring.flyway.enabled:true}")
    public Boolean FLYWAY_ENABLED;

    @Value("${flyway.createSchemas:true}")
    public Boolean CREATE_SCHEMAS_FLAG;

    @Value("${flyway.defaultSchema:public}")
    public String DEFAULT_SCHEMA;

    // comma-separated list of names, can be asked to separate schemas.
    // Not Recommended to use multiple schemas without a specific business need
    @Value("${flyway.schemas:public}")
    public String SCHEMAS_NEEDED;

    @Autowired
    MeterRegistry meterRegistry;
    @Bean
    PostgreSQLContainer postgreSQLContainer() {
        final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("postgres")
                .withUsername("bart")
                .withPassword("51mp50n");
        container.start();

        return container;
    }

    @Bean
    public DataSource dataSource(final PostgreSQLContainer container) {
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

    @Bean
    public RabbitMQContainer rabbitMQContainer() throws IOException, InterruptedException {
        final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management")
                .withUser("rabbit", "rabbit");
        // TODO: enable plugins
        //  .execInContainer("rabbitmq-plugins enable rabbitmq_management rabbitmq_tracing rabbitmq_top")
        rabbitMQContainer.start();
        return rabbitMQContainer;
    }

    @Bean
    ConnectionFactory connectionFactory(RabbitMQContainer rabbitMQContainer) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUri(rabbitMQContainer.getAmqpUrl());
        connectionFactory.setConnectionTimeout(5);

        MicrometerMetricsCollector metricsCollector = new MicrometerMetricsCollector(meterRegistry, "rabbitmq.client");
        connectionFactory.getRabbitConnectionFactory().setMetricsCollector(metricsCollector);

        return connectionFactory;
    }

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }
}