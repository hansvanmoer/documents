package org.documents.documents.helper;

import org.documents.documents.file.FileContent;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.file.Path;

public interface FileHelper {

    String createFileName(String fileName, String mimeType);

    Mono<String> readToString(FileContent file, Charset charset);

    Flux<DataBuffer> readFromPath(Path path);

}
