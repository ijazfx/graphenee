package io.graphenee.core.flow.documents;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.core.model.jpa.repository.GxFileTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxFileUploadNewVersionForm extends GxAbstractEntityForm<GxDocument> {

	Upload upload;
	ChipField<GxFileTag> fileTags;
	List<GxUploadedFile> uploadedFiles = new ArrayList<>();

	@Autowired
	GxFileTagRepository tagRepository;

	public GxFileUploadNewVersionForm() {
		super(GxDocument.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		upload = new Upload(new Receiver() {

			@Override
			public OutputStream receiveUpload(String fileName, String mimeType) {
				try {
					File file = File.createTempFile("uploaded", fileName);
					GxUploadedFile uploadedFile = new GxUploadedFile();
					uploadedFile.setFile(file);
					uploadedFile.setFileName(fileName);
					uploadedFile.setMimeType(mimeType);
					uploadedFiles.clear();
					uploadedFiles.add(uploadedFile);
					FileOutputStream fos = new FileOutputStream(file);
					return fos;
				} catch (Exception e) {
					return null;
				}
			}

		});
		upload.setMaxFiles(1);
		upload.setMaxFileSize(1024000000);
		fileTags = new ChipField<>("Add Tags");

		fileTags.addValueChangeListener(l -> {
			if (l.getValue() == null) {
				uploadedFiles.forEach(f -> f.setFileTags(null));
			} else {
				uploadedFiles.forEach(f -> f.setFileTags(l.getValue()));
			}
		});
		fileTags.setNewItemHandler(label -> {
			GxFileTag newTag = new GxFileTag();
			newTag.setTag(label);
			newTag.setOid(null);
			return newTag;
		});

		entityForm.add(upload, fileTags);
		setColspan(upload, 2);
		setColspan(fileTags, 2);
	}

	public void initializeWithFileUploadHandler(GxFileUploadNewVersionHandler handler) {
		setDelegate(dlg -> {
			GxUploadedFile uploadedFile = !uploadedFiles.isEmpty() ? uploadedFiles.get(0) : null;
			handler.onSave(dlg, uploadedFile);
		});
	}

	@Override
	protected void preBinding(GxDocument entity) {
		uploadedFiles.clear();
		fileTags.setItems(tagRepository.findAll());
	}

	@Override
	protected String dialogHeight() {
		return "37.5rem";
	}

	@Override
	protected String dialogWidth() {
		return "50rem";
	}

	@Override
	protected String formTitleProperty() {
		return "name";
	}

	public static interface GxFileUploadNewVersionHandler {
		void onSave(GxDocument parentDocument, GxUploadedFile uploadedFile);
	}

}
