package io.graphenee.accounting.vaadin;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.BeanItemContainer;
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
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.AbstractEntityTablePanel.TableColumn;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxBalanceSheetPanel extends MVerticalLayout {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	private GxNamespaceBean namespaceBean;

	private DateField toDateField;

	private MVerticalLayout mainPanel;

	public GxBalanceSheetPanel() {
		setWidth(100, Unit.PERCENTAGE);
	}

	private MHorizontalLayout buildToolbar() {
		MHorizontalLayout layout = new MHorizontalLayout().withStyleName("toolbar").withDefaultComponentAlignment(Alignment.BOTTOM_LEFT).withFullWidth().withMargin(false)
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

		layout.addComponents(toDateField);
		return layout;

	}

	private MVerticalLayout buildBody() {
		MVerticalLayout mainLayout = new MVerticalLayout().withSizeUndefined().withFullWidth().withMargin(false).withSpacing(true);
		MHorizontalLayout body = new MHorizontalLayout().withFullHeight().withFullWidth().withMargin(false).withSpacing(true);

		List<GxBalanceSheetBean> balanceSheetBeans = accountingDataService.findBalanceSheetByDateAndNamespace(new Timestamp(toDateField.getValue().getTime()), namespaceBean);

		Double netIncome = accountingDataService.findNetIncomeByDateAndNamespace(new Timestamp(toDateField.getValue().getTime()), namespaceBean);

		List<GxBalanceSheetBean> assets = balanceSheetBeans.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.ASSET.typeCode()))
				.collect(Collectors.toList());

		MVerticalLayout assetLayout = new MVerticalLayout().withSizeUndefined().withFullWidth().withMargin(false).withSpacing(true);

		Map<Optional<String>, List<GxBalanceSheetBean>> assetMap = assets.stream().collect(Collectors.groupingBy(entity -> Optional.ofNullable(entity.getParentAccountName())));
		assetMap.forEach((parentAccount, childAccounts) -> {
			Double childAssetsTotalAmount = childAccounts.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();
			assetLayout.addComponent(constructTable(parentAccount.orElse("Assets"), childAccounts, childAssetsTotalAmount));
		});

		Double assetsTotalAmount = assets.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();

		MVerticalLayout liabilityAndEquityLayout = new MVerticalLayout().withSizeUndefined().withFullWidth().withMargin(false);

		List<GxBalanceSheetBean> liabilities = balanceSheetBeans.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.LIABILITY.typeCode()))
				.collect(Collectors.toList());

		Map<Optional<String>, List<GxBalanceSheetBean>> liabilityMap = liabilities.stream()
				.collect(Collectors.groupingBy(entity -> Optional.ofNullable(entity.getParentAccountName())));
		liabilityMap.forEach((parentAccount, childAccounts) -> {
			Double childLiabilitiesTotalAmount = childAccounts.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();
			liabilityAndEquityLayout.addComponent(constructTable(parentAccount.orElse("Liabilities"), childAccounts, childLiabilitiesTotalAmount));
		});

		Double liabilitiesTotalAmount = liabilities.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();

		List<GxBalanceSheetBean> equity = balanceSheetBeans.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.EQUITY.typeCode()))
				.collect(Collectors.toList());

		Map<Optional<String>, List<GxBalanceSheetBean>> equityMap = equity.stream().collect(Collectors.groupingBy(entity -> Optional.ofNullable(entity.getParentAccountName())));
		equityMap.forEach((parentAccount, childAccounts) -> {
			Double childEquitiesTotalAmount = childAccounts.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();
			liabilityAndEquityLayout.addComponent(constructTable(parentAccount.orElse("Equity"), childAccounts, childEquitiesTotalAmount));
		});

		Double equityTotalAmount = equity.stream().mapToDouble(GxBalanceSheetBean::getAmount).sum();

		liabilityAndEquityLayout.addComponents(constructFooterTable("Net Income", netIncome), constructFooterTable("Total Equity", netIncome + equityTotalAmount));

		body.addComponents(assetLayout, liabilityAndEquityLayout);

		MHorizontalLayout footer = new MHorizontalLayout().withFullHeight().withFullWidth().withMargin(false).withSpacing(true);

		footer.addComponents(constructFooterTable("Total", assetsTotalAmount),
				constructFooterTable("Total Liabilities & Equity", equityTotalAmount + liabilitiesTotalAmount + netIncome));

		mainLayout.addComponents(body, footer);

		return mainLayout;
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

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
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
