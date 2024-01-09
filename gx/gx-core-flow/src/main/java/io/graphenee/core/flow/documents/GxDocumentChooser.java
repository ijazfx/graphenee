package io.graphenee.core.flow.documents;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentFilter;
import io.graphenee.vaadin.flow.utils.IconUtils;

@SuppressWarnings("serial")
public class GxDocumentChooser extends CustomField<GxDocument> {

	private FlexLayout docsLayout;
	private GxDocumentExplorer explorer;

	@Override
	protected GxDocument generateModelValue() {
		return getValue();
	}

	@Override
	protected void setPresentationValue(GxDocument newPresentationValue) {
		setValue(newPresentationValue);
		refreshLayout();
	}

	private void refreshLayout() {
		docsLayout.removeAll();
		docsLayout.add(createItem(getValue()));
	}

	private Component createItem(GxDocument s) {
		FlexLayout fl = new FlexLayout();
		fl.setClassName("gx-document-chooser-item");
		fl.setFlexDirection(FlexDirection.COLUMN);
		NativeLabel label = new NativeLabel(s.getName());
		Image image = null;
		String extension = s.getExtension();
		String mimeType = s.getMimeType();
		if (!s.isFile()) {
			image = IconUtils.fileExtensionIconResource("folder");
		} else {
			if (mimeType.startsWith("image")) {
				String resourcePath = explorer.storage.resourcePath("documents", s.getPath());
				try {
					InputStream stream = explorer.storage.resolve(resourcePath);
					byte[] bytes = IOUtils.toByteArray(stream);
					StreamResource sr = new StreamResource(s.getName(), () -> new ByteArrayInputStream(bytes));
					sr.setContentType(mimeType);
					image = new Image(sr, s.getName());
				} catch (Exception e) {
					image = IconUtils.fileExtensionIconResource("image");
				}
			} else if (mimeType.startsWith("audio")) {
				image = IconUtils.fileExtensionIconResource("audio");
			} else if (mimeType.startsWith("video")) {
				image = IconUtils.fileExtensionIconResource("video");
			} else {
				image = IconUtils.fileExtensionIconResource(extension);
			}
			if (image == null) {
				image = IconUtils.fileExtensionIconResource("bin");
			}
		}
		fl.add(image, label);
		fl.setWidth("4rem");
		return fl;
	}

	public GxDocumentChooser(GxDocumentFilter filter) {
		this("Browse Documents...", filter);
	}

	public GxDocumentChooser(String buttonCaption, GxDocumentFilter filter) {
		addClassName("gx-document-chooser");

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(false);
		rootLayout.setPadding(false);
		rootLayout.setWidthFull();

		Button selectDocument = new Button(buttonCaption);
		selectDocument.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS);
		selectDocument.addClickListener(cl -> {
			explorer.chooseSingle(filter, doc -> {
				setPresentationValue(doc);
			});
		});

		docsLayout = new FlexLayout();

		rootLayout.add(selectDocument, docsLayout);
		add(rootLayout);
	}

	public void initializeWithExplorer(GxDocumentExplorer explorer) {
		this.explorer = explorer;
	}

}
