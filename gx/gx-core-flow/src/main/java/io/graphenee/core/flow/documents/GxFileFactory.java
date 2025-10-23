package io.graphenee.core.flow.documents;

import com.vaadin.flow.server.streams.FileFactory;
import com.vaadin.flow.server.streams.UploadMetadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class GxFileFactory implements FileFactory {
    @Override
    public File createFile(UploadMetadata uploadMetadata) throws IOException {
        Path tempDirPath;
        try {
            tempDirPath = Files.createTempDirectory("temp_dir");
        } catch (IOException e) {
            throw new IOException("Failed to create temp directory", e);
        }

        return Files.createTempFile(tempDirPath, "Graphenee-", uploadMetadata.fileName()).toFile();
    }
}
