package org.documents.documents.model.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.UUID;

public record Rendition(
        @Schema(description = "A unique ID for the rendition")
        UUID uuid,
        @Schema(description = "The mime type of the rendition")
        String mimeType,
        @Schema(description = "When the rendition was created")
        ZonedDateTime created
) {}