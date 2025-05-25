package org.documents.documents.model.message;

import lombok.Data;

@Data
public class TransformFailedMessage {
    private String path;
    private String sourceMimeType;
    private String targetMimeType;
    private TransformFailureType failureType;
    private String message;
}
