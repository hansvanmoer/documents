package org.documents.documents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.documents.documents.model.api.ApiConstants;
import org.documents.documents.model.api.CreateRenditionRequest;
import org.documents.documents.model.api.CreateRenditionResponse;
import org.documents.documents.model.api.Rendition;
import org.documents.documents.service.RenditionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class RenditionController {

    private final RenditionService renditionService;

    @GetMapping(ApiConstants.DOCUMENT_BY_UUID_RENDITIONS_PATH)
    @Operation(
            description = "Searches documents",
            summary = "Searches documents"
    )
    public Flux<Rendition> getDocumentRenditions(
            @Parameter(description = "The document UUID")
            @PathVariable(ApiConstants.DOCUMENT_UUID_PATH_VARIABLE)
            UUID documentUuid
    ) {
        return renditionService.getDocumentRenditions(documentUuid);
    }

    @Operation(
            description = "Requests a rendition",
            summary = "Requests a rendition, which will either be present or asynchronously requested"
    )
    @PutMapping(ApiConstants.DOCUMENT_BY_UUID_RENDITIONS_PATH)
    public Mono<CreateRenditionResponse> create(
            @Parameter(description = "The document UUID")
            @PathVariable(ApiConstants.DOCUMENT_UUID_PATH_VARIABLE)
            UUID documentUuid,
            @RequestBody CreateRenditionRequest createRenditionRequest
    ){
        return renditionService.getOrRequestRendition(documentUuid, createRenditionRequest.mimeType())
                .map(CreateRenditionResponse::new)
                .switchIfEmpty(Mono.just(new CreateRenditionResponse(null)));
    }

}
