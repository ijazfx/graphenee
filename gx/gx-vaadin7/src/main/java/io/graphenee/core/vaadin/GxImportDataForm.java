package io.graphenee.core.vaadin;

import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.DownloadButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.api.GxImportDataProcessor;
import io.graphenee.core.util.CSVUtil;
import io.graphenee.vaadin.TRAbstractPanel;
import io.graphenee.vaadin.component.FileChooser;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxImportDataForm extends TRAbstractPanel {

	FileChooser filePath;

	private GxImportDataProcessor importDataProcessor;

	MGrid importDataGrid;

	BeanItemContainer importBeanContainer;

	private MButton importDataButton;

	private Consumer<List> onImportCompletion;

	public GxImportDataForm() {

	}

	@Override
	protected void addButtonsToFooter(MHorizontalLayout layout) {
		layout.setVisible(false);
	}

	public void importData() {
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

		layout.withMargin(true).withSpacing(true);

		importBeanContainer = new BeanItemContainer<>(importDataProcessor.getEntityClass());
		importDataGrid = new MGrid<>();
		importDataGrid.setSizeFull();
		importDataGrid.setContainerDataSource(importBeanContainer);
		importDataGrid.withProperties(importDataProcessor.getVisibleProperties());
		filePath = new FileChooser("Select File");
		filePath.addValueChangeListener(e -> {
			importBeanContainer.removeAllItems();
			if (filePath.getValue() != null) {
				importDataProcessor.loadFile(filePath.getValue());
				importBeanContainer.addAll(importDataProcessor.getImportDataBeans());
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
		MHorizontalLayout downloadImportFileLayout = new MHorizontalLayout();
		downloadImportFileLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		downloadImportFileLayout.addComponents(filePath, downloadButton);
		downloadImportFileLayout.setComponentAlignment(downloadButton, Alignment.BOTTOM_RIGHT);
		downloadImportFileLayout.setSizeFull();

		layout.addComponents(downloadImportFileLayout, importDataGrid);

		layout.setExpandRatio(importDataGrid, 1);
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

	@Override
	protected String popupHeight() {
		return "500px";
	}

	@Override
	protected String popupWidth() {
		return "700px";
	}

	public Consumer<List> getOnImportCompletion() {
		return onImportCompletion;
	}

	public void setOnImportCompletion(Consumer<List> onImportCompletion) {
		this.onImportCompletion = onImportCompletion;
	}
}
