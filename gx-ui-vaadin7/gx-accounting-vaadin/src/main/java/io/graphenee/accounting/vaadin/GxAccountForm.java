package io.graphenee.accounting.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.BeanFaultToBeanConverter;

@SpringComponent
@Scope("prototype")
public class GxAccountForm extends TRAbstractForm<GxAccountBean> {
	private static final long serialVersionUID = 1L;

	@Autowired
	GxAccountingDataService accountingDataService;

	MTextField accountCode;
	MTextField accountName;
	ComboBox gxParentAccountBeanFault;
	ComboBox gxAccountTypeBeanFault;

	BeanItemContainer<GxAccountBean> accountBeanContainer = new BeanItemContainer<>(GxAccountBean.class);
	BeanItemContainer<GxAccountTypeBean> accountTypeBeanContainer = new BeanItemContainer<>(GxAccountTypeBean.class);

	@Override
	protected void addFieldsToForm(FormLayout form) {
		accountCode = new MTextField("Account Code");
		accountCode.setRequired(true);
		accountCode.setConverter(new StringToIntegerConverter());

		accountCode.addValidator(new Validator() {

			@Override
			public void validate(Object value) throws InvalidValueException {
				if (value != null && !value.toString().isEmpty() && !value.toString().equals("0")) {
					GxAccountBean accountBean = null;
					if (getEntity().getGxNamespaceBeanFault() != null) {
						accountBean = accountingDataService.findByAccountNumberAndNamespace(Integer.parseInt(value.toString()), getEntity().getGxNamespaceBeanFault().getBean());
					} else {
						accountBean = accountingDataService.findByAccountNumber(Integer.parseInt(value.toString()));
					}
					if (accountBean != null && (getEntity().getOid() == null || accountBean.getOid().intValue() != getEntity().getOid().intValue())) {
						String message = "Account already exist against account code %s";
						message = String.format(message, value.toString());
						throw new InvalidValueException(message);
					}
				}
			}
		});

		accountName = new MTextField("Account Name").withRequired(true);

		gxParentAccountBeanFault = new ComboBox("Parent Account");
		gxParentAccountBeanFault.setNullSelectionAllowed(true);
		gxParentAccountBeanFault.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		gxParentAccountBeanFault.setItemCaptionPropertyId("accountName");
		gxParentAccountBeanFault.setContainerDataSource(accountBeanContainer);
		gxParentAccountBeanFault.setConverter(new BeanFaultToBeanConverter(GxAccountBean.class));

		gxParentAccountBeanFault.addValueChangeListener(listener -> {
			if (listener.getProperty().getValue() != null) {
				GxAccountBean accountBean = (GxAccountBean) listener.getProperty().getValue();
				if (accountBean != null) {
					gxAccountTypeBeanFault.setValue(accountBean.getGxAccountTypeBeanFault().getBean());
					gxAccountTypeBeanFault.setReadOnly(true);
				}
			} else {
				gxAccountTypeBeanFault.setReadOnly(false);
				gxAccountTypeBeanFault.setValue(null);
			}
		});

		gxAccountTypeBeanFault = new ComboBox("Account Type");
		gxAccountTypeBeanFault.setRequired(true);
		gxAccountTypeBeanFault.setNullSelectionAllowed(false);
		gxAccountTypeBeanFault.setItemCaptionPropertyId("typeName");
		gxAccountTypeBeanFault.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		gxAccountTypeBeanFault.setContainerDataSource(accountTypeBeanContainer);
		gxAccountTypeBeanFault.setConverter(new BeanFaultToBeanConverter(GxAccountBean.class));

		form.addComponents(accountCode, accountName, gxParentAccountBeanFault, gxAccountTypeBeanFault);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected void preBinding(GxAccountBean entity) {
		super.preBinding(entity);
		if (entity.getGxNamespaceBeanFault() != null)
			accountBeanContainer.addAll(accountingDataService.findAllAccountsByNamespace(entity.getGxNamespaceBeanFault().getBean()));
		else
			accountBeanContainer.addAll(accountingDataService.findAllAccounts());
		if (entity.getOid() != null && accountBeanContainer.size() > 0)
			accountBeanContainer.removeItem(entity);
		accountTypeBeanContainer.addAll(accountingDataService.findAllAccountTypes());
	}

	@Override
	protected String formTitle() {
		return "Account Form";
	}

}
