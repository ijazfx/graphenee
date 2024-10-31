package io.graphenee.vaadin.flow.documents;

import java.io.File;

import lombok.Data;

@Data
public class GxUploadedFile {

    private File file;
    private String fileName;
    private String mimeType;

}
