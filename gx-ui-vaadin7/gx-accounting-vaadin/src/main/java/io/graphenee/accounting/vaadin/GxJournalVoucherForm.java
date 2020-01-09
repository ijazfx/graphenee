package io.graphenee.accounting.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.accounting.vaadin.GxTransactionTablePanel.GxVoucherTableDelegate;
import io.graphenee.core.model.bean.GxVoucherBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.DateToTimestampConverter;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxJournalVoucherForm extends TRAbstractForm<GxVoucherBean> {

	@Autowired
	GxAccountingDataService accountingDataService;

	MLabel voucherNumber;
	MDateField voucherDate;
	MTextArea description;

	@Autowired
	GxTransactionTablePanel transactionTablePanel;

	@Override
	protected Component getFormComponent() {
		MVerticalLayout form = new MVerticalLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		form.setSizeFull();

		MFormLayout headerLayout = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		headerLayout.setMargin(false);

		voucherNumber = new MLabel().withCaption("Voucher Number");

		description = new MTextArea("Description").withRequired(true);
		description.setMaxLength(200);
		description.setRows(2);

		voucherDate = new MDateField("Date Created");
		voucherDate.setRequired(true);
		voucherDate.setDateFormat(TRCalendarUtil.dateFormatter.toPattern());
		voucherDate.setConverter(new DateToTimestampConverter());
		voucherDate.setRangeEnd(TRCalendarUtil.getCurrentDate());

		headerLayout.addComponents(voucherNumber, description, voucherDate);

		transactionTablePanel.setDelegate(new GxVoucherTableDelegate<GxVoucherBean>() {
			@Override
			public void onUpdate(GxVoucherBean entity) {
				GxVoucherTableDelegate.super.onUpdate(entity);
				adjustSaveButtonState();
			}
		});

		form.addComponents(headerLayout, transactionTablePanel.build().withSelectionEnabled(true));
		form.setExpandRatio(transactionTablePanel, 1);

		return form;
	}

	@Override
	public boolean isValid() {
		return super.isValid() && (getEntity().getCreditTotal().equals(getEntity().getDebitTotal()));
	}

	@Override
	protected void preBinding(GxVoucherBean entity) {
		super.preBinding(entity);
		if (entity.getVoucherNumber() == null)
			voucherNumber.setValue("-- Auto-Generated --");
		else
			voucherNumber.setValue(entity.getVoucherNumber());
	}

	@Override
	protected void postBinding(GxVoucherBean entity) {
		if (entity.getOid() != null) {
			voucherDate.setEnabled(false);
		}
		transactionTablePanel.initializeWithEntity(entity);
		transactionTablePanel.refresh();
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Journal Voucher";
	}

	@Override
	protected String popupHeight() {
		return "600px";
	}

	@Override
	protected String popupWidth() {
		return "800px";
	}

}
