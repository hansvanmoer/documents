package org.documents.documents.model.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.UUID;

@Schema(description = "A document created in the store")
public record Document(
        @Schema(description = "The document's unique ID")
        UUID uuid,
        @Schema(description = "The timestamp on which the document was created")
        ZonedDateTime created,
        @Schema(description = "The timestamp on which the document was modified")
        ZonedDateTime modified,
        @Schema(description = "The timestamp on which the document content was modified")
        ZonedDateTime contentModified,
        @Schema(description = "The content associated with the document")
        UUID contentUuid,
        @Schema(description = "The mime type of the document")
        String mimeType,
        @Schema(description = "The document title")
        String title
) {}