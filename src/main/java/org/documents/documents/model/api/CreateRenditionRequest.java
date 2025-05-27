package org.documents.documents.model.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A request for a rendition")
public record CreateRenditionRequest (
        @Schema(description = "The mime type of the rendition to request")
        String mimeType
){}
