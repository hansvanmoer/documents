package org.documents.documents.model.api;

import java.time.ZonedDateTime;
import java.util.UUID;

public record Document(UUID uuid, ZonedDateTime created, String mimeType, String title) {
}