package io.graphenee.accounting.vaadin;

import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.bean.GxAccountConfigurationBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.DateToTimestampConverter;

@SuppressWarnings("serial")
@SpringComponent
public class GxAccountConfigurationForm extends TRAbstractForm<GxAccountConfigurationBean> {

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

}
