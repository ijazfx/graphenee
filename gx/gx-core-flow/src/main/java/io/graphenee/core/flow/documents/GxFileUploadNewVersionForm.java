package io.graphenee.core.flow.documents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.server.streams.FileUploadCallback;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.core.model.jpa.repository.GxFileTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxFileUploadNewVersionForm extends GxAbstractEntityForm<GxDocument> {

	Upload upload;
	MultiSelectComboBox<GxFileTag> fileTags;
	List<GxUploadedFile> uploadedFiles = new ArrayList<>();

	@Autowired
	GxFileTagRepository tagRepository;

	public GxFileUploadNewVersionForm() {
		super(GxDocument.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		upload = new Upload();
		upload.setUploadHandler(UploadHandler.toFile(new FileUploadCallback() {

			@Override
			public void complete(UploadMetadata uploadMetadata, File file) throws IOException {
				GxUploadedFile uploadedFile = new GxUploadedFile();
				uploadedFile.setFile(file);
				uploadedFile.setFileName(uploadMetadata.fileName());
				uploadedFile.setMimeType(uploadMetadata.contentType());
				if (fileTags.getValue() != null) {
					uploadedFile.setFileTags(fileTags.getValue().stream().toList());
				}
				uploadedFiles.add(uploadedFile);
			}

		}, new GxFileFactory()));
		upload.setMaxFiles(1);
		upload.setMaxFileSize(1024000000);
		fileTags = new MultiSelectComboBox<>("Add Tags");

		fileTags.addValueChangeListener(l -> {
			if (l.getValue() == null) {
				uploadedFiles.forEach(f -> f.setFileTags(null));
			} else {
				uploadedFiles.forEach(f -> f.setFileTags(l.getValue().stream().toList()));
			}
		});
		fileTags.addCustomValueSetListener(l -> {
			GxFileTag newTag = new GxFileTag();
			newTag.setTag(l.getDetail());
			newTag.setOid(null);

			// Copy current value into a mutable set
			Set<GxFileTag> updated = new HashSet<>(fileTags.getValue());
			updated.add(newTag);

			// Update items (so the combo knows this tag exists)
			List<GxFileTag> items = new ArrayList<>(fileTags.getListDataView().getItems().toList());
			items.add(newTag);
			fileTags.setItems(items);

			// Set new value
			fileTags.setValue(updated);
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
