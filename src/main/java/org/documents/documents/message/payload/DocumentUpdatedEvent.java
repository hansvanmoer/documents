package org.documents.documents.message.payload;

import org.documents.documents.model.api.Document;

public record DocumentUpdatedEvent(Document document) {
}
