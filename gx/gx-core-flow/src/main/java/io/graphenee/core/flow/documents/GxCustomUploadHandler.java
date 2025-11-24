package io.graphenee.core.flow.documents;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Set;

import org.apache.tika.Tika;

import com.vaadin.flow.server.streams.AbstractFileUploadHandler;
import com.vaadin.flow.server.streams.FileFactory;
import com.vaadin.flow.server.streams.FileUploadCallback;
import com.vaadin.flow.server.streams.TransferUtil;
import com.vaadin.flow.server.streams.UploadEvent;
import com.vaadin.flow.server.streams.UploadMetadata;

public class GxCustomUploadHandler extends AbstractFileUploadHandler<GxCustomUploadHandler> {

    private FileUploadCallback gxSuccessCallback;
    private FileFactory gxFileFactory;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".pdf", ".doc", ".docx", ".xls", ".xlsx", ".csv",
            ".ppt", ".pptx", ".jpg", ".jpeg", ".png", ".gif", ".txt", ".mp3", ".mp4", ".m4a", ".mpeg");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation", "text/plain", "text/csv",
            "image/jpeg", "image/png", "image/gif", "audio/.*", "video/.*");
    private static final int MAX_HEADER_SIZE = 16 * 1024; // 16 KB for Tika detection

    public GxCustomUploadHandler(FileUploadCallback successCallback, FileFactory fileFactory) {
        super(successCallback, fileFactory);
        this.gxSuccessCallback = successCallback;
        this.gxFileFactory = fileFactory;
    }

    @Override
    public void handleUploadRequest(UploadEvent event) throws IOException {
        UploadMetadata metadata = new UploadMetadata(event.getFileName(),
                event.getContentType(), event.getFileSize());
        File file;
        try {
            String lowerName = event.getFileName().toLowerCase();
            if (ALLOWED_EXTENSIONS.stream().noneMatch(lowerName::endsWith)) {
                throw new IOException("File extension not allowed: " + event.getFileName());
            }

            file = gxFileFactory.createFile(metadata);
            try (InputStream inputStream = event.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(
                            file)) {

                ByteArrayOutputStream headerOut = new ByteArrayOutputStream();
                byte[] buf = new byte[4096];
                int bytesRead, total = 0;
                while ((bytesRead = inputStream.read(buf)) != -1 && total < MAX_HEADER_SIZE) {
                    int toWrite = Math.min(bytesRead, MAX_HEADER_SIZE - total);
                    headerOut.write(buf, 0, toWrite);
                    total += toWrite;
                    if (total >= MAX_HEADER_SIZE) {
                        break;
                    }
                }
                byte[] headerBuffer = headerOut.toByteArray();

                Tika tika = new Tika();
                String detectedMimeType = tika.detect(headerBuffer, event.getFileName());

                if (detectedMimeType == null || !ALLOWED_MIME_TYPES.stream().filter(mt -> detectedMimeType.matches(mt))
                        .findFirst().isPresent()) {
                    throw new IllegalArgumentException("Unsupported or suspicious file type: " + detectedMimeType);
                }

                // write header
                try (InputStream headerStream = new ByteArrayInputStream(headerBuffer)) {
                    TransferUtil.transfer(headerStream, outputStream,
                            getTransferContext(event), getListeners());
                }

                // write remaining
                TransferUtil.transfer(inputStream, outputStream,
                        getTransferContext(event), getListeners());
            }
        } catch (IOException e) {
            notifyError(event, e);
            throw e;
        }
        event.getUI().access(() -> {
            try {
                gxSuccessCallback.complete(metadata, file);
            } catch (IOException e) {
                throw new UncheckedIOException("Error in file upload callback",
                        e);
            }
        });
    }

}
