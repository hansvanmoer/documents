package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.file.FileStoreRegistry;
import org.documents.documents.file.TypedFileReference;
import org.documents.documents.helper.DownloadHelper;
import org.documents.documents.helper.FileHelper;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class DownloadHelperImpl implements DownloadHelper {

    private final FileHelper fileHelper;
    private final FileStoreRegistry fileStoreRegistry;

    public Mono<Void> download(ServerHttpResponse response, String name, TypedFileReference fileReference) {
        final String fileName = fileHelper.createFileName(name, fileReference.getMimeType());
        response.getHeaders().add(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.builder(fileReference.getMimeType()).filename(fileName).build().toString()
        );
        return response.writeWith(fileStoreRegistry.createFileProxy(fileReference.getFileReference()).read());
    }

}
