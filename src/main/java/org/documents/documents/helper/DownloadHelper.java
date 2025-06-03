package org.documents.documents.helper;

import org.documents.documents.file.TypedFileReference;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

public interface DownloadHelper {
    Mono<Void> download(ServerHttpResponse response, String name, TypedFileReference fileReference);
}
