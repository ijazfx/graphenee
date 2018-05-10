package io.graphenee.core.vaadin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.vaadin.viritin.button.DownloadButton;
import org.vaadin.viritin.button.DownloadButton.ContentWriter;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.callback.TRParamCallback;
import io.graphenee.core.model.bean.GxSavedQueryBean;
import io.graphenee.core.util.JSONUtils;
import io.graphenee.vaadin.AbstractDashboardPanel;
import io.graphenee.vaadin.TRAbstractBaseForm.SavedHandler;
import io.graphenee.vaadin.TRAbstractQueryForm;
import io.graphenee.vaadin.TRAbstractQueryForm.QueryFormDelegate;

public abstract class GxAbstractQuerySpreadsheetPanel<QB, RB> extends AbstractDashboardPanel implements View, QueryFormDelegate<QB> {

	private static final long serialVersionUID = 1L;

	private static final Logger L = LoggerFactory.getLogger(GxAbstractQuerySpreadsheetPanel.class);

	private Class<QB> queryBeanClass;
	private TabSheet tabsheet;

	private CellStyle dateStyle;

	private CellStyle dateTimeStyle;

	private CellStyle timeStyle;

	private TRParamCallback<GxSavedQueryBean> onSaveCallback;
	private TRParamCallback<GxSavedQueryBean> onDeleteCallback;

	public GxAbstractQuerySpreadsheetPanel(Class<QB> queryBeanClass) {
		this.queryBeanClass = queryBeanClass;
	}

	@Override
	public void onSubmit(QB queryBean) {
		Spreadsheet spreadsheet = new Spreadsheet();
		spreadsheet.setReadOnly(true);
		Workbook workbook = spreadsheet.getWorkbook();
		Sheet sheet = workbook.getSheetAt(0);

		dateStyle = sheet.getWorkbook().createCellStyle();
		dateStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("dd/MM/yyyy"));

		dateTimeStyle = sheet.getWorkbook().createCellStyle();
		dateTimeStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("dd/MM/yyyy hh:mm"));

		timeStyle = sheet.getWorkbook().createCellStyle();
		timeStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("hh:mm"));

		DownloadButton downloadButton = new DownloadButton();
		downloadButton.setIcon(FontAwesome.FILE_EXCEL_O);
		downloadButton.setCaption("Download");
		String file = "results.xls";
		if (workbook instanceof XSSFWorkbook) {
			file += "x";
		}
		downloadButton.setFileName(file);
		downloadButton.setWriter(new ContentWriter() {

			@Override
			public void write(OutputStream stream) {
				try {
					workbook.write(stream);
				} catch (IOException e) {
					L.warn(e.getMessage(), e);
				}
			}
		});
		downloadButton.setVisible(false);
		spreadsheet.setVisible(false);

		ProgressBar progress = new ProgressBar();
		progress.setCaption("Preparing results, please wait...");
		progress.setIndeterminate(true);

		Tab tab = tabsheet.addTab(new MVerticalLayout(progress, downloadButton, spreadsheet).withExpand(spreadsheet, 1).withFullHeight());
		tab.setCaption("Results");
		tab.setClosable(true);
		tabsheet.setSelectedTab(tab);

		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		ListenableFuture<Collection<RB>> listenable = executor.submitListenable(new Callable<Collection<RB>>() {

			@Override
			public Collection<RB> call() throws Exception {
				Collection<RB> query = query(queryBean);
				return query;
			}

		});

		listenable.addCallback(new ListenableFutureCallback<Collection<RB>>() {

			@Override
			public void onSuccess(Collection<RB> result) {
				UI.getCurrent().access(() -> {
					Iterator<RB> iter = result.iterator();
					Row headerRow = sheet.createRow(0);
					generateCellsForHeaderRow(sheet, headerRow);
					int i = 1;
					while (iter.hasNext()) {
						RB bean = iter.next();
						Row row = sheet.createRow(i);
						try {
							generateCellsForDataRow(sheet, row, bean);
						} catch (Exception e) {
							L.error(e.getMessage(), e);
						}
						i++;
					}
					for (int k = headerRow.getFirstCellNum(); k <= headerRow.getLastCellNum(); k++) {
						spreadsheet.autofitColumn(k);
					}
					downloadButton.setVisible(true);
					spreadsheet.setVisible(true);
					progress.setVisible(false);
					UI.getCurrent().push();
				});
			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub
				ex.printStackTrace();
			}
		});

	}

	@Override
	protected boolean shouldShowHeader() {
		return true;
	}

	protected abstract void generateCellsForDataRow(Sheet sheet, Row row, RB bean);

	protected abstract void generateCellsForHeaderRow(Sheet sheet, Row headerRow);

	protected abstract Collection<RB> query(QB queryBean);

	@Override
	protected void postInitialize() {
		try {
			QB queryBean = queryBeanClass.newInstance();
			queryForm().setEntity(queryBean);
			queryForm().setQueryFormDelegate(this);
			queryForm().addButtonToFooter("Save", bean -> {
				saveQuery(bean);
			});
			tabsheet = new TabSheet();
			tabsheet.setSizeFull();
			tabsheet.setStyleName(ValoTheme.TABSHEET_FRAMED);
			queryForm().setCaption("Query");
			tabsheet.addComponents(queryForm());
			addComponent(tabsheet);
		} catch (InstantiationException | IllegalAccessException e) {
			L.error(e.getMessage(), e);
		}
	}

	protected void saveQuery(QB bean) {
		GxSaveQueryForm saveQueryForm = new GxSaveQueryForm();
		GxSavedQueryBean savedQueryBean = new GxSavedQueryBean();
		String json = JSONUtils.objectToJson(bean);
		savedQueryBean.setQueryBeanJson(json);
		saveQueryForm.setEntity(savedQueryBean);
		saveQueryForm.openInModalPopup();
		saveQueryForm.setSavedHandler(new SavedHandler<GxSavedQueryBean>() {

			@Override
			public void onSave(GxSavedQueryBean entity) {
				saveQueryForm.closePopup();
				if (onSaveCallback != null)
					onSaveCallback.execute(entity);
			}
		});
	}

	public abstract TRAbstractQueryForm<QB> queryForm();

	protected CellStyle getDateStyle() {
		return dateStyle;
	}

	protected CellStyle getDateTimeStyle() {
		return dateTimeStyle;
	}

	protected CellStyle getTimeStyle() {
		return timeStyle;
	}

	public void setOnSaveCallback(TRParamCallback<GxSavedQueryBean> onSaveCallback) {
		this.onSaveCallback = onSaveCallback;
	}

	public void setOnDeleteCallback(TRParamCallback<GxSavedQueryBean> onDeleteCallback) {
		this.onDeleteCallback = onDeleteCallback;
	}

}