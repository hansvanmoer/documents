package org.documents.documents.config.settings;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "documents.message")
@Getter
@Setter
public class MessageSettings {
    private String exchangeName;
    private String transformRequestedQueueName;
    private String transformRequestedRoutingKey;
    private String transformSucceededQueueName;
    private String transformSucceededRoutingKey;
    private String transformFailedQueueName;
    private String transformFailedRoutingKey;
}
