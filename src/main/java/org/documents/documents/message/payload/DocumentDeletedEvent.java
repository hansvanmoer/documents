package org.documents.documents.message.payload;

import java.util.UUID;

public record DocumentDeletedEvent(UUID uuid) {
}
