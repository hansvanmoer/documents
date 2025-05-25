package org.documents.documents.handler;

import lombok.AllArgsConstructor;
import org.documents.documents.model.api.Content;
import org.documents.documents.service.ContentService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class ContentHandler {

    private final ContentService contentService;

    /**
     * Handles the upload requests
     *
     * @param request the server request containing the content
     * @return the response
     */
    public Mono<ServerResponse> upload(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        contentService.upload(
                                request.headers().contentType().orElse(MediaType.APPLICATION_OCTET_STREAM),
                                request.bodyToFlux(DataBuffer.class)
                        ),
                        Content.class
                );
    }
}
