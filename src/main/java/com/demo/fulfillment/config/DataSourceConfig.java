package com.demo.fulfillment.config;

import com.rabbitmq.client.impl.MicrometerMetricsCollector;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("production")
@Configuration
public class DataSourceConfig {
    @Value("${JDBC_DATABASE_URL}")
    public String JDBC_DATABASE_URL;

    @Value("${logging.level.root:}")
    public String LOG_SQL;

    @Value("${RABBITMQ_AMQP_URL}")
    protected String RABBITMQ_AMQP_URL;

    @Autowired
    MeterRegistry meterRegistry;

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

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        // Example RABBITMQ_AMQP_URI in BMS
        // String connUri = "amqp://{user_name}:{password}@{host}:{port}/{vhost}";
        connectionFactory.setUri(RABBITMQ_AMQP_URL);
        connectionFactory.setConnectionTimeout(5);

        MicrometerMetricsCollector metricsCollector = new MicrometerMetricsCollector(meterRegistry, "rabbitmq.client");
        connectionFactory.getRabbitConnectionFactory().setMetricsCollector(metricsCollector);

        return connectionFactory;
    }
}
