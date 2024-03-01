package com.demo.fulfillment.config;

import io.lettuce.core.resource.ClientResources;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.observability.MicrometerTracingAdapter;

@Profile("production")
@Configuration
public class RedisDataConfig {

    @Value("${REDIS_URL}")
    protected String REDIS_URL;

    @Bean
    RedisConnectionFactory redisConnectionFactory(ClientResources clientResources) {
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .clientResources(clientResources)
                .build();

        return new LettuceConnectionFactory(
                redisClusterConfiguration(),
                lettuceClientConfiguration
        );
    }

    @Bean
    public ClientResources clientResources(ObservationRegistry observationRegistry) {
        return ClientResources.builder()
                .tracing(new MicrometerTracingAdapter(observationRegistry, "fulfillment-redis"))
                .build();
    }
    private RedisClusterConfiguration redisClusterConfiguration() {
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
        RedisNode node = RedisClusterNode.fromString("my_redis_host:port");
        clusterConfiguration.addClusterNode(node);

        return clusterConfiguration;
    }
}
