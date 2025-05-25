package org.documents.documents.model.message;

import lombok.Data;

import java.util.UUID;

@Data
public class TransformSucceededMessage {
    private String path;
    private String sourceMimeType;
    private String targetMimeType;
    private UUID transformedContentUuid;
}
