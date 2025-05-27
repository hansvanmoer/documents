package org.documents.documents.config;

import org.documents.documents.transform.TransformRegistry;
import org.documents.documents.transform.impl.CompositeTransform;
import org.documents.documents.transform.impl.LibreOfficeTransform;
import org.documents.documents.transform.impl.PdfBoxTransform;
import org.documents.documents.transform.impl.TransformRegistryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.Arrays;

@Configuration
public class TransformConfig {

    @Bean
    public TransformRegistry transformRegistry(
            LibreOfficeTransform libreOfficeTransform,
            PdfBoxTransform pdfBoxTransform
    ) {
        return new TransformRegistryImpl(
                Arrays.asList(
                        libreOfficeTransform,
                        pdfBoxTransform,
                        new CompositeTransform(libreOfficeTransform, MediaType.APPLICATION_PDF_VALUE, pdfBoxTransform)
                )
        );
    }
}