package org.documents.documents.model.message;

import lombok.Data;

@Data
public class RequestTransformMessage {
    private String path;
    private String sourceMimeType;
    private String targetMimeType;
}
