package org.documents.documents.message.payload;

import org.documents.documents.model.api.Document;

public record DocumentCreatedEvent(Document document) {
}
