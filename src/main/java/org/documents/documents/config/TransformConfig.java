package org.documents.documents.config;

import org.documents.documents.transform.TransformRegistry;
import org.documents.documents.transform.impl.LibreOfficeTransform;
import org.documents.documents.transform.impl.TransformRegistryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class TransformConfig {

    @Bean
    public TransformRegistry transformRegistry(
            LibreOfficeTransform word2007ToPlainTextTransform
    ) {
        return new TransformRegistryImpl(Collections.singletonList(word2007ToPlainTextTransform));
    }
}
