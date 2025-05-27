package org.documents.documents.model.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contains either a rendition or signifies that a rendition has been requested")
public record CreateRenditionResponse(
        @Schema(description = "A rendition if it was found")
        Rendition rendition
) {}
