package io.graphenee.vaadin.flow.component;

import java.io.InputStream;
import java.util.function.Supplier;

import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

import io.graphenee.util.TRFileContentUtil;
import io.graphenee.vaadin.flow.GxAbstractDialog;
import io.graphenee.vaadin.flow.utils.IconUtils;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("serial")
@Slf4j
public class ResourcePreviewPanel extends GxAbstractDialog {

	private String fileName;
	private String mimeType;
	private Supplier<InputStream> inputStreamProvider;

	public ResourcePreviewPanel(String fileName, Supplier<InputStream> inputStreamProvider) {
		setSizeFull();
		setMargin(false);
		setPadding(false);
		setSpacing(false);
		this.fileName = fileName;
		this.mimeType = TRFileContentUtil.getMimeType(fileName);
		this.inputStreamProvider = inputStreamProvider;
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
		try {
			if (mimeType.startsWith("image")) {
				Image image = new Image();
				// image.setHeightFull();
				image.setSrc(DownloadHandler.fromInputStream(de -> {
					return new DownloadResponse(inputStreamProvider.get(), fileName, mimeType, -1);
				}));
				bodyLayout.add(image);
			} else if (mimeType.startsWith("audio")) {
				AudioPlayer audioPlayer = new AudioPlayer();
				audioPlayer.setSrc(DownloadHandler.fromInputStream(de -> {
					return new DownloadResponse(inputStreamProvider.get(), fileName, mimeType, -1);
				}));
				bodyLayout.add(audioPlayer);
			} else if (mimeType.startsWith("video")) {
				VideoPlayer videoPlayer = new VideoPlayer();
				videoPlayer.setSrc(DownloadHandler.fromInputStream(de -> {
					return new DownloadResponse(inputStreamProvider.get(), fileName, mimeType, -1);
				}));
				bodyLayout.add(videoPlayer);
			} else if (mimeType.contains("pdf")) {
				PdfViewer pdfPreview = new PdfViewer();
				pdfPreview.setSizeFull();
				pdfPreview.setAddPrintButton(true);
				pdfPreview.setAddRotateClockwiseButton(true);
				pdfPreview.setAddRotateCounterClockwiseButton(true);
				pdfPreview.setCustomTitle("PDF Viewer");
				pdfPreview.setSrc(DownloadHandler.fromInputStream(de -> {
					return new DownloadResponse(inputStreamProvider.get(), fileName, mimeType, -1);
				}));
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
		download.setHref(DownloadHandler.fromInputStream(de -> {
			return new DownloadResponse(inputStreamProvider.get(), fileName, mimeType, -1);
		}));
		download.setId("download");
		download.getElement().setAttribute("download", true);
		download.add(downloadButton);
		toolbar.add(download);
	}

}
