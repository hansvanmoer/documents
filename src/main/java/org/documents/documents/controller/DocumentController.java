package org.documents.documents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.documents.documents.helper.ControllerHelper;
import org.documents.documents.model.api.ApiConstants;
import org.documents.documents.model.api.CreateDocumentRequest;
import org.documents.documents.model.api.Document;
import org.documents.documents.model.api.SearchDocumentRequest;
import org.documents.documents.service.DocumentService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
    public Flux<Document> list(
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
}
