package com.demo.fulfillment.workers;

import com.demo.fulfillment.config.queues.RabbitMqConfig;
import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.services.OrderService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderWorker {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    OrderService orderService;

    @RabbitListener(queues = RabbitMqConfig.ORDERS_QUEUE_NAME)
    public void handleOrders(@Payload Order in,
                             Message message,
                             Channel channel,
                             @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                             @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key) throws IOException {
        logger.info("Message: {}, Channel: {}", message.toString(), channel.toString());
        logger.info("Key: {}, msg: {}",key,in.toString());

        try {
            orderService.save(in);
            channel.basicAck(tag, false);
        } catch (IOException e) {
            logger.error("unable to save order, sending to dlq", e);
            channel.basicReject(tag,false);
        }
    }

    /**
     * Use Enqueue when allowing normal proccessing of Status Requests for 60 second delay
     * */
    public void enqueueJobRun(Order in) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ORDER_ROUTING_KEY, in);
    }
}
