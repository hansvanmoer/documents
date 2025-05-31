package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.helper.EventHelper;
import org.documents.documents.message.payload.*;
import org.documents.documents.message.MessageConstants;
import org.documents.documents.model.api.Content;
import org.documents.documents.model.api.Document;
import org.documents.documents.model.api.Rendition;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@AllArgsConstructor
@Component
public class EventHelperImpl implements EventHelper {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public Mono<Void> notifyContentCreated(Content content) {
        return notify(MessageConstants.CONTENT_CREATED_ROUTING_KEY, new ContentCreatedEvent(content));
    }

    @Override
    public Mono<Void> notifyRenditionCreated(Rendition rendition) {
        return notify(MessageConstants.RENDITION_CREATED_ROUTING_KEY, new RenditionCreatedEvent(rendition));
    }

    @Override
    public Mono<Void> notifyRenditionDeleted(UUID renditionUuid) {
        return notify(MessageConstants.RENDITION_DELETED_ROUTING_KEY, new RenditionDeletedEvent(renditionUuid));
    }

    @Override
    public Mono<Void> notifyDocumentCreated(Document document) {
        return notify(MessageConstants.DOCUMENT_CREATED_ROUTING_KEY, new DocumentCreatedEvent(document));
    }

    @Override
    public Mono<Void> notifyDocumentUpdated(Document document) {
        return notify(MessageConstants.DOCUMENT_UPDATED_ROUTING_KEY, new DocumentUpdatedEvent(document));
    }

    @Override
    public Mono<Void> notifyDocumentDeleted(UUID documentUuid) {
        return notify(MessageConstants.DOCUMENT_DELETED_ROUTING_KEY, new DocumentDeletedEvent(documentUuid));
    }

    private Mono<Void> notify(String routingKey, Object payload) {
        return Mono.fromRunnable( () -> rabbitTemplate.convertAndSend(MessageConstants.FANOUT_EXCHANGE_NAME, routingKey, payload))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
