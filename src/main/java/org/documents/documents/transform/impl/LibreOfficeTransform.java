package org.documents.documents.transform.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.documents.documents.config.settings.TransformSettings;
import org.documents.documents.file.TransformFileStore;
import org.documents.documents.transform.Transform;
import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.model.transform.TransformType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Component
@Slf4j
public class LibreOfficeTransform implements Transform {

    private static final MediaType WORD_2007_MEDIA_TYPE = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    private static final List<String> ARGS = Arrays.asList("--headless", "--convert-to");
    private static final Map<String, String> OUTPUT_FORMATS = Collections.unmodifiableMap(new HashMap<>() {{
        put(MediaType.TEXT_PLAIN_VALUE, "txt:Text");
        put(MediaType.APPLICATION_PDF_VALUE, "pdf:writer_pdf_Export");
    }});

    private final TransformFileStore transformFileStore;
    private final TransformSettings transformSettings;

    @Override
    public TransformResult transform(UUID uuid, String sourceMimeType, String targetMimeType) {
        final List<String> command = new ArrayList<>();
        command.add(transformSettings.getLibreOfficeExecutable());
        command.addAll(ARGS);
        final String outputFormat = OUTPUT_FORMATS.get(targetMimeType);
        if(outputFormat == null) {
            return new TransformResult(false, String.format("Target mime type %s is not supported", targetMimeType));
        }
        command.add(outputFormat);
        command.add(transformFileStore.getFileName(uuid, sourceMimeType));
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(command.toArray(new String[0]));
            processBuilder.directory(transformFileStore.getWorkingDirectory().toFile());
            final Process process = processBuilder.start();
            process.waitFor(transformSettings.getTimeoutInMilliseconds(), TimeUnit.MILLISECONDS);
            if(process.exitValue() == 0) {
                return new TransformResult(true, null);
            } else {
                try(Reader reader = new InputStreamReader(process.getErrorStream())) {
                    return new TransformResult(false, IOUtils.toString(reader));
                }
            }
        } catch(IOException | InterruptedException e) {
            log.error("error while converting", e);
            return new TransformResult(false, e.getMessage());
        }
    }

    @Override
    public Set<TransformType> getSupportedTransformTypes() {
        return Set.of(
                new TransformType(WORD_2007_MEDIA_TYPE.toString(), MediaType.TEXT_PLAIN.toString())
        );
    }
}
