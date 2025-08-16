package io.graphenee.core.flow.documents;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
	List<GxUploadedFile> uploadedFiles = new ArrayList<>();

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
		entityForm.add(upload);
		setColspan(upload, 2);
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
