package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.helper.RequestTransformHelper;
import org.documents.documents.message.MessageConstants;
import org.documents.documents.message.payload.RequestTransformMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class RequestTransformHelperImpl implements RequestTransformHelper {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void requestTransform(UUID contentUuid, String targetMimeType) {
        final RequestTransformMessage requestTransformMessage = new RequestTransformMessage();
        requestTransformMessage.setContentUuid(contentUuid);
        requestTransformMessage.setTargetMimeType(targetMimeType);
        rabbitTemplate.convertAndSend(MessageConstants.FANOUT_EXCHANGE_NAME, MessageConstants.TRANSFORM_REQUESTED_ROUTING_KEY, requestTransformMessage);
    }
}
