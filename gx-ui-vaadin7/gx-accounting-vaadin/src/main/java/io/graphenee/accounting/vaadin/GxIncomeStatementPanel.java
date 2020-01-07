package io.graphenee.accounting.vaadin;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.enums.AccountType;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxIncomeStatementBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.AbstractEntityTablePanel.TableColumn;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxIncomeStatementPanel extends MVerticalLayout {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	private GxNamespaceBean namespaceBean;

	private DateField toDateField;

	private MVerticalLayout mainPanel;

	public GxIncomeStatementPanel() {
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

		List<GxIncomeStatementBean> incomeStatementList = accountingDataService.findIncomeStatementByDateAndNamespace(new Timestamp(toDateField.getValue().getTime()),
				namespaceBean);

		List<GxIncomeStatementBean> incomes = incomeStatementList.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.INCOME.typeCode()))
				.collect(Collectors.toList());
		Double incomesTotalAmount = incomes.stream().mapToDouble(GxIncomeStatementBean::getAmount).sum();

		List<GxIncomeStatementBean> expenses = incomeStatementList.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.EXPENSE.typeCode()))
				.collect(Collectors.toList());
		Double expensesTotalAmount = expenses.stream().mapToDouble(GxIncomeStatementBean::getAmount).sum();

		body.addComponents(constructTable("Income", incomes, incomesTotalAmount), constructTable("Expense", expenses, expensesTotalAmount));

		Component constructFooter = constructFooter(incomesTotalAmount, expensesTotalAmount);
		mainLayout.addComponents(body, constructFooter);
		mainLayout.setComponentAlignment(constructFooter, Alignment.TOP_CENTER);
		return mainLayout;
	}

	private Component constructFooter(Double incomesTotalAmount, Double expensesTotalAmount) {
		MVerticalLayout footer = new MVerticalLayout().withSizeUndefined().withMargin(true).withSpacing(true);

		MLabel incomeStatementLabel = new MLabel().withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
		incomeStatementLabel.setValue("Income Statement = Income - Expense");

		MLabel valueLabel = new MLabel().withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
		valueLabel.setValue(String.format("%.2f", incomesTotalAmount) + " - " + String.format("%.2f", expensesTotalAmount));

		MLabel resultLabel = new MLabel().withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
		resultLabel.setValue(String.format("%.2f", incomesTotalAmount - expensesTotalAmount));

		footer.addComponents(incomeStatementLabel, valueLabel, resultLabel);
		footer.setComponentAlignment(valueLabel, Alignment.TOP_RIGHT);
		footer.setComponentAlignment(resultLabel, Alignment.TOP_RIGHT);
		return footer;

	}

	private Component constructTable(String tableCaption, List<GxIncomeStatementBean> balanceSheetList, Double totalAmount) {

		MTable<GxIncomeStatementBean> table = new MTable<GxIncomeStatementBean>().withCaption(tableCaption);
		BeanItemContainer<GxIncomeStatementBean> dataSource = new BeanItemContainer<>(GxIncomeStatementBean.class);
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

	private GxIncomeStatementPanel refresh() {
		UI.getCurrent().access(() -> {
			removeComponent(mainPanel);
			mainPanel = buildBody();
			addComponent(mainPanel);
			UI.getCurrent().push();
		});
		return this;
	}

}
