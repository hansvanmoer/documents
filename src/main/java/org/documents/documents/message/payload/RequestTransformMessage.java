package org.documents.documents.message.payload;

import lombok.Data;
import org.documents.documents.file.FileReference;

import java.util.UUID;

@Data
public class RequestTransformMessage {
    private UUID contentUuid;
    private FileReference fileReference;
    private String sourceMimeType;
    private String targetMimeType;
}
