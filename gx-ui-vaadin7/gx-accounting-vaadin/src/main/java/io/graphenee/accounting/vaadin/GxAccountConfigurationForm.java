package io.graphenee.accounting.vaadin;

import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.bean.GxAccountConfigurationBean;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
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

		voucherNumber = new MTextField("Voucher Number");
		voucherNumber.setConverter(new StringToIntegerConverter());
		voucherNumber.setRequired(true);

		form.addComponents(fiscalYearStart, voucherNumber);
	}

	@Override
	protected boolean shouldShowDismissButton() {
		return false;
	}

}
