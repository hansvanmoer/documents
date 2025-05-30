package org.documents.documents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.documents.documents.helper.ControllerHelper;
import org.documents.documents.model.DocumentUpdate;
import org.documents.documents.model.api.*;
import org.documents.documents.service.DocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class DocumentController {

    private final ControllerHelper controllerHelper;
    private final DocumentService documentService;

    @Operation(
        description = "Creates a document",
        summary = "Creates a document with the supplied content and metadata"
    )
    @PutMapping(ApiConstants.DOCUMENTS_PATH)
    public Mono<Document> create(
            @RequestBody
            CreateDocumentRequest createDocumentRequest
    ) {
        return documentService.create(createDocumentRequest.title(), createDocumentRequest.contentUuid());
    }

    @Operation(
            description = "Lists all documents",
            summary = "List documents"
    )
    @GetMapping(ApiConstants.DOCUMENTS_PATH)
    public Mono<Page<Document>> list(
            @Parameter(description = "Page size")
            @RequestParam(name = "page-size", required = false, defaultValue = "#{apiSettings.defaultPageSize}")
            Integer pageSize,
            @Parameter(description = "Page index")
            @RequestParam(required = false, defaultValue = "0")
            Integer page
    ) {
        return documentService.list(controllerHelper.getPageable(pageSize, page));
    }

    @Operation(
            description = "Searches documents",
            summary = "Searches documents"
    )
    @PostMapping(ApiConstants.SEARCH_DOCUMENTS_PATH)
    public Flux<Document> search(@RequestBody SearchDocumentRequest searchDocumentRequest) {
        final Pageable pageable = Optional.ofNullable(searchDocumentRequest.pagination())
                .map(controllerHelper::getPageable).orElseGet(controllerHelper::getDefaultPageable);
        return documentService.search(pageable, searchDocumentRequest.term());
    }

    @Operation(
            description = "Gets a document by UUID",
            summary = "Gets a single document by its UUID"
    )
    @GetMapping(ApiConstants.DOCUMENT_BY_UUID_PATH)
    public Mono<Document> getByUuid(
            @Parameter(description = "The document UUID")
            @PathVariable(ApiConstants.DOCUMENT_UUID_PATH_VARIABLE)
            UUID documentUuid
    ) {
        return documentService.get(documentUuid);
    }

    @Operation(
            description = "Updates a document by UUID",
            summary = "Updates a single document by its UUID"
    )
    @PostMapping(ApiConstants.DOCUMENT_BY_UUID_PATH)
    public Mono<Document> updateByUuid(
            @Parameter(description = "The document UUID")
            @PathVariable(ApiConstants.DOCUMENT_UUID_PATH_VARIABLE)
            UUID documentUuid,
            @RequestBody
            UpdateDocumentRequest updateDocumentRequest

    ) {
        final DocumentUpdate.DocumentUpdateBuilder builder = DocumentUpdate.builder();
        if(updateDocumentRequest.title() != null) {
            builder.title(updateDocumentRequest.title());
        }
        if(updateDocumentRequest.contentUuid() != null) {
            builder.contentUuid(updateDocumentRequest.contentUuid());
        }
        return documentService.update(documentUuid, builder.build());
    }

    @Operation(
            description = "Deletes a document by UUID",
            summary = "Deletes a single document by its UUID"
    )
    @DeleteMapping(ApiConstants.DOCUMENT_BY_UUID_PATH)
    public Mono<Void> deleteByUuid(
            @Parameter(description = "The document UUID")
            @PathVariable(ApiConstants.DOCUMENT_UUID_PATH_VARIABLE)
            UUID documentUuid
    ) {
        return documentService.delete(documentUuid);
    }
}
