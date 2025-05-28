package org.documents.documents.transform.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.documents.documents.file.TransformFileStore;
import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.model.transform.TransformType;
import org.documents.documents.transform.Transform;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@AllArgsConstructor
@Component
@Slf4j
public class PdfBoxRendererTransform implements Transform {

    private static final Map<String, String> OUTPUT_FORMATS = Map.of(MediaType.IMAGE_PNG_VALUE, "png");

    private final TransformFileStore transformFileStore;

    @Override
    public TransformResult transform(UUID uuid, String sourceMimeType, String targetMimeType) {
        final String outputFormat = OUTPUT_FORMATS.get(targetMimeType);
        if(outputFormat == null) {
            return new TransformResult("unsupported output format: " + targetMimeType);
        }
        try(PDDocument document = Loader.loadPDF(transformFileStore.getFilePath(uuid, sourceMimeType).toFile())){
            final PDFRenderer pdfRenderer = new PDFRenderer(document);
            final BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            final Path destinationPath = transformFileStore.getFilePath(uuid, targetMimeType);
            try {
                ImageIO.write(image, outputFormat, destinationPath.toFile());
                return new TransformResult(Collections.singleton(targetMimeType));
            } catch(IOException e) {
                log.error("unable to write image", e);
                return new TransformResult("unable to write image: " + e.getMessage());
            }
        } catch(IOException e) {
            log.error("unable to load pdf file", e);
            return new TransformResult("unable to load pdf file: " + e.getMessage());
        }
    }

    @Override
    public Set<TransformType> getSupportedTransformTypes() {
        return Set.of(new TransformType(MediaType.APPLICATION_PDF_VALUE, MediaType.IMAGE_PNG_VALUE));
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
