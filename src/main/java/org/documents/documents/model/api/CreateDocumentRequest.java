package org.documents.documents.model.api;

import java.util.UUID;

public record CreateDocumentRequest(String title, UUID contentUuid) {
}
