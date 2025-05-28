package org.documents.documents.transform.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.documents.documents.file.TransformFileStore;
import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.model.transform.TransformType;
import org.documents.documents.transform.Transform;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class PdfBoxTextStripperTransform implements Transform {

    private final TransformFileStore transformFileStore;

    @Override
    public TransformResult transform(UUID uuid, String sourceMimeType, String targetMimeType) {
        final Path sourcePath = transformFileStore.getFilePath(uuid, sourceMimeType);
        final Path targetPath = transformFileStore.getFilePath(uuid, targetMimeType);
        try(PDDocument document = Loader.loadPDF(sourcePath.toFile())){
            final PDFTextStripper pdfStripper = new PDFTextStripper();
            final String text = pdfStripper.getText(document);
            try {
                Files.createDirectories(targetPath.getParent());
                Files.createFile(targetPath);
                Files.writeString(targetPath, text, StandardCharsets.UTF_8);
                return new TransformResult(Collections.singleton(targetMimeType));
            } catch(IOException e){
                log.error("unable to write text", e);
                return new TransformResult("unable to write text: " + e.getMessage());
            }
        } catch(IOException e) {
            log.error("unable to load pdf file", e);
            return new TransformResult("unable to load pdf file: " + e.getMessage());
        }
    }

    @Override
    public Set<TransformType> getSupportedTransformTypes() {
        return Collections.singleton(new TransformType(MediaType.APPLICATION_PDF_VALUE, MediaType.TEXT_PLAIN_VALUE));
    }

    @Override
    public List<String> getPreferredSourceMimeTypes(String targetMimeType) {
        return Collections.singletonList(MediaType.APPLICATION_PDF_VALUE);
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
