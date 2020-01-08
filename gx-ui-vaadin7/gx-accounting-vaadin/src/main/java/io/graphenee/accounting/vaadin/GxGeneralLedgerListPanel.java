package io.graphenee.accounting.vaadin;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxGeneralLedgerBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxGeneralLedgerListPanel extends AbstractEntityListPanel<GxGeneralLedgerBean> {

	private GxNamespaceBean namespaceBean;
	private GxAccountBean selectedAccount;

	private ComboBox namespaceComboBox;
	private ComboBox accountComboBox;

	private DateField fromDateField;
	private DateField toDateField;

	private CheckBox displaySubAccountsCheckBox;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	public GxGeneralLedgerListPanel() {
		super(GxGeneralLedgerBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxGeneralLedgerBean entity) {
		return false;
	}

	@Override
	protected boolean onDeleteEntity(GxGeneralLedgerBean entity) {
		return false;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxGeneralLedgerBean> fetchEntities() {
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

	@Override
	protected <F> List<GxGeneralLedgerBean> fetchEntities(F filter) {
		boolean displayChildAccounts = displaySubAccountsCheckBox.getValue();
		if (filter instanceof GxNamespaceBean) {
			if (selectedAccount != null) {
				if (displayChildAccounts) {
					return accountingDataService.findAllByAccountAndChildAccountsAndNamespaceAndDateRangeOrderByTransactionDateAsc(selectedAccount, namespaceBean,
							new Timestamp(fromDateField.getValue().getTime()), new Timestamp(toDateField.getValue().getTime()));
				}
				return accountingDataService.findAllByAccountAndNamespaceAndDateRangeOrderByTransactionDateAsc(selectedAccount, (GxNamespaceBean) filter,
						new Timestamp(fromDateField.getValue().getTime()), new Timestamp(toDateField.getValue().getTime()));
			} else
				return Collections.emptyList();
		}
		return super.fetchEntities(filter);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "transactionDateTime", "accountName", "description", "debit", "credit", "balance" };
	}

	@Override
	protected TRAbstractForm<GxGeneralLedgerBean> editorForm() {
		return null;
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
		namespaceComboBox.setVisible(namespaceBean == null);
	}

	@Override
	protected void addButtonsToSecondaryToolbar(AbstractOrderedLayout toolbar) {
		namespaceComboBox = new ComboBox("Namespace");
		namespaceComboBox.setTextInputAllowed(false);
		namespaceComboBox.addItems(dataService.findNamespace());
		namespaceComboBox.addValueChangeListener(event -> {
			refresh(event.getProperty().getValue());
		});
		toolbar.addComponent(namespaceComboBox);
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
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

		accountComboBox = new ComboBox("Account");
		accountComboBox.setNullSelectionAllowed(false);
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

		toolbar.addComponents(accountComboBox, displaySubAccountsCheckBox, fromDateField, toDateField);
		toolbar.setComponentAlignment(fromDateField, Alignment.MIDDLE_RIGHT);
		toolbar.setExpandRatio(fromDateField, 1);
	}

	@Override
	protected Alignment alignmentForProperty(String propertyId) {
		if (propertyId != null && propertyId.matches("debit|credit|balance"))
			return Alignment.MIDDLE_RIGHT;
		return super.alignmentForProperty(propertyId);
	}

}
