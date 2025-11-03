package io.graphenee.core.flow.documents;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.FileUploadCallback;
import com.vaadin.flow.server.streams.UploadMetadata;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SpringComponent
@Scope("prototype")
public class GxFileUploadForm extends GxAbstractEntityForm<GxFolder> {

	@Autowired
	GxDataService dataService;

	Upload upload;
	MultiSelectComboBox<GxTag> tags;
	MultiSelectComboBox<Principal> grants;
	List<GxUploadedFile> uploadedFiles = new ArrayList<>();

	public GxFileUploadForm() {
		super(GxFolder.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		upload = new Upload();
		GxCustomUploadHandler gxFileUploadHandler = new GxCustomUploadHandler(new FileUploadCallback() {

			@Override
			public void complete(UploadMetadata uploadMetadata, File file) throws IOException {
				GxUploadedFile uploadedFile = new GxUploadedFile();
				uploadedFile.setFile(file);
				uploadedFile.setFileName(uploadMetadata.fileName());
				uploadedFile.setMimeType(uploadMetadata.contentType());
				if (tags.getValue() != null) {
					uploadedFile.setTags(tags.getValue().stream().toList());
				}
				uploadedFiles.add(uploadedFile);
			}

		}, new GxFileFactory());
		upload.setUploadHandler(gxFileUploadHandler);
		upload.setMaxFiles(10);
		upload.setMaxFileSize(1024000000);

		tags = new MultiSelectComboBox<>("Add Tags");

		tags.addValueChangeListener(l -> {
			if (l.getValue() == null) {
				uploadedFiles.forEach(f -> f.setTags(null));
			} else {
				uploadedFiles.forEach(f -> f.setTags(l.getValue().stream().toList()));
			}
		});

		tags.addCustomValueSetListener(l -> {
			GxTag newTag = new GxTag();
			newTag.setTag(l.getDetail());
			newTag.setNamespace(getEntity().getNamespace());
			tags.getListDataView().addItem(newTag);
			Set<GxTag> updated = new HashSet<>(tags.getValue());
			updated.add(newTag);
			tags.setValue(updated);
			tags.setOpened(false);
		});
		grants = new MultiSelectComboBox<>("Grant Access (User/Group)");
		grants.setItemLabelGenerator(i -> i.getName());

		entityForm.add(upload, tags, grants);
		expand(upload, tags, grants);
	}

	public void initializeWithFileUploadHandler(GxFileUploadHandler handler) {
		setDelegate(dlg -> {
			handler.onSave(dlg, uploadedFiles);
		});
	}

	@Override
	protected void preBinding(GxFolder entity) {
		uploadedFiles.clear();
		tags.clear();
		grants.clear();
		tags.setItems(dataService.findTagByNamespace(entity.getNamespace()));
		grants.setItems(dataService.findPrincipalActiveByNamespace(entity.getNamespace()));
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
