package io.graphenee.core.flow.documents;

import com.vaadin.flow.server.streams.*;

import java.io.*;
import java.util.Set;

public class GxCustomUploadHandler extends AbstractFileUploadHandler<GxCustomUploadHandler> {

    private FileUploadCallback gxSuccessCallback;
    private FileFactory gxFileFactory;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".pdf", ".doc", ".docx", ".xls", ".xlsx", ".csv", ".ppt", ".pptx", ".jpg", ".jpeg", ".png", ".gif", ".txt");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "text/plain", "text/csv", "image/jpeg", "image/png", "image/gif");

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
            file = gxFileFactory.createFile(metadata);
            try (InputStream inputStream = event.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(
                         file)) {
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
