package io.graphenee.accounting.vaadin;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxGeneralLedgerBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.AbstractEntityTablePanel.TableColumn;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxGeneralLedgerPanel extends MVerticalLayout {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	private GxNamespaceBean namespaceBean;
	private GxAccountBean selectedAccount;

	private ComboBox accountComboBox;

	private DateField fromDateField;
	private DateField toDateField;

	private CheckBox displaySubAccountsCheckBox;

	private MVerticalLayout mainPanel;

	public GxGeneralLedgerPanel() {
		setWidth(100, Unit.PERCENTAGE);
	}

	private MHorizontalLayout buildToolbar() {
		MHorizontalLayout layout = new MHorizontalLayout().withStyleName("toolbar").withDefaultComponentAlignment(Alignment.BOTTOM_LEFT).withFullWidth().withMargin(false)
				.withSpacing(true);
		accountComboBox = new ComboBox("Account");
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

		layout.addComponents(accountComboBox, displaySubAccountsCheckBox, fromDateField, toDateField);
		layout.setComponentAlignment(fromDateField, Alignment.MIDDLE_RIGHT);
		layout.setExpandRatio(fromDateField, 1);
		return layout;

	}

	private MVerticalLayout buildBody() {
		MVerticalLayout mainLayout = new MVerticalLayout().withSizeUndefined().withFullWidth().withMargin(false).withSpacing(true);
		if (selectedAccount != null) {
			mainLayout.addComponent(constructTable(findLedgerByAccount(), null));
		} else {
			Map<String, List<GxGeneralLedgerBean>> ledgerMap = accountingDataService.findAllByNamespaceAndDateRangeOrderByTransactionDateAsc(namespaceBean,
					new Timestamp(fromDateField.getValue().getTime()), new Timestamp(toDateField.getValue().getTime()));
			ledgerMap.forEach((accountName, entities) -> {
				mainLayout.addComponent(constructTable(entities, accountName));
			});
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
		table.setVisibleColumns("transactionDate", "accountName", "description", "debit", "credit", "balance");

		for (Object o : table.getVisibleColumns()) {
			if (o != null) {
				applyRendereForColumn(new TableColumn(table, o.toString()));
			}
		}

		return table;
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

	private List<GxGeneralLedgerBean> findLedgerByAccount() {
		boolean displayChildAccounts = displaySubAccountsCheckBox.getValue();
		if (selectedAccount != null) {
			if (displayChildAccounts) {
				return accountingDataService.findAllByAccountAndChildAccountsAndNamespaceAndDateRangeOrderByTransactionDateAsc(selectedAccount, namespaceBean,
						new Timestamp(fromDateField.getValue().getTime()), new Timestamp(toDateField.getValue().getTime()));
			}
			return accountingDataService.findAllByAccountAndNamespaceAndDateRangeOrderByTransactionDateAsc(selectedAccount, namespaceBean,
					new Timestamp(fromDateField.getValue().getTime()), new Timestamp(toDateField.getValue().getTime()));

		} else
			return Collections.emptyList();
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
		addComponent(buildToolbar());
		mainPanel = buildBody();
		addComponent(mainPanel);
	}

	private GxGeneralLedgerPanel refresh() {
		UI.getCurrent().access(() -> {
			removeComponent(mainPanel);
			mainPanel = buildBody();
			addComponent(mainPanel);
			UI.getCurrent().push();
		});
		return this;
	}

}
