package io.graphenee.accounting.vaadin;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.button.MButton;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.enums.Timeframe;
import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxVoucherBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxVoucherListPanel extends AbstractEntityListPanel<GxVoucherBean> {

	private GxNamespaceBean namespaceBean;

	private ComboBox namespaceComboBox;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	@Autowired
	GxVoucherForm form;

	private DateField fromDateField;
	private DateField toDateField;

	private MButton thisMonthButton;

	private MButton thisYearButton;

	private MButton lastYearButton;

	private MButton pastButton;

	private Timeframe fetchMode = Timeframe.ThisMonth;

	public GxVoucherListPanel() {
		super(GxVoucherBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxVoucherBean entity) {
		accountingDataService.createOrUpdate(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxVoucherBean entity) {
		accountingDataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxVoucherBean> fetchEntities() {
		Timestamp fromDate = fetchMode != Timeframe.Past ? fetchMode.getFromDate() : new Timestamp(fromDateField.getValue().getTime());
		Timestamp toDate = fetchMode != Timeframe.Past ? fetchMode.getToDate() : new Timestamp(toDateField.getValue().getTime());

		if (namespaceBean != null)
			return accountingDataService.findAllVouchersByNamespaceAndDateRangeOrderByVoucherDateDesc(namespaceBean, fromDate, toDate);
		return accountingDataService.findAllVouchersByDateRangeOrderByVoucherDateAsc(fromDate, toDate);
	}

	@Override
	protected <F> List<GxVoucherBean> fetchEntities(F filter) {
		Timestamp fromDate = fetchMode != Timeframe.Past ? fetchMode.getFromDate() : new Timestamp(fromDateField.getValue().getTime());
		Timestamp toDate = fetchMode != Timeframe.Past ? fetchMode.getToDate() : new Timestamp(toDateField.getValue().getTime());
		if (filter instanceof GxNamespaceBean)
			return accountingDataService.findAllVouchersByNamespaceAndDateRangeOrderByVoucherDateDesc(namespaceBean, fromDate, toDate);
		return super.fetchEntities(filter);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "voucherNumber", "description", "voucherDate", "totalAmount" };
	}

	@Override
	protected TRAbstractForm<GxVoucherBean> editorForm() {
		return form;
	}

	@Override
	protected boolean shouldShowDeleteConfirmation() {
		return true;
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
	}

	@Override
	protected void preEdit(GxVoucherBean item) {
		if (item.getOid() == null) {
			GxNamespaceBean selectedNamespaceBean = namespaceBean != null ? namespaceBean : (GxNamespaceBean) namespaceComboBox.getValue();
			if (selectedNamespaceBean != null) {
				item.setGxNamespaceBeanFault(BeanFault.beanFault(selectedNamespaceBean.getOid(), selectedNamespaceBean));
			}
		}
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
		namespaceComboBox.setVisible(namespaceBean == null);
	}

	@Override
	protected void postBuild() {
		super.postBuild();
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

		toolbar.addComponent(dateRangeLayout);
		toolbar.setComponentAlignment(dateRangeLayout, Alignment.BOTTOM_RIGHT);
		toolbar.setExpandRatio(dateRangeLayout, 1);

		toDateField = new DateField("To Date");
		toDateField.setDateFormat(TRCalendarUtil.dateFormatter.toPattern());

		fromDateField = new DateField("From Date");
		fromDateField.setDateFormat(TRCalendarUtil.dateFormatter.toPattern());

		Date currentDate = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, -1);

		Date dateBeforeOneMonth = cal.getTime();

		toDateField.setValue(currentDate);
		fromDateField.setValue(dateBeforeOneMonth);

		fromDateField.addValueChangeListener(event -> {
			refresh();
		});

		toDateField.addValueChangeListener(event -> {
			refresh();
		});

		toggleEnable(true);
		thisMonthButton.setEnabled(false);

		toggleEnableDateFields(false);

		toolbar.addComponents(fromDateField, toDateField);
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

}
