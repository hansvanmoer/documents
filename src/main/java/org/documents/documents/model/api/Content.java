package org.documents.documents.model.api;

import java.time.ZonedDateTime;
import java.util.UUID;

public record Content(UUID uuid, ZonedDateTime created, String mimeType){}
