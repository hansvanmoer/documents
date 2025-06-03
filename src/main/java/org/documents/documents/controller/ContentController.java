package org.documents.documents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.documents.documents.helper.ControllerHelper;
import org.documents.documents.model.api.ApiConstants;
import org.documents.documents.model.api.Content;
import org.documents.documents.model.exception.NotFoundException;
import org.documents.documents.service.ContentService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class ContentController {

    private final ContentService contentService;
    private final ControllerHelper controllerHelper;

    @Operation(
            description = "Uploads new content",
            summary = "Uploads content and returns a unique ID to use in further requests",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The content to upload",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = MediaType.ALL_VALUE
                    )
            )
    )
    @PutMapping(ApiConstants.CONTENT_PATH)
    public Mono<Content> upload(
            @Parameter(hidden = true)
            @RequestHeader(value = HttpHeaders.CONTENT_TYPE, defaultValue = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            MediaType contentType,
            @RequestBody Flux<DataBuffer> content
    ) {
        return contentService.upload(
                contentType,
                content
        );
    }

    @Operation(
            description = "Lists all content",
            summary = "List content"
    )
    @GetMapping(ApiConstants.CONTENT_PATH)
    public Mono<Page<Content>> list(
            @Parameter(description = "Page size")
            @RequestParam(name = "page-size", required = false, defaultValue = "#{apiSettings.defaultPageSize}")
            Integer pageSize,
            @Parameter(description = "Page index")
            @RequestParam(required = false, defaultValue = "0")
            Integer page
    ) {
        return contentService.list(controllerHelper.getPageable(pageSize, page));
    }

    @Operation(
            description = "Gets a piece of content by UUID",
            summary = "Gets a single piece of content by its UUID"
    )
    @GetMapping(ApiConstants.CONTENT_BY_UUID_PATH)
    public Mono<Content> get(
            @Parameter(description = "The document UUID")
            @PathVariable(ApiConstants.CONTENT_UUID_PATH_VARIABLE)
            UUID contentUuid
    ) {
        return contentService.get(contentUuid).switchIfEmpty(Mono.error(new NotFoundException(Content.class, contentUuid)));
    }

    @Operation(
            description = "Downloads the content",
            summary = "Downloads the content by its UUID"
    )
    @GetMapping(ApiConstants.CONTENT_BY_UUID_DOWNLOAD_PATH)
    public Mono<Void> download(
            @Parameter(description = "The content UUID")
            @PathVariable(ApiConstants.CONTENT_UUID_PATH_VARIABLE)
            UUID documentUuid,
            ServerHttpResponse response
    ) {
        return contentService.download(response, documentUuid);
    }
}
