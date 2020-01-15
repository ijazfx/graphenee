package io.graphenee.accounting.vaadin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.enums.Timeframe;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxGeneralLedgerBean;
import io.graphenee.core.model.bean.GxGeneralLedgerReportBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxVoucherBean;
import io.graphenee.core.storage.FileStorage;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.AbstractEntityTablePanel.TableColumn;
import io.graphenee.vaadin.ResourcePreviewPanel;
import io.graphenee.vaadin.event.TRItemClickListener;
import io.graphenee.vaadin.util.VaadinUtils;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxGeneralLedgerPanel extends VerticalSplitPanel {

	public static final Logger L = LoggerFactory.getLogger(GxGeneralLedgerBean.class);

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	@Autowired
	GxJournalVoucherForm form;

	@Autowired
	FileStorage storage;

	private GxNamespaceBean namespaceBean;
	private GxAccountBean selectedAccount;

	private ComboBox accountComboBox;

	private DateField fromDateField;
	private DateField toDateField;

	private MButton thisMonthButton;

	private MButton thisYearButton;

	private MButton lastYearButton;

	private MButton pastButton;

	private Timeframe fetchMode = Timeframe.ThisMonth;

	private CheckBox displaySubAccountsCheckBox;

	private MButton generateReportButton;

	private MVerticalLayout mainPanel;

	private String companyName;

	private Map<String, List<GxGeneralLedgerBean>> ledgerMap = new HashMap<String, List<GxGeneralLedgerBean>>();

	private String companyLogoPath = null;

	public GxGeneralLedgerPanel() {
		setWidth(100, Unit.PERCENTAGE);
		setLocked(true);
	}

	private MHorizontalLayout buildToolbar() {
		MHorizontalLayout layout = new MHorizontalLayout().withStyleName("toolbar").withDefaultComponentAlignment(Alignment.BOTTOM_LEFT).withFullWidth().withMargin(false)
				.withSpacing(true);
		accountComboBox = new ComboBox();
		accountComboBox.setInputPrompt("Select Account");
		accountComboBox.setWidth("200px");
		List<GxAccountBean> accountBeans = null;
		if (namespaceBean != null)
			accountBeans = accountingDataService.findAllAccountsByNamespace(namespaceBean);
		else
			accountBeans = accountingDataService.findAllAccounts();
		accountComboBox.addItems(accountBeans);
		accountComboBox.addValueChangeListener(event -> {
			this.selectedAccount = (GxAccountBean) event.getProperty().getValue();
			refresh();
		});

		displaySubAccountsCheckBox = new MCheckBox("Display Sub Accounts", false);
		displaySubAccountsCheckBox.addValueChangeListener(event -> {
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
						reportName = "GeneralLedgerReport.jrxml";

						params.put("CompanyName", companyName);
						params.put("FromDate", TRCalendarUtil.dateFormatter.format(fromDateField.getValue()));
						params.put("ToDate", TRCalendarUtil.dateFormatter.format(toDateField.getValue()));

						if (companyLogoPath != null) {
							try {
								params.put("CompanyLogoPath", storage.resolveToURI(companyLogoPath).getRawPath());
							} catch (Exception e) {
								L.warn("Failed to resolve logo file path", e);
							}
						}

						List<GxGeneralLedgerReportBean> legderDataSourceList = new ArrayList<>();

						ledgerMap.forEach((parentAccount, childAccounts) -> {
							GxGeneralLedgerReportBean bean = new GxGeneralLedgerReportBean();
							bean.setParentAccount(parentAccount);
							bean.setGeneralLedgerList(childAccounts);
							legderDataSourceList.add(bean);
						});

						JRBeanCollectionDataSource ledgerDataSource = new JRBeanCollectionDataSource(legderDataSourceList);
						params.put("LedgerDataSource", ledgerDataSource);

						String reportResourcePath = storage.resourcePath("reports", reportName);
						InputStream reportResource = storage.resolve(reportResourcePath);
						JasperReport balanceSheetReport = JasperCompileManager.compileReport(reportResource);

						ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();

						JasperPrint filledReport = JasperFillManager.fillReport(balanceSheetReport, params, new JREmptyDataSource());
						JasperExportManager.exportReportToPdfStream(filledReport, reportOutput);
						return new ByteArrayInputStream(reportOutput.toByteArray());
					} catch (Exception ex) {
						L.warn("Failed to process report", ex);
					}
					return null;
				}

			}, "general-ledger-report" + System.currentTimeMillis() + ".pdf"));
		});

		MHorizontalLayout accountAndSubAccountLayout = new MHorizontalLayout();
		accountAndSubAccountLayout.addComponents(accountComboBox, displaySubAccountsCheckBox, generateReportButton);
		accountAndSubAccountLayout.setComponentAlignment(displaySubAccountsCheckBox, Alignment.MIDDLE_CENTER);

		thisMonthButton = new MButton("This Month");
		thisYearButton = new MButton("This Year");
		lastYearButton = new MButton("Last Year");
		pastButton = new MButton("Past");

		thisMonthButton.addClickListener(event -> {
			toggleEnable(true);
			toggleEnableDateFields(false);
			thisMonthButton.setEnabled(false);
			fetchMode = Timeframe.ThisMonth;
			refresh();
		});
		thisYearButton.addClickListener(event -> {
			toggleEnable(true);
			toggleEnableDateFields(false);
			thisYearButton.setEnabled(false);
			fetchMode = Timeframe.ThisYear;
			refresh();
		});
		lastYearButton.addClickListener(event -> {
			toggleEnable(true);
			toggleEnableDateFields(false);
			lastYearButton.setEnabled(false);
			fetchMode = Timeframe.LastYear;
			refresh();
		});
		pastButton.addClickListener(event -> {
			toggleEnable(true);
			toggleEnableDateFields(true);
			pastButton.setEnabled(false);
			fetchMode = Timeframe.Past;
			refresh();
		});

		CssLayout dateRangeLayout = new CssLayout(thisMonthButton, thisYearButton, lastYearButton, pastButton);
		dateRangeLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		fromDateField = new DateField("From Date");
		toDateField = new DateField("To Date");
		fromDateField.setDateFormat(TRCalendarUtil.dateFormatter.toPattern());
		toDateField.setDateFormat(TRCalendarUtil.dateFormatter.toPattern());

		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, -1);
		Date dateBefore1Month = cal.getTime();
		toDateField.setValue(currentDate);
		fromDateField.setValue(dateBefore1Month);

		fromDateField.addValueChangeListener(event -> {
			refresh();
		});
		toDateField.addValueChangeListener(event -> {
			refresh();
		});

		layout.addComponents(accountAndSubAccountLayout, dateRangeLayout, fromDateField, toDateField);
		layout.setComponentAlignment(dateRangeLayout, Alignment.BOTTOM_RIGHT);
		layout.setExpandRatio(dateRangeLayout, 1);

		toggleEnable(true);
		thisMonthButton.setEnabled(false);

		toggleEnableDateFields(false);

		VaadinUtils.applyStyleRecursively(layout, "small");

		return layout;

	}

	private void toggleEnable(boolean value) {
		thisMonthButton.setEnabled(value);
		thisYearButton.setEnabled(value);
		lastYearButton.setEnabled(value);
		pastButton.setEnabled(value);
	}

	private void toggleEnableDateFields(boolean value) {
		toDateField.setEnabled(value);
		fromDateField.setEnabled(value);
	}

	private MVerticalLayout buildBody() {
		MVerticalLayout mainLayout = new MVerticalLayout().withSizeUndefined().withFullWidth().withMargin(false).withSpacing(true);
		Timestamp fromDate = fetchMode != Timeframe.Past ? fetchMode.getFromDate() : new Timestamp(fromDateField.getValue().getTime());
		Timestamp toDate = fetchMode != Timeframe.Past ? fetchMode.getToDate() : new Timestamp(toDateField.getValue().getTime());
		if (selectedAccount != null) {
			Boolean displaySubAccount = displaySubAccountsCheckBox.getValue();
			if (displaySubAccount) {
				ledgerMap = accountingDataService.findAllByAccountAndChildAccountsAndNamespaceAndDateRangeGroupByAccountOrderByTransactionDateAsc(selectedAccount, namespaceBean,
						fromDate, toDate);
				Double totalBalance = 0.0;
				for (Map.Entry<String, List<GxGeneralLedgerBean>> entry : ledgerMap.entrySet()) {
					mainLayout.addComponent(constructTable(entry.getValue(), entry.getKey()));
					if (entry.getValue().size() > 0)
						totalBalance = entry.getValue().get(entry.getValue().size() - 1).getBalance();
				}

				mainLayout.addComponent(constructFooterTable(selectedAccount.getAccountName(), totalBalance));
			} else {
				findLedgerByAccount(fromDate, toDate).forEach((accountName, entities) -> {
					mainLayout.addComponent(constructTable(entities, null));
				});
			}
		} else {
			ledgerMap = accountingDataService.findAllByNamespaceAndDateRangeOrderByTransactionDateAsc(namespaceBean, fromDate, toDate);
			ledgerMap.forEach((accountName, entities) -> {
				mainLayout.addComponent(constructTable(entities, accountName));
			});
			if (ledgerMap.isEmpty()) {
				mainLayout.addComponent(new MLabel("No data available"));
			}
		}
		return mainLayout;
	}

	private Component constructTable(List<GxGeneralLedgerBean> entities, String caption) {
		MTable<GxGeneralLedgerBean> table = new MTable<GxGeneralLedgerBean>() {
			@Override
			protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
				String id = (String) colId;
				if ("transactionDate".equals(id)) {
					return TRCalendarUtil.getFormattedDate((Date) property.getValue());
				}
				return super.formatPropertyValue(rowId, colId, property);
			}
		};
		table.setCaption(caption);
		BeanItemContainer<GxGeneralLedgerBean> dataSource = new BeanItemContainer<>(GxGeneralLedgerBean.class);
		dataSource.addAll(entities);
		table.setContainerDataSource(dataSource);

		table.setResponsive(true);
		table.setPageLength(table.size());
		table.setWidth(100, Unit.PERCENTAGE);
		table.setColumnHeader("transactionDate", "Transaction Date");
		table.setColumnHeader("accountName", "Account");
		table.setColumnHeader("description", "Description");
		table.setColumnHeader("debit", "Debit");
		table.setColumnHeader("credit", "Credit");
		table.setColumnHeader("balance", "Balance");
		table.setVisibleColumns("transactionDate", "description", "debit", "credit", "balance");

		for (Object o : table.getVisibleColumns()) {
			if (o != null) {
				applyRendereForColumn(new TableColumn(table, o.toString()));
			}
		}

		table.setSelectable(true);

		table.addItemClickListener(new TRItemClickListener() {

			@Override
			public void onItemClick(ItemClickEvent event) {
				if (event.getPropertyId() != null) {
					BeanItem<GxGeneralLedgerBean> item = dataSource.getItem(event.getItemId());
					if (item != null) {
						GxVoucherBean voucher = accountingDataService.findByOidAndNamespace(item.getBean().getOidVoucher(), namespaceBean);
						if (voucher != null) {
							form.setEntity(GxVoucherBean.class, voucher);
							form.transactionTablePanel.hideToolbar();
							form.build().openInModalPopup();
						}
					}
				}
			}
		});

		return table;
	}

	private Component constructFooterTable(String title, Double amount) {
		MTable<String> footerTable = new MTable<String>();
		footerTable.addContainerProperty(title, String.class, null);
		footerTable.addContainerProperty("Amount", String.class, null);
		footerTable.addItem(new Object[] { title, String.format("%.2f", amount) }, 1);
		footerTable.setResponsive(true);
		footerTable.setPageLength(footerTable.size());
		footerTable.setWidth(100, Unit.PERCENTAGE);
		footerTable.setColumnWidth("Amount", 150);
		footerTable.setColumnAlignment("Amount", Align.RIGHT);
		footerTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		return footerTable;
	}

	private void applyRendereForColumn(TableColumn column) {
		String id = column.getPropertyId();
		if (id.equals("debit") || id.equals("credit") || id.equals("balance")) {
			column.setAlignment(Align.RIGHT);
			column.setWidth(150);
		}
		if (id.equals("transactionDate")) {
			column.setWidth(150);
		}
		if (id.equals("accountName")) {
			column.setWidth(250);
		}
	}

	private Map<String, List<GxGeneralLedgerBean>> findLedgerByAccount(Timestamp fromDate, Timestamp toDate) {
		if (selectedAccount != null) {
			ledgerMap = accountingDataService.findAllByAccountAndNamespaceAndDateRangeOrderByTransactionDateAscGroupByAccountName(selectedAccount, namespaceBean, fromDate, toDate);
			return ledgerMap;
		} else
			return Collections.emptyMap();
	}

	public void initializeWithEntity(GxNamespaceBean namespaceBean, String companyName, String companyLogoPath) {
		this.namespaceBean = namespaceBean;
		this.companyName = companyName;
		this.companyLogoPath = companyLogoPath;
		setFirstComponent(buildToolbar());
		mainPanel = buildBody();
		setSecondComponent(mainPanel);
		setSplitPosition(8, Unit.PERCENTAGE);
	}

	private GxGeneralLedgerPanel refresh() {
		UI.getCurrent().access(() -> {
			removeComponent(mainPanel);
			mainPanel = buildBody();
			addComponent(mainPanel);
			setSplitPosition(8, Unit.PERCENTAGE);
			UI.getCurrent().push();
		});
		return this;
	}

}
