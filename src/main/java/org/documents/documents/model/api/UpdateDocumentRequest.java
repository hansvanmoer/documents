package org.documents.documents.model.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(
        name = "The request to update a document",
        description = "Contains all the information needed to update a document"
)
public record UpdateDocumentRequest(
        @Schema(description = "The title of the document")
        String title,
        @Schema(description = "The UUID of the content to be associated with the document")
        UUID contentUuid
) {}
