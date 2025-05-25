package org.documents.documents.config.settings;

import lombok.Getter;
import lombok.Setter;

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
