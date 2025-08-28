package io.graphenee.core.flow.documents;

import java.io.IOException;
import java.io.InputStream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentFilter;
import io.graphenee.vaadin.flow.utils.IconUtils;

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
		GxDocument value = getValue();
		if (value != null)
			docsLayout.add(createItem(value));
	}

	private Component createItem(GxDocument s) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setPadding(false);
		layout.setSpacing(false);
		FlexLayout fl = new FlexLayout();
		fl.setClassName("gx-document-chooser-item");
		fl.setFlexDirection(FlexDirection.ROW);
		fl.setAlignItems(Alignment.END);
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
					DownloadHandler dh = DownloadHandler.fromInputStream(new InputStreamDownloadCallback() {

						@Override
						public DownloadResponse complete(DownloadEvent downloadEvent) throws IOException {
							return new DownloadResponse(stream, s.getName(), s.getMimeType(), s.getSize());
						}
						
					});
					image = new Image(dh, s.getName());

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
		Button deleteDocument = new Button();
		deleteDocument.addClickListener(cl -> {
			setPresentationValue(null);
		});
		deleteDocument.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
		deleteDocument.setIcon(VaadinIcon.TRASH.create());
		fl.add(image, label);
		image.addClickListener(cl -> {
			explorer.previewDocument(getValue());
		});
		layout.add(fl, deleteDocument);
		return layout;
	}

	public GxDocumentChooser(GxDocumentFilter filter) {
		this("Attach Document...", filter);
	}

	public GxDocumentChooser(String buttonCaption, GxDocumentFilter filter) {
		addClassName("gx-document-chooser");

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(false);
		rootLayout.setPadding(false);
		rootLayout.setWidthFull();

		Button attachDocument = new Button(buttonCaption);
		attachDocument.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS);
		attachDocument.addClickListener(cl -> {
			explorer.uploadSingle(filter, doc -> {
				setPresentationValue(doc);
			});
		});

		Button browseDocument = new Button(VaadinIcon.FOLDER_SEARCH.create());
		browseDocument.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS);
		browseDocument.addClickListener(cl -> {
			explorer.chooseSingle(filter, doc -> {
				setPresentationValue(doc);
			});
		});

		docsLayout = new FlexLayout();

		HorizontalLayout buttonsLayout = new HorizontalLayout(attachDocument, browseDocument);
		buttonsLayout.setSpacing(false);

		rootLayout.add(buttonsLayout, docsLayout);
		add(rootLayout);
	}

	public void initializeWithExplorer(GxDocumentExplorer explorer) {
		this.explorer = explorer;
	}

}
