package io.graphenee.accounting.vaadin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

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
import io.graphenee.vaadin.AbstractEntityTablePanel;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxTrialBalanceListPanel extends AbstractEntityTablePanel<GxTrialBalanceBean> {

	private GxNamespaceBean namespaceBean;

	private ComboBox namespaceComboBox;

	private DateField monthField;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	List<GxTrialBalanceBean> trialBalance = new ArrayList<GxTrialBalanceBean>();

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
		if (!trialBalance.isEmpty())
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

		monthField = new DateField("Month");
		monthField.setResolution(Resolution.MONTH);

		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);

		monthField.setValue(currentDate);

		monthField.addValueChangeListener(event -> {
			refresh();
		});

		toolbar.addComponent(monthField);
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
