package org.documents.documents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.documents.documents.model.api.ApiConstants;
import org.documents.documents.model.api.CreateRenditionRequest;
import org.documents.documents.model.api.CreateRenditionResponse;
import org.documents.documents.model.api.Rendition;
import org.documents.documents.service.RenditionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class RenditionController {

    private final RenditionService renditionService;

    @GetMapping(ApiConstants.RENDITIONS_PATH)
    @Operation(
            description = "Lists all renditions",
            summary = "Lists all renditions, optionally filtered by mime type"
    )
    public Mono<Page<Rendition>> list(
            @Parameter(description = "Page size")
            @RequestParam(name = "page-size", required = false, defaultValue = "#{apiSettings.defaultPageSize}")
            Integer pageSize,
            @Parameter(description = "Page index")
            @RequestParam(required = false, defaultValue = "0")
            Integer page,
            @Parameter(description = "Mime type")
            @RequestParam(name = ApiConstants.MIME_TYPE_PARAMETER, required = false)
            String mimeType
    ) {
        final Pageable pageable = PageRequest.of(page, pageSize);
        if(mimeType == null) {
            return renditionService.list(pageable);
        } else {
            return renditionService.listByMimeType(mimeType, pageable);
        }
    }

    @GetMapping(ApiConstants.DOCUMENT_BY_UUID_RENDITIONS_PATH)
    @Operation(
            description = "Lists all renditions for a given document",
            summary = "Lists all renditions for a given document by UUID"
    )
    public Flux<Rendition> listByDocumentUuid(
            @Parameter(description = "The document UUID")
            @PathVariable(ApiConstants.DOCUMENT_UUID_PATH_VARIABLE)
            UUID documentUuid
    ) {
        return renditionService.listByDocumentUuid(documentUuid);
    }

    @GetMapping(ApiConstants.RENDITION_BY_UUID_PATH)
    @Operation(
            description = "Fetches a rendition",
            summary = "Fetches a rendition by UUID"
    )
    public Mono<Rendition> findByUuid(
            @Parameter(description = "The rendition UUID")
            @PathVariable(ApiConstants.RENDITION_UUID_PATH_VARIABLE)
            UUID renditionUuid
    ) {
        return renditionService.get(renditionUuid);
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
        return renditionService.getOrRequest(documentUuid, createRenditionRequest.mimeType())
                .map(CreateRenditionResponse::new)
                .switchIfEmpty(Mono.just(new CreateRenditionResponse(null)));
    }

    @DeleteMapping(ApiConstants.RENDITION_BY_UUID_PATH)
    @Operation(
            description = "Deletes a rendition",
            summary = "Deletes a rendition by UUID"
    )
    public Mono<Void> delete(
            @Parameter(description = "The rendition UUID")
            @PathVariable(ApiConstants.RENDITION_UUID_PATH_VARIABLE)
            UUID renditionUuid
    ) {
        return renditionService.delete(renditionUuid);
    }

    @Operation(
            description = "Downloads the rendition",
            summary = "Downloads the rendition by its UUID"
    )
    @GetMapping(ApiConstants.RENDITION_BY_UUID_DOWNLOAD_PATH)
    public Mono<Void> download(
            @Parameter(description = "The rendition UUID")
            @PathVariable(ApiConstants.RENDITION_UUID_PATH_VARIABLE)
            UUID documentUuid,
            ServerHttpResponse response
    ) {
        return renditionService.download(response, documentUuid);
    }

}
