package org.documents.documents.config.settings;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
@ConfigurationProperties(prefix = "documents.file")
@Getter
@Setter
public class FileSettings {
    private Path contentPath;
    private Path renditionPath;
    private int readBufferSize;
}
