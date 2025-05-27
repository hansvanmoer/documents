package org.documents.documents.config.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "documents.transform")
@Data
public class TransformSettings {
    private String libreOfficeExecutable;
    private long timeoutInMilliseconds;
}
