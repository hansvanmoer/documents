package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.helper.ContentHelper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@AllArgsConstructor
@Component
public class ContentHelperImpl implements ContentHelper {

    private final DataBufferFactory dataBufferFactory;
    private final FileSettings fileSettings;

    @Override
    public Mono<String> readContentAsString(ContentEntity entity) {
        if(entity.getMimeType().equals(MediaType.TEXT_PLAIN_VALUE)) {
            final Charset charset = Optional.ofNullable(MediaType.parseMediaType(entity.getMimeType()).getCharset())
                    .orElse(StandardCharsets.UTF_8);
            return DataBufferUtils.join(readContent(entity))
                    .map(DataBuffer::readableByteBuffers)
                    .map(iter -> decodeString(iter, charset));
        } else {
            return Mono.empty();
        }
    }

    private String decodeString(DataBuffer.ByteBufferIterator iterator, Charset charset) {
        final StringBuilder result = new StringBuilder();
        while(iterator.hasNext()) {
            result.append(charset.decode(iterator.next()));
        }
        return result.toString();
    }

    private Flux<DataBuffer> readContent(ContentEntity entity) {
        return DataBufferUtils.read(fileSettings.getPath().resolve(entity.getPath()), dataBufferFactory,fileSettings.getReadBufferSize());
    }
}
