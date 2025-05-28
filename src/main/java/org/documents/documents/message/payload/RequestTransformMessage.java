package org.documents.documents.message.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class RequestTransformMessage {
    private UUID contentUuid;
    private String targetMimeType;
}
