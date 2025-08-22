package io.graphenee.vaadin.flow.component;

import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import io.graphenee.util.TRFileContentUtil;
import io.graphenee.vaadin.flow.GxAbstractDialog;
import io.graphenee.vaadin.flow.utils.IconUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("serial")
@Slf4j
public class ResourcePreviewPanel extends GxAbstractDialog {

	@Setter
	@Getter
	private String fileName;

	@Setter
	@Getter
	private StreamResource resource;

	public ResourcePreviewPanel(String fileName, StreamResource resource) {
		setSizeFull();
		setMargin(false);
		setPadding(false);
		setSpacing(false);
		this.fileName = fileName;
		this.resource = resource;
	}

	@Override
	protected void decorateLayout(HasComponents layout) {
		removeAll();
		Scroller scroller = new Scroller();
		scroller.setSizeFull();
		VerticalLayout bodyLayout = new VerticalLayout();
		bodyLayout.addClassName("gx-preview-content-wrapper");
		// bodyLayout.setAlignItems(Alignment.CENTER);
		// bodyLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		// bodyLayout.setSizeFull();
		// bodyLayout.setMargin(false);
		// bodyLayout.setSpacing(false);
		// bodyLayout.setPadding(true);
		scroller.setContent(bodyLayout);
		String mimeType = TRFileContentUtil.getMimeType(fileName);
		if (mimeType == null)
			mimeType = "application/octat-stream";
		resource.setContentType(mimeType);
		try {
			if (mimeType.startsWith("image")) {
				Image image = new Image();
				// image.setHeightFull();
				image.getElement().setAttribute("src", resource);
				bodyLayout.add(image);
			} else if (mimeType.startsWith("audio")) {
				resource.setContentType(mimeType);
				AudioPlayer audioPlayer = new AudioPlayer(resource);
				bodyLayout.add(audioPlayer);

			} else if (mimeType.startsWith("video")) {
				resource.setContentType(mimeType);
				VideoPlayer videoPlayer = new VideoPlayer(resource);
				bodyLayout.add(videoPlayer);
			} else if (mimeType.contains("pdf")) {
				PdfViewer pdfPreview = new PdfViewer();
				pdfPreview.setSizeFull();
				pdfPreview.setSrc(resource);
				bodyLayout.add(pdfPreview);
				UI.getCurrent().access(() -> {
					pdfPreview.openThumbnailsView();
				});
			} else {
				String extension = TRFileContentUtil.getExtensionFromFilename(fileName);
				Image image = IconUtils.fileExtensionIconResource(extension);
				image.setHeight("48px");
				bodyLayout.add(image);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		layout.add(scroller);
	}

	@Override
	protected void decorateToolbar(HasComponents toolbar) {
		Button downloadButton = new Button("Download");
		downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		Anchor download = new Anchor("", "");
		download.setHref(resource);
		download.setId("download");
		download.getElement().setAttribute("download", true);
		download.add(downloadButton);
		toolbar.add(download);
	}

}
