package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.file.FileContent;
import org.documents.documents.helper.FileHelper;
import org.documents.documents.helper.MimeTypeHelper;
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

    private static final String INVALID_FILE_CHARACTERS = "[<>:\"/\\\\|?*]";
    private static final String INVALID_FILE_CHARACTER_REPLACEMENT = "_";

    private final DataBufferFactory dataBufferFactory;
    private final FileSettings fileSettings;
    private final MimeTypeHelper mimeTypeHelper;

    @Override
    public String createFileName(String fileName, String mimeType) {
        return sanitizeFileName(fileName) + mimeTypeHelper.getExtension(mimeType).orElse("");
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll(INVALID_FILE_CHARACTERS, INVALID_FILE_CHARACTER_REPLACEMENT);
    }

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
