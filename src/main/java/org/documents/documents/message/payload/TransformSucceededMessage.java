package org.documents.documents.message.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class TransformSucceededMessage {
    private String path;
    private String sourceMimeType;
    private String targetMimeType;
    private UUID transformedContentUuid;
}
