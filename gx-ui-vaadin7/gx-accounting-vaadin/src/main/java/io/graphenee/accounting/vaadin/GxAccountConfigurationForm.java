package io.graphenee.accounting.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.bean.GxAccountConfigurationBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.DateToTimestampConverter;
import io.graphenee.vaadin.ui.GxNotification;

@SuppressWarnings("serial")
@SpringComponent
public class GxAccountConfigurationForm extends TRAbstractForm<GxAccountConfigurationBean> {

	@Autowired
	GxAccountingDataService accountingDataService;

	MDateField fiscalYearStart;
	MTextField voucherNumber;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return null;
	}

	@Override
	protected void addFieldsToForm(FormLayout form) {
		fiscalYearStart = new MDateField("Fiscal Year Start Date");
		fiscalYearStart.setRequired(true);
		fiscalYearStart.setDateFormat(TRCalendarUtil.dateFormatter.toPattern());
		fiscalYearStart.setConverter(new DateToTimestampConverter());

		voucherNumber = new MTextField("Last Voucher Number");
		voucherNumber.setConverter(new StringToIntegerConverter());
		voucherNumber.setRequired(true);

		form.addComponents(fiscalYearStart, voucherNumber);
	}

	@Override
	protected boolean shouldShowDismissButton() {
		return false;
	}

	@Override
	protected void addButtonsToFooter(HorizontalLayout footer) {
		MButton closeYearButton = new MButton("Close Year").withListener(event -> {
			GxAccountConfigurationBean configurationBean = getEntity();
			if (configurationBean != null) {
				if (TRCalendarUtil.getCurrentTimeStamp().after(configurationBean.getFiscalYearEnd())) {
					ConfirmDialog.show(UI.getCurrent(), "Are you sure to close fiscal year " + configurationBean.getFormattedFiscalYear() + " ?", e -> {
						if (e.isConfirmed()) {
							if (accountingDataService.closeYear(configurationBean.getGxNamespaceBeanFault().getBean())) {
								GxNotification.tray("Fiscal year " + configurationBean.getFormattedFiscalYear() + " closed successfully.").show(Page.getCurrent());
							}
						}
					});
				} else {
					GxNotification.tray("You cannot close year till " + TRCalendarUtil.getFormattedDate(configurationBean.getFiscalYearEnd()) + " .").show(Page.getCurrent());
				}
			} else {
				GxNotification.tray("Account configuration is not configured.").show(Page.getCurrent());
			}
		});

		closeYearButton.setStyleName(ValoTheme.BUTTON_DANGER);
		footer.addComponent(closeYearButton);
		footer.setSizeFull();
		footer.addComponentAsFirst(closeYearButton);
		footer.setComponentAlignment(closeYearButton, Alignment.MIDDLE_LEFT);
	}

}
