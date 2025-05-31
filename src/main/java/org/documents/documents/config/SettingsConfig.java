package org.documents.documents.config;

import org.documents.documents.config.settings.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        FileSettings.class,
        TemporalSettings.class,
        TransformSettings.class
})
public class SettingsConfig {
}
