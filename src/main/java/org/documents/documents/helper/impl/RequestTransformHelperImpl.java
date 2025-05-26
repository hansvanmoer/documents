package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.MessageSettings;
import org.documents.documents.file.FileReference;
import org.documents.documents.helper.RequestTransformHelper;
import org.documents.documents.message.payload.RequestTransformMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class RequestTransformHelperImpl implements RequestTransformHelper {

    private final RabbitTemplate rabbitTemplate;
    private final MessageSettings messageSettings;

    @Override
    public void requestTransform(UUID contentUuid, FileReference fileReference, String sourceMimeType, String targetMimeType) {
        final RequestTransformMessage requestTransformMessage = new RequestTransformMessage();
        requestTransformMessage.setContentUuid(contentUuid);
        requestTransformMessage.setFileReference(fileReference);
        requestTransformMessage.setFileReference(fileReference);
        requestTransformMessage.setSourceMimeType(sourceMimeType);
        requestTransformMessage.setTargetMimeType(targetMimeType);
        rabbitTemplate.convertAndSend(messageSettings.getExchangeName(), messageSettings.getTransformRequestedRoutingKey(), requestTransformMessage);
    }
}
