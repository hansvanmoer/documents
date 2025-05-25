package org.documents.documents.message.payload;

import lombok.Data;

@Data
public class RequestTransformMessage {
    private String path;
    private String sourceMimeType;
    private String targetMimeType;
}
