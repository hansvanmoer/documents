package org.documents.documents.config;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.file.FileStore;
import org.documents.documents.file.impl.LocalFileStore;
import org.documents.documents.helper.UuidHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;

@Configuration
public class FileConfig {

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
        return new LocalFileStore(
                fileSettings.getContentPath(),
                bufferFactory,
                fileSettings,
                uuidHelper
        );
    }

    @Bean
    public FileStore renditionFileStore(DataBufferFactory bufferFactory, FileSettings fileSettings, UuidHelper uuidHelper) {
        return new LocalFileStore(
                fileSettings.getRenditionPath(),
                bufferFactory,
                fileSettings,
                uuidHelper
        );
    }
}
