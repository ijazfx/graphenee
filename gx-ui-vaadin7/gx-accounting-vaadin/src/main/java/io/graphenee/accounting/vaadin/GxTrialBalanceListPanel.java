package io.graphenee.accounting.vaadin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.button.MButton;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table.Align;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxTrialBalanceBean;
import io.graphenee.core.storage.FileStorage;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.AbstractEntityTablePanel;
import io.graphenee.vaadin.ResourcePreviewPanel;
import io.graphenee.vaadin.TRAbstractForm;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxTrialBalanceListPanel extends AbstractEntityTablePanel<GxTrialBalanceBean> {

	public static final Logger L = LoggerFactory.getLogger(GxTrialBalanceBean.class);

	private GxNamespaceBean namespaceBean;

	private String companyName;

	private ComboBox namespaceComboBox;

	private DateField monthField;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	@Autowired
	FileStorage storage;

	private MButton generateReportButton;

	List<GxTrialBalanceBean> trialBalance = new ArrayList<GxTrialBalanceBean>();

	private String companyLogoPath;

	public GxTrialBalanceListPanel() {
		super(GxTrialBalanceBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxTrialBalanceBean entity) {
		return false;
	}

	@Override
	protected boolean onDeleteEntity(GxTrialBalanceBean entity) {
		return false;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxTrialBalanceBean> fetchEntities() {
		trialBalance = accountingDataService.findAllByMonthAndYearAndNamespace(new Timestamp(monthField.getValue().getTime()), namespaceBean);
		buildFooter();
		return trialBalance;
	}

	@Override
	protected <F> List<GxTrialBalanceBean> fetchEntities(F filter) {
		if (filter instanceof GxNamespaceBean) {
			return accountingDataService.findAllByMonthAndYearAndNamespace(new Timestamp(monthField.getValue().getTime()), namespaceBean);
		}
		return super.fetchEntities(filter);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "accountName", "debit", "credit" };
	}

	@Override
	protected TRAbstractForm<GxTrialBalanceBean> editorForm() {
		return null;
	}

	public void initializeWithEntity(GxNamespaceBean namespaceBean, String companyName, String companyLogoPath) {
		this.namespaceBean = namespaceBean;
		this.companyName = companyName;
		this.companyLogoPath = companyLogoPath;
		namespaceComboBox.setVisible(namespaceBean == null);
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
	}

	private String calculateTotalDebit() {
		return String.format("%.2f", trialBalance.stream().mapToDouble(GxTrialBalanceBean::getDebit).sum());
	}

	private String calculateTotalCredit() {
		return String.format("%.2f", trialBalance.stream().mapToDouble(GxTrialBalanceBean::getCredit).sum());
	}

	private void buildFooter() {
		entityTable().setFooterVisible(true);
		entityTable().setColumnFooter("accountName", "Total");
		entityTable().setColumnFooter("debit", calculateTotalDebit());
		entityTable().setColumnFooter("credit", calculateTotalCredit());
	}

	@Override
	protected void postBuild() {
		super.postBuild();
		setAddButtonVisibility(false);
		setDeleteButtonVisibility(false);
		setEditButtonVisibility(false);
	}

	@Override
	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
		super.addButtonsToToolbar(toolbar);

		namespaceComboBox = new ComboBox("Namespace");
		namespaceComboBox.setTextInputAllowed(false);
		namespaceComboBox.addItems(dataService.findNamespace());
		namespaceComboBox.addValueChangeListener(event -> {
			refresh(event.getProperty().getValue());
		});
		toolbar.addComponent(namespaceComboBox);

		monthField = new DateField("Upto");
		monthField.setResolution(Resolution.MONTH);
		monthField.setDateFormat(TRCalendarUtil.dateFormatter.toPattern());

		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);

		monthField.setValue(currentDate);

		monthField.addValueChangeListener(event -> {
			refresh();
		});

		generateReportButton = new MButton("Generate Report", event -> {
			ResourcePreviewPanel previewPanel = new ResourcePreviewPanel();
			previewPanel.build().openInModalPopup();
			previewPanel.preview(new StreamResource(new StreamSource() {
				@Override
				public InputStream getStream() {
					try {
						String reportName = null;
						Map<String, Object> params = new HashMap<>();
						reportName = "TrialBalanceReport.jrxml";

						List<GxTrialBalanceBean> trialBalances = (List<GxTrialBalanceBean>) entityTable().getItemIds().stream().collect(Collectors.toList());

						trialBalances.add(0, new GxTrialBalanceBean());

						params.put("CompanyName", companyName);
						params.put("ToDate", TRCalendarUtil.dateFormatter.format(monthField.getValue()));

						if (companyLogoPath != null) {
							try {
								params.put("CompanyLogoPath", storage.resolveToURI(companyLogoPath).getRawPath());
							} catch (Exception e) {
								L.warn("Failed to resolve logo file path", e);
							}
						}

						JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(trialBalances);
						params.put("ItemDataSource", dataSource);
						String reportResourcePath = storage.resourcePath("reports", reportName);
						InputStream reportResource = storage.resolve(reportResourcePath);
						JasperReport trialBalanceReport = JasperCompileManager.compileReport(reportResource);

						ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();

						JasperPrint filledReport = JasperFillManager.fillReport(trialBalanceReport, params, dataSource);
						JasperExportManager.exportReportToPdfStream(filledReport, reportOutput);
						return new ByteArrayInputStream(reportOutput.toByteArray());
					} catch (Exception ex) {
						L.warn("Failed to process report", ex);
					}
					return null;
				}

			}, "trial-balance-report" + System.currentTimeMillis() + ".pdf"));
		});

		toolbar.addComponents(monthField, generateReportButton);
	}

	@Override
	protected void applyRendererForColumn(TableColumn column) {
		String id = column.getPropertyId();
		if (id.equals("accountName"))
			column.setHeader("Account");
		if (id.equals("debit") || id.equals("credit")) {
			column.setAlignment(Align.RIGHT);
			column.setWidth(150);
		}
		super.applyRendererForColumn(column);
	}

}
