package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.vaadin.flow.documents.GxDocumentExplorer;
import io.graphenee.vaadin.flow.utils.IconUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
				image = IconUtils.fileExtensionIconResource("image");
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
		image.setHeight("4rem");
		fl.add(image, label);
		return fl;
	}

	public GxDocumentChooser() {
		this("Browse Documents...");
	}

	public GxDocumentChooser(String buttonCaption) {
		addClassName("gx-document-chooser");

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(false);
		rootLayout.setPadding(false);
		rootLayout.setWidthFull();

		Button selectDocument = new Button(buttonCaption);
		selectDocument.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS);
		selectDocument.addClickListener(cl -> {
			explorer.chooseSingle(doc -> {
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
