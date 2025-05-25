package org.documents.documents.config.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
@ConfigurationProperties(prefix = "documents.temporal")
@Data
public class TemporalSettings {
    private ZoneId zoneId;
}
