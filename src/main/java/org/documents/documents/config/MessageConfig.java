package org.documents.documents.config;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.MessageSettings;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class MessageConfig {

    private final MessageSettings messageSettings;

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue transformRequestedQueue() {
        return new Queue(messageSettings.getTransformRequestedQueueName());
    }

    @Bean
    public Queue transformSucceededQueue() {
        return new Queue(messageSettings.getTransformSucceededQueueName());
    }

    @Bean
    public Queue transformFailedQueue() {
        return new Queue(messageSettings.getTransformFailedQueueName());
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(messageSettings.getExchangeName());
    }

    @Bean
    public Binding transformRequestedBinding(TopicExchange exchange, Queue transformRequestedQueue) {
        return BindingBuilder.bind(transformRequestedQueue).to(exchange).with(messageSettings.getTransformRequestedRoutingKey());
    }

    @Bean
    public Binding transformSucceededBinding(TopicExchange exchange, Queue transformSucceededQueue) {
        return BindingBuilder.bind(transformSucceededQueue).to(exchange).with(messageSettings.getTransformSucceededRoutingKey());
    }

    @Bean
    public Binding transformFailedBinding(TopicExchange exchange, Queue transformFailedQueue) {
        return BindingBuilder.bind(transformFailedQueue).to(exchange).with(messageSettings.getTransformFailedRoutingKey());
    }
}
