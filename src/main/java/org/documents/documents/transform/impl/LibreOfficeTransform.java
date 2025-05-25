package org.documents.documents.transform.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.documents.documents.config.settings.TransformSettings;
import org.documents.documents.transform.Transform;
import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.model.transform.TransformType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Component
@Slf4j
public class LibreOfficeTransform implements Transform {

    private static final MediaType WORD_2007_MEDIA_TYPE = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    private static final List<String> ARGS = Arrays.asList("--headless", "--convert-to","txt:Text");

    private final TransformSettings transformSettings;

    @Override
    public TransformResult transform(Path sourcePath, String sourceMimeType, String targetMimeType, String targetExtension) {
        final List<String> command = new ArrayList<>();
        command.add(transformSettings.getLibreOfficeExecutable());
        command.addAll(ARGS);
        command.add(sourcePath.getFileName().toString());
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(command.toArray(new String[0]));
            processBuilder.directory(sourcePath.getParent().toFile());
            final Process process = processBuilder.start();
            process.waitFor(transformSettings.getTimeoutInMilliseconds(), TimeUnit.MILLISECONDS);
            if(process.exitValue() == 0) {
                return new TransformResult(replaceExtension(sourcePath, targetExtension));
            } else {
                try(Reader reader = new InputStreamReader(process.getErrorStream())) {
                    return new TransformResult(IOUtils.toString(reader));
                }
            }
        } catch(IOException | InterruptedException e) {
            log.error("error while converting", e);
            return new TransformResult(e.getMessage());
        }
    }

    private Path replaceExtension(Path sourcePath, String targetExtension) {
        final String sourceExtension = FilenameUtils.getExtension(sourcePath.toString());
        final String fileName = "." + sourcePath.getFileName().toString();
        return sourcePath.getParent().resolve(fileName.replace(sourceExtension, targetExtension));
    }

    @Override
    public Set<TransformType> getSupportedTransformTypes() {
        return Set.of(
                new TransformType(WORD_2007_MEDIA_TYPE.toString(), MediaType.TEXT_PLAIN.toString())
        );
    }
}
