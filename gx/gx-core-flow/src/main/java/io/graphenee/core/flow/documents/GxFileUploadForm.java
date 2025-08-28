package io.graphenee.core.flow.documents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.FileUploadCallback;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

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
		upload = new Upload();
		upload.setUploadHandler(UploadHandler.toTempFile(new FileUploadCallback() {

			@Override
			public void complete(UploadMetadata uploadMetadata, File file) throws IOException {
				GxUploadedFile uploadedFile = new GxUploadedFile();
				uploadedFile.setFile(file);
				uploadedFile.setFileName(uploadMetadata.fileName());
				uploadedFile.setMimeType(uploadMetadata.contentType());
				uploadedFiles.add(uploadedFile);
			}

		}));

		// upload = new Upload(new Receiver() {

		// @Override
		// public OutputStream receiveUpload(String fileName, String mimeType) {
		// try {
		// File file = File.createTempFile("uploaded", fileName);
		// GxUploadedFile uploadedFile = new GxUploadedFile();
		// uploadedFile.setFile(file);
		// uploadedFile.setFileName(fileName);
		// uploadedFile.setMimeType(mimeType);
		// uploadedFiles.add(uploadedFile);
		// FileOutputStream fos = new FileOutputStream(file);
		// return fos;
		// } catch (Exception e) {
		// return null;
		// }
		// }

		// });
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

	public static interface GxFileUploadHandler {
		void onSave(GxFolder parentFolder, List<GxUploadedFile> uploadedFiles);
	}

}
