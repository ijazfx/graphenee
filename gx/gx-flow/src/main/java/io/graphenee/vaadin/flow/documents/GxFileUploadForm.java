package io.graphenee.vaadin.flow.documents;

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

import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxFileUploadForm extends GxAbstractEntityForm<GxFolder> {

	Upload upload;

	List<GxUploadedFile> uploadedFiles = new ArrayList<>();

	public GxFileUploadForm() {
		super(GxFolder.class);
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
					uploadedFiles.add(uploadedFile);
					FileOutputStream fos = new FileOutputStream(file);
					return fos;
				} catch (Exception e) {
					return null;
				}
			}

		});
		upload.setMaxFiles(10);
		upload.setMaxFileSize(1024000000);
		entityForm.add(upload);
	}

	public void initializeWithFileUploadHandler(GxFileUploadHandler handler) {
		setDelegate(dlg -> {
			handler.onSave(dlg, uploadedFiles);
		});
	}

	@Override
	protected void preBinding(GxFolder entity) {
		uploadedFiles.clear();
	}

	@Override
	protected String dialogHeight() {
		return "100%";
	}

	@Override
	protected String dialogWidth() {
		return "100%";
	}

	@Override
	protected String formTitleProperty() {
		return "name";
	}

	public static interface GxFileUploadHandler {
		void onSave(GxFolder parentFolder, List<GxUploadedFile> uploadedFiles);
	}

}
