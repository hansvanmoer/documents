package org.documents.documents.handler;

import lombok.AllArgsConstructor;
import org.documents.documents.mapper.HandlerMapper;
import org.documents.documents.model.api.CreateDocumentRequest;
import org.documents.documents.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class DocumentHandler {
    private final DocumentService documentService;
    private final HandlerMapper handlerMapper;
    /**
     * Handles the upload requests
     *
     * @param request the server request containing the content
     * @return the response
     */
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(CreateDocumentRequest.class)
                .map(CreateDocumentRequest::contentUuid)
                .flatMap(documentService::create)
                .flatMap(document ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(document)
                );
    }

    public Mono<ServerResponse> list(ServerRequest request) {
        return documentService.list(handlerMapper.mapPageable(request))
                .collect(Collectors.toList())
                .flatMap(documents ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(documents)
                );
    }
}
