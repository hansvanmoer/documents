package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.file.FileContent;
import org.documents.documents.helper.FileHelper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.file.Path;

@AllArgsConstructor
@Component
public class FileHelperImpl implements FileHelper {

    private final DataBufferFactory dataBufferFactory;
    private final FileSettings fileSettings;

    @Override
    public Mono<String> readToString(FileContent fileContent, Charset charset) {
        return DataBufferUtils.join(fileContent.read())
                .map(DataBuffer::readableByteBuffers)
                .map(iter -> decodeString(iter, charset));
    }

    @Override
    public Flux<DataBuffer> readFromPath(Path path) {
        return DataBufferUtils.read(path, dataBufferFactory, fileSettings.getReadBufferSize());
    }

    private String decodeString(DataBuffer.ByteBufferIterator iterator, Charset charset) {
        final StringBuilder result = new StringBuilder();
        while(iterator.hasNext()) {
            result.append(charset.decode(iterator.next()));
        }
        return result.toString();
    }
}
