package org.documents.documents.config.settings;

import lombok.Data;

import java.nio.file.Path;

@Data
public class TransformSettings {
    private Path path;
    private String libreOfficeExecutable;
    private long timeoutInMilliseconds;
}
