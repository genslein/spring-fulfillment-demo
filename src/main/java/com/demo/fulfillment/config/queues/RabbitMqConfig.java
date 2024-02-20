package com.demo.fulfillment.config.queues;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableRabbit
public class RabbitMqConfig {
    public static final Integer MESSAGE_DELAY = 60000; // 1 minute in Milliseconds
    public static final Integer SUPPLEMENT_MESSAGE_DELAY = 600000; // 1 minute in Milliseconds
    public static final String ORDERS_QUEUE_NAME = "queue_orders_processed";
    public static final String ORDERS_DEADLETTER_QUEUE_NAME = ORDERS_QUEUE_NAME + ".dlq";
    public static final String EXCHANGE_NAME = "fulfillment_jobs";
    public static final String DEADLETTER_EXCHANGE_NAME = EXCHANGE_NAME + ".dlx";
    public static final String ORDER_ROUTING_KEY = "order_job_run";

    @Bean
    Queue ordersQueue() {
        return QueueBuilder.durable(ORDERS_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DEADLETTER_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", ORDER_ROUTING_KEY)
                .withArgument("x-message-ttl", MESSAGE_DELAY)
                .build();
    }

    @Bean
    Queue ordersDeadLetterQueue() {
        return QueueBuilder.durable(ORDERS_DEADLETTER_QUEUE_NAME).build();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(DEADLETTER_EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding orderBinding() {
        return BindingBuilder.bind(ordersQueue()).to(exchange()).with(ORDER_ROUTING_KEY);
    }

    @Bean
    Binding orderDeadLetterBinding() {
        return BindingBuilder.bind(ordersDeadLetterQueue()).to(deadLetterExchange()).with(ORDER_ROUTING_KEY);
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(jsonConverter());

        return factory;
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }
}