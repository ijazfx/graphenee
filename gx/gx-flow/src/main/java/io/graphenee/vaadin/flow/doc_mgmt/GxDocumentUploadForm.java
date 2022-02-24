package io.graphenee.vaadin.flow.doc_mgmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.springframework.context.annotation.Scope;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;

@SpringComponent
@Scope("prototype")
public class GxDocumentUploadForm extends GxAbstractEntityForm<GxFolder> implements Receiver {

	Upload upload;
	FileStorage storage;

	public GxDocumentUploadForm() {
		super(GxFolder.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		upload = new Upload(this);
		entityForm.add(upload);
	}

	@Override
	public OutputStream receiveUpload(String fileName, String mimeType) {
		GxDocument document = new GxDocument();
		document.setName(fileName);
		document.setMimeType(mimeType);
		// getEntity().addDocument(document);
		try {
			return new FileOutputStream("~" + File.separator + fileName);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public void initializeWithStorage(FileStorage storage) {
		this.storage = storage;
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

}
