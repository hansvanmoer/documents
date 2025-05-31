package org.documents.documents.config;

import lombok.AllArgsConstructor;
import org.documents.documents.message.MessageConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class MessageConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(MessageConstants.FANOUT_EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange transformExchange() {
        return new TopicExchange(MessageConstants.TRANSFORM_EXCHANGE_NAME);
    }

    @Bean
    public Binding transformExhangeBinding(FanoutExchange fanoutExchange, TopicExchange transformExchange) {
        return BindingBuilder.bind(transformExchange).to(fanoutExchange);
    }

    @Bean
    public Queue transformRequestedQueue() {
        return new Queue(MessageConstants.TRANSFORM_REQUESTED_QUEUE_NAME);
    }

    @Bean
    public Binding transformRequestedBinding(TopicExchange transformExchange, Queue transformRequestedQueue) {
        return BindingBuilder.bind(transformRequestedQueue).to(transformExchange).with(MessageConstants.TRANSFORM_REQUESTED_ROUTING_KEY);
    }

    @Bean
    public Queue transformSucceededQueue() {
        return new Queue(MessageConstants.TRANSFORM_SUCCEEDED_QUEUE_NAME);
    }

    @Bean
    public Binding transformSucceededBinding(TopicExchange transformExchange, Queue transformSucceededQueue) {
        return BindingBuilder.bind(transformSucceededQueue).to(transformExchange).with(MessageConstants.TRANSFORM_SUCCEEDED_ROUTING_KEY);
    }

    @Bean
    public Queue transformFailedQueue() {
        return new Queue(MessageConstants.TRANSFORM_FAILED_QUEUE_NAME);
    }

    @Bean
    public Binding transformFailedBinding(TopicExchange transformExchange, Queue transformFailedQueue) {
        return BindingBuilder.bind(transformFailedQueue).to(transformExchange).with(MessageConstants.TRANSFORM_FAILED_ROUTING_KEY);
    }

    @Bean
    public TopicExchange eventExchange() {
        return new TopicExchange(MessageConstants.EVENT_EXCHANGE_NAME);
    }

    @Bean
    public Binding eventExhangeBinding(TopicExchange eventExchange, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(eventExchange).to(fanoutExchange);
    }

    @Bean
    public Queue contentCreatedQueue() {
        return new Queue(MessageConstants.CONTENT_CREATED_QUEUE_NAME);
    }

    @Bean
    public Binding contentCreatedBinding(TopicExchange eventExchange, Queue contentCreatedQueue) {
        return BindingBuilder.bind(contentCreatedQueue).to(eventExchange).with(MessageConstants.CONTENT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue renditionCreatedQueue() {
        return new Queue(MessageConstants.RENDITION_CREATED_QUEUE_NAME);
    }

    @Bean
    public Binding renditionCreatedBinding(TopicExchange eventExchange, Queue renditionCreatedQueue) {
        return BindingBuilder.bind(renditionCreatedQueue).to(eventExchange).with(MessageConstants.RENDITION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue renditionDeletedQueue() {
        return new Queue(MessageConstants.RENDITION_DELETED_QUEUE_NAME);
    }

    @Bean
    public Binding renditionDeletedBinding(TopicExchange eventExchange, Queue renditionDeletedQueue) {
        return BindingBuilder.bind(renditionDeletedQueue).to(eventExchange).with(MessageConstants.RENDITION_DELETED_ROUTING_KEY);
    }

    @Bean
    public Queue documentCreatedQueue() {
        return new Queue(MessageConstants.DOCUMENT_CREATED_QUEUE_NAME);
    }

    @Bean
    public Binding documentCreatedBinding(TopicExchange eventExchange, Queue documentCreatedQueue) {
        return BindingBuilder.bind(documentCreatedQueue).to(eventExchange).with(MessageConstants.DOCUMENT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue documentUpdatedQueue() {
        return new Queue(MessageConstants.DOCUMENT_UPDATED_QUEUE_NAME);
    }

    @Bean
    public Binding documentUpdatedBinding(TopicExchange eventExchange, Queue documentUpdatedQueue) {
        return BindingBuilder.bind(documentUpdatedQueue).to(eventExchange).with(MessageConstants.DOCUMENT_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Queue documentDeletedQueue() {
        return new Queue(MessageConstants.DOCUMENT_DELETED_QUEUE_NAME);
    }

    @Bean
    public Binding documentDeletedBinding(TopicExchange eventExchange, Queue documentDeletedQueue) {
        return BindingBuilder.bind(documentDeletedQueue).to(eventExchange).with(MessageConstants.DOCUMENT_DELETED_ROUTING_KEY);
    }

    @Bean
    public TopicExchange previewExchange() {
        return new TopicExchange(MessageConstants.PREVIEW_EXCHANGE_NAME);
    }

    @Bean
    public Binding previewExchangeBinding(TopicExchange previewExchange, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(previewExchange).to(fanoutExchange);
    }

    @Bean
    public Queue previewQueue() {
        return new Queue(MessageConstants.PREVIEW_QUEUE_NAME);
    }

    @Bean
    public Binding previewBinding(TopicExchange previewExchange, Queue previewQueue) {
        return BindingBuilder.bind(previewQueue).to(previewExchange).with(MessageConstants.DOCUMENT_CREATED_ROUTING_KEY);
    }
}
