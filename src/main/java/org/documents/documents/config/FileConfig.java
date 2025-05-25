package org.documents.documents.config;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
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
}
