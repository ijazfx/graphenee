package io.graphenee.core.vaadin;

import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.DownloadButton;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import io.graphenee.core.exception.InvalidImportFormatException;
import io.graphenee.core.model.api.GxImportDataProcessor;
import io.graphenee.util.CSVUtil;
import io.graphenee.util.TRFileContentUtil;
import io.graphenee.vaadin.TRAbstractPanel;
import io.graphenee.vaadin.component.FileChooser;
import io.graphenee.vaadin.ui.GxNotification;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxImportDataForm extends TRAbstractPanel {

    FileChooser filePath;

    private GxImportDataProcessor importDataProcessor;

    MGrid importDataGrid;

    BeanItemContainer importBeanContainer;

    private Consumer<List> onImportCompletion;

    public GxImportDataForm() {

    }

    @Override
    protected void addButtonsToFooter(MHorizontalLayout layout) {
        layout.setVisible(false);
    }

    public void importData() throws InvalidImportFormatException {
        if (filePath.getValue() == null || !TRFileContentUtil.getExtensionFromFilename(filePath.getValue()).equals("csv")) {
            throw new InvalidImportFormatException("File not uploaded or Invalid file format");
        } else
            ConfirmDialog.show(UI.getCurrent(), null, "Do you want to import selected file?", "Yes", "No", p -> {
                if (p.isConfirmed()) {
                    importDataProcessor.saveData();
                    List importDataBeans = importDataProcessor.getImportDataBeans();
                    if (onImportCompletion != null) {
                        onImportCompletion.accept(importDataBeans);
                    }
                }
            });
    }

    @Override
    protected String panelTitle() {
        return null;
    }

    @Override
    protected void addComponentsToContentLayout(MVerticalLayout layout) {
        importBeanContainer = new BeanItemContainer<>(importDataProcessor.getEntityClass());
        importDataGrid = new MGrid<>();
        importDataGrid.setSizeFull();
        importDataGrid.setContainerDataSource(importBeanContainer);
        importDataGrid.withProperties(importDataProcessor.getVisibleProperties());
        filePath = new FileChooser("Select File (Only csv file)");
        filePath.addValueChangeListener(e -> {
            importBeanContainer.removeAllItems();
            if (filePath.getValue() != null) {
                if (TRFileContentUtil.getExtensionFromFilename(filePath.getValue()).equals("csv")) {
                    importDataProcessor.loadFile(filePath.getValue());
                    importBeanContainer.addAll(importDataProcessor.getImportDataBeans());
                } else {
                    GxNotification.tray("Invalid Format", "Please upload file in csv format").show(Page.getCurrent());
                }
            }
        });
        String fileName = "import-template.csv";
        DownloadButton downloadButton = new DownloadButton((OutputStream out) -> {
            try {
                out.write(CSVUtil.getHeaderRow(importDataProcessor.requiredColoumnHeader()).getBytes("UTF-8"));
            } catch (Exception e2) {
            }
        }).setFileNameProvider(() -> {
            return fileName;
        }).setMimeTypeProvider(() -> {
            return "text/csv";
        }).withCaption("Download Template");
        downloadButton.withStyleName(ValoTheme.BUTTON_LINK);
        MHorizontalLayout downloadImportFileLayout = new MHorizontalLayout().withHeightUndefined().withWidth("100%");
        downloadImportFileLayout.addComponents(filePath, downloadButton);
        downloadImportFileLayout.setComponentAlignment(downloadButton, Alignment.BOTTOM_RIGHT);

        layout.addComponents(downloadImportFileLayout, importDataGrid);
        layout.setExpandRatio(importDataGrid, 1);
        layout.setSpacing(true);
    }

    @Override
    public TRAbstractPanel build() {
        TRAbstractPanel build = super.build();
        filePath.setValue(null);
        return build;
    }

    public void initializeWithDataProcessor(GxImportDataProcessor importDataProcessor) {
        this.importDataProcessor = importDataProcessor;
    }

    public Consumer<List> getOnImportCompletion() {
        return onImportCompletion;
    }

    public void setOnImportCompletion(Consumer<List> onImportCompletion) {
        this.onImportCompletion = onImportCompletion;
    }
}
