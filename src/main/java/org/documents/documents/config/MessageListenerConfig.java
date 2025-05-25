package org.documents.documents.config;

import lombok.AllArgsConstructor;
import org.documents.documents.helper.RunTransformHelper;
import org.documents.documents.model.message.RequestTransformMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@AllArgsConstructor
@Configuration
public class MessageListenerConfig {

    private RunTransformHelper runTransformHelper;

    @RabbitListener(queues = "transform_requested")
    public void transformRequested(RequestTransformMessage message) {
        runTransformHelper.runTransform(Path.of(message.getPath()), message.getSourceMimeType(), message.getTargetMimeType());
    }
}
