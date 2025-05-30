package org.documents.documents.config;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.MessageSettings;
import org.documents.documents.model.EventConstants;
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

    @Bean
    public Queue contentCreatedQueue() {
        return new Queue(EventConstants.CONTENT_CREATED_QUEUE_NAME);
    }

    @Bean
    public Binding contentCreatedBinding(TopicExchange exchange, Queue contentCreatedQueue) {
        return BindingBuilder.bind(contentCreatedQueue).to(exchange).with(EventConstants.CONTENT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue renditionCreatedQueue() {
        return new Queue(EventConstants.RENDITION_CREATED_QUEUE_NAME);
    }

    @Bean
    public Binding renditionCreatedBinding(TopicExchange exchange, Queue renditionCreatedQueue) {
        return BindingBuilder.bind(renditionCreatedQueue).to(exchange).with(EventConstants.RENDITION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue renditionDeletedQueue() {
        return new Queue(EventConstants.RENDITION_DELETED_QUEUE_NAME);
    }

    @Bean
    public Binding renditionDeletedBinding(TopicExchange exchange, Queue renditionDeletedQueue) {
        return BindingBuilder.bind(renditionDeletedQueue).to(exchange).with(EventConstants.RENDITION_DELETED_ROUTING_KEY);
    }

    @Bean
    public Queue documentCreatedQueue() {
        return new Queue(EventConstants.DOCUMENT_CREATED_QUEUE_NAME);
    }

    @Bean
    public Binding documentCreatedBinding(TopicExchange exchange, Queue documentCreatedQueue) {
        return BindingBuilder.bind(documentCreatedQueue).to(exchange).with(EventConstants.DOCUMENT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue documentUpdatedQueue() {
        return new Queue(EventConstants.DOCUMENT_UPDATED_QUEUE_NAME);
    }

    @Bean
    public Binding documentUpdatedBinding(TopicExchange exchange, Queue documentUpdatedQueue) {
        return BindingBuilder.bind(documentUpdatedQueue).to(exchange).with(EventConstants.DOCUMENT_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Queue documentDeletedQueue() {
        return new Queue(EventConstants.DOCUMENT_DELETED_QUEUE_NAME);
    }

    @Bean
    public Binding documentDeletedBinding(TopicExchange exchange, Queue documentDeletedQueue) {
        return BindingBuilder.bind(documentDeletedQueue).to(exchange).with(EventConstants.DOCUMENT_DELETED_ROUTING_KEY);
    }
}
