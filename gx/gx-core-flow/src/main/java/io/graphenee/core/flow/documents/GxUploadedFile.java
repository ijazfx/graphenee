package io.graphenee.core.flow.documents;

import java.io.File;
import java.util.List;
import java.util.Set;

import io.graphenee.core.model.entity.GxFileTag;
import lombok.Data;

@Data
public class GxUploadedFile {

	private File file;
	private String fileName;
	private String mimeType;
	private List<GxFileTag> fileTags;

}
