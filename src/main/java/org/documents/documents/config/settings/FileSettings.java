package org.documents.documents.config.settings;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
public class FileSettings {
    private Path path;
    private int readBufferSize;
}
