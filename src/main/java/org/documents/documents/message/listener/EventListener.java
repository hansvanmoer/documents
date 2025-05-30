package org.documents.documents.message.listener;

import lombok.extern.slf4j.Slf4j;
import org.documents.documents.message.payload.*;
import org.documents.documents.model.EventConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventListener {
    @RabbitListener(queues = EventConstants.CONTENT_CREATED_QUEUE_NAME)
    public void onContentCreatedEvent(ContentCreatedEvent contentCreatedEvent) {
        log.debug("Received content created event: {}", contentCreatedEvent);
    }

    @RabbitListener(queues = EventConstants.RENDITION_CREATED_QUEUE_NAME)
    public void onRenditionCreatedEvent(RenditionCreatedEvent renditionCreatedEvent) {
        log.debug("Received rendition created event: {}", renditionCreatedEvent);
    }

    @RabbitListener(queues = EventConstants.RENDITION_DELETED_QUEUE_NAME)
    public void onRenditionDeletedEvent(RenditionDeletedEvent renditionDeletedEvent) {
        log.debug("Received rendition deleted event: {}", renditionDeletedEvent);
    }

    @RabbitListener(queues = EventConstants.DOCUMENT_CREATED_QUEUE_NAME)
    public void onDocumentCreatedEvent(DocumentCreatedEvent documentCreatedEvent) {
        log.debug("Received document created event: {}", documentCreatedEvent);
    }

    @RabbitListener(queues = EventConstants.DOCUMENT_UPDATED_QUEUE_NAME)
    public void onDocumentUpdatedEvent(DocumentUpdatedEvent documentUpdatedEvent) {
        log.debug("Received document update event: {}", documentUpdatedEvent);
    }

    @RabbitListener(queues = EventConstants.DOCUMENT_DELETED_QUEUE_NAME)
    public void onDocumentDeletedEvent(DocumentDeletedEvent documentDeletedEvent) {
        log.debug("Received document deleted event: {}", documentDeletedEvent);
    }
}
