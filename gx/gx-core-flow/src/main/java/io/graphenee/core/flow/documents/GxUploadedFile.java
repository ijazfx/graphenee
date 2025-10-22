package io.graphenee.core.flow.documents;

import java.io.File;
import java.util.List;

import io.graphenee.core.model.entity.GxTag;
import lombok.Data;

@Data
public class GxUploadedFile {

	private File file;
	private String fileName;
	private String mimeType;
	private List<GxTag> tags;

}
