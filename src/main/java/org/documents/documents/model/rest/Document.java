package org.documents.documents.model.rest;

import java.time.ZonedDateTime;
import java.util.UUID;

public record Document(UUID uuid, ZonedDateTime created, String mimeType, String title) {
}