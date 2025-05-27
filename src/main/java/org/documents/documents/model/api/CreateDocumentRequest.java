package org.documents.documents.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(
        name = "The request to create the document",
        description = "Contains all the information needed to create a document"
)
public record CreateDocumentRequest(
        @NotBlank
        @Schema(description = "The title of the document")
        String title,
        @NotNull
        @Schema(description = "The UUID of the content to be associated with the document")
        UUID contentUuid
) {}
