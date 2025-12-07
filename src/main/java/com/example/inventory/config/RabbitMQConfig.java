package com.example.inventory.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    // Queue, Exchange, Routing Key isimleri
    public static final String STOCK_QUEUE = "stock.update.queue";
    public static final String STOCK_EXCHANGE = "stock.update.exchange";
    public static final String STOCK_ROUTING_KEY = "stock.update.key";

    @Bean
    public Queue stockUpdateQueue() {
        return new Queue(STOCK_QUEUE, true); // durable: true → restart sonrası da kaybolmaz
    }

    @Bean
    public TopicExchange stockUpdateExchange() {
        return new TopicExchange(STOCK_EXCHANGE);
    }

    @Bean
    public Binding stockBinding() {
        return BindingBuilder.bind(stockUpdateQueue())
                .to(stockUpdateExchange())
                .with(STOCK_ROUTING_KEY);
    }
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    
}
