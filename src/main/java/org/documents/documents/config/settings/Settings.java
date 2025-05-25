package org.documents.documents.config.settings;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "documents")
@Setter
public class Settings {
    private FileSettings file;
    private MessageSettings message;
    private TemporalSettings temporal;
    private TransformSettings transform;

    @Bean
    public FileSettings getFile() {
        return file;
    }

    @Bean
    public MessageSettings getMessage() {
        return message;
    }

    @Bean
    public TemporalSettings getTemporal() {
        return temporal;
    }

    @Bean
    public TransformSettings getTransform() {
        return transform;
    }
}
