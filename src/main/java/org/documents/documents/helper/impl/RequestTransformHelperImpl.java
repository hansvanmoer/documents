package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.MessageSettings;
import org.documents.documents.helper.RequestTransformHelper;
import org.documents.documents.model.message.RequestTransformMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@AllArgsConstructor
@Component
public class RequestTransformHelperImpl implements RequestTransformHelper {

    private final RabbitTemplate rabbitTemplate;
    private final MessageSettings messageSettings;

    @Override
    public void requestTransform(Path path, MediaType sourceType, MediaType targetType) {
        if(!sourceType.isCompatibleWith(targetType)) {
            final RequestTransformMessage requestTransformMessage = new RequestTransformMessage();
            requestTransformMessage.setPath(path.toString());
            requestTransformMessage.setSourceMimeType(sourceType.toString());
            requestTransformMessage.setTargetMimeType(targetType.toString());
            rabbitTemplate.convertAndSend(messageSettings.getExchangeName(), messageSettings.getTransformRequestedRoutingKey(), requestTransformMessage);
        }
    }
}
