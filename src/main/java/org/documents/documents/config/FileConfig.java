package org.documents.documents.config;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.file.FileStore;
import org.documents.documents.file.FileStoreType;
import org.documents.documents.file.TransformFileStore;
import org.documents.documents.file.impl.LocalFileStore;
import org.documents.documents.file.impl.TransformFileStoreImpl;
import org.documents.documents.helper.FileHelper;
import org.documents.documents.helper.MimeTypeHelper;
import org.documents.documents.helper.UuidHelper;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class FileConfig {

    private static final String SENTINEL_FILE_NAME = ".documents-filestore";
    private static final String TRANSFORM_SENTINEL_VALUE = "TRANSFORM";

    @Bean
    public DataBufferFactory bufferFactory(ByteBufAllocator byteBufAllocator) {
        return new NettyDataBufferFactory(byteBufAllocator);
    }

    @Bean
    public ByteBufAllocator byteBufAllocator() {
        return new PooledByteBufAllocator();
    }

    @Bean
    public FileStore contentFileStore(DataBufferFactory bufferFactory, FileSettings fileSettings, UuidHelper uuidHelper) {
        return createFileStore(
                fileSettings.getContentPath(),
                FileStoreType.CONTENT,
                bufferFactory,
                fileSettings,
                uuidHelper
        );
    }

    @Bean
    public FileStore renditionFileStore(DataBufferFactory bufferFactory, FileSettings fileSettings, UuidHelper uuidHelper) {
        return createFileStore(
                fileSettings.getRenditionPath(),
                FileStoreType.RENDITION,
                bufferFactory,
                fileSettings,
                uuidHelper
        );
    }

    @Bean
    public TransformFileStore transformFileStore(FileHelper fileHelper, FileSettings fileSettings, MimeTypeHelper mimeTypeHelper, UuidHelper uuidHelper) {
        final Path path = fileSettings.getTransformPath();
        createOrCheckDescriptorFile(path, TRANSFORM_SENTINEL_VALUE);
        return new TransformFileStoreImpl(
                path,
                fileHelper,
                mimeTypeHelper,
                uuidHelper
        );
    }

    private void createOrCheckDescriptorFile(Path path, String sentinelValue) {
        if(!Files.isDirectory(path)) {
            throw new BeanCreationException(String.format("file store %s does not point to an existing folder %s", sentinelValue, path));
        }
        // test writing and file creation
        try {
            final Path descriptorPath = path.resolve(SENTINEL_FILE_NAME);
            if(Files.isRegularFile(descriptorPath)) {
                final String value = Files.readString(descriptorPath, StandardCharsets.UTF_8);
                try {
                    if(!sentinelValue.equals(value)) {
                        throw new BeanCreationException(String.format("file store %s at folder %s is of the wrong type %s", sentinelValue, path, value));
                    }
                } catch(IllegalArgumentException e) {
                    throw new BeanCreationException(String.format("file store %s at folder %s is of unknown type %s", sentinelValue, path, value));
                }
            } else {
                Files.createFile(descriptorPath);
                Files.writeString(descriptorPath, sentinelValue);
            }
        } catch(IOException e) {
            throw new BeanCreationException(String.format("can't create descriptor file at file store %s at folder %s", sentinelValue, path));
        }
    }

    private FileStore createFileStore(Path path, FileStoreType fileStoreType, DataBufferFactory bufferFactory, FileSettings fileSettings, UuidHelper uuidHelper) {
        createOrCheckDescriptorFile(path, fileStoreType.toString());
        return new LocalFileStore(
                path,
                bufferFactory,
                fileSettings,
                uuidHelper
        );
    }
}
