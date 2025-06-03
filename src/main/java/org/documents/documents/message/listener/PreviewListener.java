package org.documents.documents.message.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.helper.RequestTransformHelper;
import org.documents.documents.message.payload.DocumentCreatedEvent;
import org.documents.documents.message.MessageConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@AllArgsConstructor
@Configuration
@Slf4j
public class PreviewListener {

    private final RequestTransformHelper requestTransformHelper;

    @RabbitListener(queues = MessageConstants.PREVIEW_QUEUE_NAME)
    public void onDocumentCreatedEvent(DocumentCreatedEvent documentCreatedEvent) {
        try {
            log.debug("Preview requested: {}", documentCreatedEvent.document().uuid());
            requestTransformHelper.requestTransform(documentCreatedEvent.document().contentUuid(), MediaType.IMAGE_PNG_VALUE);
        } catch(Exception ex) {
            log.error("could not request preview", ex);
        }
    }
}
