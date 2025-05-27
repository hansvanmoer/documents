package org.documents.documents.file;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

public interface FileContent {

    Flux<DataBuffer> read();

}
