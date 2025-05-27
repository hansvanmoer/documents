package org.documents.documents.model.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.UUID;

@Schema(description = "Content stored in the document store")
public record Content(
        @Schema(description = "The unique ID of this content")
        UUID uuid,
        @Schema(description = "When the content was created")
        ZonedDateTime created,
        @Schema(description = "The mime type of the content")
        String mimeType
){}
