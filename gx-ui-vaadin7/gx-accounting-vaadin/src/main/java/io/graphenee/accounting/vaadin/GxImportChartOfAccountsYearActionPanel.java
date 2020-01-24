package io.graphenee.accounting.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.accounting.impl.ChartOfAccountsDataProcessor;
import io.graphenee.core.model.bean.GxImportChartOfAccountBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.TRAbstractPanel;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxImportChartOfAccountsYearActionPanel extends TRAbstractPanel {

	ComboBox yearComboBox;

	@Autowired
	GxImportDataForm importDataForm;

	GxImportChartOfAccountBean gxImportChartOfAccountBean;

	@Autowired
	GxAccountingDataService accountingDataService;

	public void initializeWithEntity(GxImportChartOfAccountBean entity) {
		this.gxImportChartOfAccountBean = entity;
	}

	@Override
	protected void addButtonsToFooter(MHorizontalLayout layout) {

	}

	@Override
	protected String panelTitle() {
		return "Chart of Accounts Import Year";
	}

	@Override
	protected void addComponentsToContentLayout(MVerticalLayout layout) {
		MFormLayout formLayout = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		formLayout.setMargin(false);

		BeanItemContainer<Integer> yearBeanItemContainer = new BeanItemContainer<>(Integer.class);

		for (Integer year = 2010; year <= TRCalendarUtil.getYear(TRCalendarUtil.getCurrentTimeStamp()); year++) {
			yearBeanItemContainer.addBean(year);
		}

		yearComboBox = new ComboBox("Select Year");
		yearComboBox.setNullSelectionAllowed(false);
		yearComboBox.setContainerDataSource(yearBeanItemContainer);

		yearComboBox.addValueChangeListener(event -> {
			gxImportChartOfAccountBean.setYear((Integer) event.getProperty().getValue());
			importDataForm.initializeWithDataProcessor(new ChartOfAccountsDataProcessor(accountingDataService, gxImportChartOfAccountBean));
			importDataForm.build();
			importDataForm.openInModalPopup();
		});

		formLayout.addComponent(yearComboBox);
		layout.addComponents(formLayout);
	}

	@Override
	protected String popupHeight() {
		return "160px";
	}

	@Override
	protected String popupWidth() {
		return "380px";
	}

	@Override
	protected boolean isPopupResizable() {
		return false;
	}

}
