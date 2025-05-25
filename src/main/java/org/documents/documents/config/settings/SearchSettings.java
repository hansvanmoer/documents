package org.documents.documents.config.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "documents.search")
@Data
public class SearchSettings {
    private long contentIndexDelayInMs;
    private long contentIndexPeriodInMs;
}
