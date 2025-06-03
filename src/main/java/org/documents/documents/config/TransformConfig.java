package org.documents.documents.config;

import org.documents.documents.transform.TransformRegistry;
import org.documents.documents.transform.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.Arrays;

@Configuration
public class TransformConfig {

    @Bean
    public TransformRegistry transformRegistry(
            LibreOfficeTransform libreOfficeTransform,
            PdfBoxRendererTransform pdfBoxRendererTransform,
            PdfBoxTextStripperTransform pdfBoxTextStripperTransform
    ) {
        return new TransformRegistryImpl(
                Arrays.asList(
                        libreOfficeTransform,
                        pdfBoxRendererTransform,
                        pdfBoxTextStripperTransform,
                        new CompositeTransform(libreOfficeTransform, MediaType.APPLICATION_PDF_VALUE, pdfBoxRendererTransform),
                        new CompositeTransform(libreOfficeTransform, MediaType.APPLICATION_PDF_VALUE, pdfBoxTextStripperTransform)
                )
        );
    }
}