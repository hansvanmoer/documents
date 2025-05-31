package org.documents.documents.message.listener;

import lombok.AllArgsConstructor;
import org.documents.documents.helper.RunTransformHelper;
import org.documents.documents.message.payload.RequestTransformMessage;
import org.documents.documents.message.MessageConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TransformMessageListener {
    private final RunTransformHelper runTransformHelper;

    @RabbitListener(queues = MessageConstants.TRANSFORM_REQUESTED_QUEUE_NAME)
    public void transformRequested(RequestTransformMessage message) {
        runTransformHelper.runTransform(
                message.getContentUuid(),
                message.getTargetMimeType()
        );
    }
}
