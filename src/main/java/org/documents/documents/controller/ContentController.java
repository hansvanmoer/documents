package org.documents.documents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.documents.documents.model.api.ApiConstants;
import org.documents.documents.model.api.Content;
import org.documents.documents.service.ContentService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class ContentController {

    private final ContentService contentService;

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

}
