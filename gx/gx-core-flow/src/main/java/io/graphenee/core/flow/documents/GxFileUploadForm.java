package io.graphenee.core.flow.documents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.flowingcode.vaadin.addons.chipfield.ChipField;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.core.model.jpa.repository.GxFileTagRepository;
import io.graphenee.vaadin.flow.data.ListToSetConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.FileUploadCallback;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxFileUploadForm extends GxAbstractEntityForm<GxFolder> {

	Upload upload;
	ChipField<GxFileTag> fileTags;

	List<GxUploadedFile> uploadedFiles = new ArrayList<>();

	public GxFileUploadForm() {
		super(GxFolder.class);
	}

	@Autowired
	GxFileTagRepository tagRepository;

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
				if (fileTags.getValue() != null) {
					uploadedFile.setFileTags(fileTags.getValue());
				}
				uploadedFiles.add(uploadedFile);
			}

		}));

		upload.setMaxFiles(10);
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

	public void initializeWithFileUploadHandler(GxFileUploadHandler handler) {
		setDelegate(dlg -> {
			handler.onSave(dlg, uploadedFiles);
		});
	}

	@Override
	protected void preBinding(GxFolder entity) {
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

	public static interface GxFileUploadHandler {
		void onSave(GxFolder parentFolder, List<GxUploadedFile> uploadedFiles);
	}

}
