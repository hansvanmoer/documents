package org.documents.documents.message.listener;

import lombok.AllArgsConstructor;
import org.documents.documents.helper.RunTransformHelper;
import org.documents.documents.message.payload.RequestTransformMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@AllArgsConstructor
@Component
public class TransformMessageListener {
    private final RunTransformHelper runTransformHelper;

    @RabbitListener(queues = "#{messageSettings.transformRequestedQueueName}")
    public void transformRequested(RequestTransformMessage message) {
        runTransformHelper.runTransform(
                message.getContentUuid(),
                message.getFileReference(),
                message.getSourceMimeType(),
                message.getTargetMimeType()
        );
    }
}
