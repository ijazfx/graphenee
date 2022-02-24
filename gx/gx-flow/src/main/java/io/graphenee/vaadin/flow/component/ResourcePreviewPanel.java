package io.graphenee.vaadin.flow.component;

import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import io.graphenee.util.TRFileContentUtil;
import lombok.Getter;
import lombok.Setter;

public class ResourcePreviewPanel extends VerticalLayout {

    @Setter
    @Getter
    private String fileName;

    @Setter
    @Getter
    private StreamResource resource;

    private GxDialog dialog = null;

    public ResourcePreviewPanel(String fileName, StreamResource resource) {
        setSizeFull();
        setMargin(false);
        setPadding(false);
        setSpacing(false);
        this.fileName = fileName;
        this.resource = resource;
    }

    private synchronized ResourcePreviewPanel build() {
        removeAll();
        Scroller scroller = new Scroller();
        scroller.setSizeFull();
        VerticalLayout bodyLayout = new VerticalLayout();
        bodyLayout.setAlignItems(Alignment.CENTER);
        bodyLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        bodyLayout.setSizeFull();
        bodyLayout.setMargin(false);
        bodyLayout.setSpacing(false);
        bodyLayout.setPadding(true);
        scroller.setContent(bodyLayout);
        String mimeType = TRFileContentUtil.getMimeType(fileName);
        try {
            if (mimeType.startsWith("image")) {
                Image image = new Image();
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
            } else {
                PdfViewer pdfPreview = new PdfViewer();
                pdfPreview.setSizeFull();
                pdfPreview.openThumbnailsView();
                pdfPreview.setSrc(resource);
                bodyLayout.add(pdfPreview);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(scroller, createToolbar());
        return this;
    }

    public GxDialog showInDialog(String width, String height) {
        build();
        dialog = new GxDialog(ResourcePreviewPanel.this);
        dialog.addThemeVariants(DialogVariant.NO_PADDING);
        dialog.setWidth(width);
        dialog.setHeight(height);
        dialog.setResizable(true);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.open();
        return dialog;
    }

    public void closeDialog() {
        if (dialog != null) {
            dialog.close();
        }
    }

    private Component createToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.getStyle().set("border-radius", "var(--lumo-border-radius)");
        toolbar.getStyle().set("border-top-right-radius", "0px");
        toolbar.getStyle().set("border-top-left-radius", "0px");
        toolbar.getStyle().set("background-color", "#F8F8F8");
        toolbar.setWidthFull();
        toolbar.setPadding(true);
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        Button dismissButton = new Button("DISMISS");
        dismissButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        dismissButton.addClickListener(cl -> {
            if (dialog != null) {
                dialog.close();
            }
        });
        toolbar.add(dismissButton);
        return toolbar;
    }
}
