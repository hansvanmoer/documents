package org.documents.documents.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "A request for a document search")
public record SearchDocumentRequest(
        @NotBlank
        @Schema(description = "The search term")
        String term,
        @Schema(description = "The pagination")
        PaginationRequest pagination
) {}
