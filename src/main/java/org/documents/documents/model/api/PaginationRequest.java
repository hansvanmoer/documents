package org.documents.documents.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record PaginationRequest (
        @Min(1)
        @Schema(description = "The page size")
        int pageSize,
        @Min(0)
        @Schema(description = "The index of the page to display")
        int page
){}
