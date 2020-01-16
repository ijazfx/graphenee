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
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.UI;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.enums.AccountType;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxBalanceSheetBean;
import io.graphenee.core.model.bean.GxBalanceSheetReportBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.storage.FileStorage;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.AbstractEntityTablePanel.TableColumn;
import io.graphenee.vaadin.ResourcePreviewPanel;
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
public class GxBalanceSheetPanel extends MVerticalLayout {

	public static final Logger L = LoggerFactory.getLogger(GxBalanceSheetBean.class);

	@Autowired
	GxDataService dataService;

	@Autowired
	FileStorage storage;

	@Autowired
	GxAccountingDataService accountingDataService;

	private GxNamespaceBean namespaceBean;

	private DateField toDateField;

	private MVerticalLayout mainPanel;

	private String companyName;

	private MButton generateReportButton;

	private List<GxBalanceSheetBean> assets = new ArrayList<>();
	private List<GxBalanceSheetBean> liabilities = new ArrayList<>();
	private List<GxBalanceSheetBean> equity = new ArrayList<>();

	private Double equityTotalAmount;
	private Double netIncome;
	private Double liabilitiesTotalAmount;
	private Double assetsTotalAmount;
	private String companyLogoPath;

	public GxBalanceSheetPanel() {
		setWidth(100, Unit.PERCENTAGE);
		setMargin(false);
	}

	private MHorizontalLayout buildToolbar() {
		MHorizontalLayout layout = new MHorizontalLayout().withStyleName("toolbar").withDefaultComponentAlignment(Alignment.BOTTOM_LEFT).withWidthUndefined().withMargin(false)
				.withSpacing(true);
		toDateField = new DateField("Upto");
		toDateField.setResolution(Resolution.MONTH);
		toDateField.setDateFormat(TRCalendarUtil.dateFormatter.toPattern());
		toDateField.setWidth("100px");

		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);

		toDateField.setValue(currentDate);

		toDateField.addValueChangeListener(event -> {
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
						reportName = "BalanceSheetReport.jrxml";

						params.put("CompanyName", companyName);
						params.put("ToDate", TRCalendarUtil.dateFormatter.format(toDateField.getValue()));

						if (companyLogoPath != null) {
							try {
								params.put("CompanyLogoPath", storage.resolveToURI(companyLogoPath).getRawPath());
							} catch (Exception e) {
								L.warn("Failed to resolve logo file path", e);
							}
						}

						List<GxBalanceSheetReportBean> assetDataSourceList = new ArrayList<>();

						generateAssetMap().forEach((parentAccount, childAccounts) -> {
							GxBalanceSheetReportBean bean = new GxBalanceSheetReportBean();
							bean.setParentAccount(parentAccount.orElse("Asset"));
							bean.setBalanceSheetList(childAccounts);
							assetDataSourceList.add(bean);
						});

						JRBeanCollectionDataSource assetDataSource = new JRBeanCollectionDataSource(assetDataSourceList);
						params.put("AssetDataSource", assetDataSource);

						List<GxBalanceSheetReportBean> liabilityDataSourceList = new ArrayList<>();

						generateLiablityMap().forEach((parentAccount, childAccounts) -> {
							GxBalanceSheetReportBean bean = new GxBalanceSheetReportBean();
							bean.setParentAccount(parentAccount.orElse("Liability"));
							bean.setBalanceSheetList(childAccounts);
							liabilityDataSourceList.add(bean);
						});

						JRBeanCollectionDataSource liabilityDataSource = new JRBeanCollectionDataSource(liabilityDataSourceList);
						params.put("LiabilityDataSource", liabilityDataSource);

						List<GxBalanceSheetReportBean> equityDataSourceList = new ArrayList<>();

						generateEquityMap().forEach((parentAccount, childAccounts) -> {
							GxBalanceSheetReportBean bean = new GxBalanceSheetReportBean();
							bean.setParentAccount(parentAccount.orElse("Equity"));
							bean.setBalanceSheetList(childAccounts);
							equityDataSourceList.add(bean);
						});

						JRBeanCollectionDataSource equityDataSource = new JRBeanCollectionDataSource(equityDataSourceList);
						params.put("EquityDataSource", equityDataSource);
						params.put("AssetsTotalAmount", assetsTotalAmount);
						params.put("LiabilitiesTotalAmount", liabilitiesTotalAmount);
						params.put("EquityTotalAmount", equityTotalAmount);
						params.put("NetIncome", netIncome);

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

			}, "balance-sheet-report" + System.currentTimeMillis() + ".pdf"));
		});

		layout.addComponents(toDateField, generateReportButton);
		VaadinUtils.applyStyleRecursively(layout, "small");

		return layout;
	}

	private MVerticalLayout buildBody() {
		MVerticalLayout mainLayout = new MVerticalLayout().withSizeUndefined().withFullWidth().withMargin(false).withSpacing(true);
		MHorizontalLayout body = new MHorizontalLayout().withFullHeight().withFullWidth().withMargin(false).withSpacing(true);

		List<GxBalanceSheetBean> balanceSheetBeans = accountingDataService.findBalanceSheetByDateAndNamespace(new Timestamp(toDateField.getValue().getTime()), namespaceBean);

		netIncome = accountingDataService.findNetIncomeByDateAndNamespace(new Timestamp(toDateField.getValue().getTime()), namespaceBean);

		assets = balanceSheetBeans.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.ASSET.typeCode())).collect(Collectors.toList());

		MVerticalLayout assetLayout = new MVerticalLayout().withSizeUndefined().withFullWidth().withMargin(false).withSpacing(true);

		generateAssetMap().forEach((parentAccount, childAccounts) -> {
			Double childAssetsTotalAmount = childAccounts.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();
			assetLayout.addComponent(constructTable(parentAccount.orElse("Assets"), childAccounts, childAssetsTotalAmount));
		});

		assetsTotalAmount = assets.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();

		MVerticalLayout liabilityAndEquityLayout = new MVerticalLayout().withSizeUndefined().withFullWidth().withMargin(false);

		liabilities = balanceSheetBeans.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.LIABILITY.typeCode())).collect(Collectors.toList());

		generateLiablityMap().forEach((parentAccount, childAccounts) -> {
			Double childLiabilitiesTotalAmount = childAccounts.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();
			liabilityAndEquityLayout.addComponent(constructTable(parentAccount.orElse("Liabilities"), childAccounts, childLiabilitiesTotalAmount));
		});

		liabilitiesTotalAmount = liabilities.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();

		equity = balanceSheetBeans.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.EQUITY.typeCode())).collect(Collectors.toList());

		generateEquityMap().forEach((parentAccount, childAccounts) -> {
			Double childEquitiesTotalAmount = childAccounts.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();
			liabilityAndEquityLayout.addComponent(constructTable(parentAccount.orElse("Equity"), childAccounts, childEquitiesTotalAmount));
		});

		equityTotalAmount = equity.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();

		liabilityAndEquityLayout.addComponents(constructFooterTable("Net Income", netIncome), constructFooterTable("Total Equity", netIncome + equityTotalAmount));

		body.addComponents(assetLayout, liabilityAndEquityLayout);

		MHorizontalLayout footer = new MHorizontalLayout().withFullHeight().withFullWidth().withMargin(false).withSpacing(true);

		footer.addComponents(constructFooterTable("Total", assetsTotalAmount),
				constructFooterTable("Total Liabilities & Equity", equityTotalAmount + liabilitiesTotalAmount + netIncome));

		mainLayout.addComponents(body, footer);

		return mainLayout;
	}

	private Map<Optional<String>, List<GxBalanceSheetBean>> generateAssetMap() {
		return assets.stream().collect(Collectors.groupingBy(entity -> Optional.ofNullable(entity.getParentAccountName())));
	}

	private Map<Optional<String>, List<GxBalanceSheetBean>> generateLiablityMap() {
		return liabilities.stream().collect(Collectors.groupingBy(entity -> Optional.ofNullable(entity.getParentAccountName())));
	}

	private Map<Optional<String>, List<GxBalanceSheetBean>> generateEquityMap() {
		return equity.stream().collect(Collectors.groupingBy(entity -> Optional.ofNullable(entity.getParentAccountName())));
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

	private Component constructTable(String tableCaption, List<GxBalanceSheetBean> balanceSheetList, Double totalAmount) {

		MTable<GxBalanceSheetBean> table = new MTable<GxBalanceSheetBean>().withCaption(tableCaption);
		BeanItemContainer<GxBalanceSheetBean> dataSource = new BeanItemContainer<>(GxBalanceSheetBean.class);
		dataSource.addAll(balanceSheetList);
		table.setContainerDataSource(dataSource);

		table.setResponsive(true);
		table.setPageLength(table.size());
		table.setWidth(100, Unit.PERCENTAGE);
		table.setColumnHeader("accountName", "Account");
		table.setColumnHeader("amount", "Amount");
		table.setVisibleColumns(new Object[] { "accountName", "amount" });
		table.setFooterVisible(true);
		table.setColumnFooter("accountName", "Total");
		table.setColumnFooter("amount", String.format("%.2f", totalAmount));
		for (Object o : table.getVisibleColumns()) {
			if (o != null) {
				applyRendereForColumn(new TableColumn(table, o.toString()));
			}
		}

		return table;
	}

	private void applyRendereForColumn(TableColumn column) {
		String id = column.getPropertyId();
		if (id.equals("amount")) {
			column.setAlignment(Align.RIGHT);
			column.setWidth(150);
		}
	}

	public void initializeWithEntity(GxNamespaceBean namespaceBean, String companyName, String companyLogoPath) {
		this.namespaceBean = namespaceBean;
		this.companyName = companyName;
		this.companyLogoPath = companyLogoPath;
		addComponent(buildToolbar());
		mainPanel = buildBody();
		addComponent(mainPanel);
	}

	private GxBalanceSheetPanel refresh() {
		UI.getCurrent().access(() -> {
			removeComponent(mainPanel);
			mainPanel = buildBody();
			addComponent(mainPanel);
			UI.getCurrent().push();
		});
		return this;
	}
}
