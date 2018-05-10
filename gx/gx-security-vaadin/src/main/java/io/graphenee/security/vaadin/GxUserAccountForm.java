package io.graphenee.security.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.BeanCollectionFaultToSetConverter;

@SpringComponent
@Scope("prototype")
public class GxUserAccountForm extends TRAbstractForm<GxUserAccountBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	MTextField username;
	MTextField firstName;
	MTextField lastName;
	MTextField fullNameNative;
	MTextField email;
	MPasswordField password;
	MCheckBox isLocked;
	MCheckBox isActive;
	MCheckBox isPasswordChangeRequired;

	TwinColSelect securityGroupCollectionFault;
	TwinColSelect securityPolicyCollectionFault;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Component getFormComponent() {
		// detail form
		MFormLayout form = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT).withMargin(false);
		username = new MTextField("Username").withRequired(true);
		firstName = new MTextField("First Name").withRequired(false);
		lastName = new MTextField("Last Name").withRequired(false);
		fullNameNative = new MTextField("Full Name (Native)").withRequired(false);
		email = new MTextField("Email").withRequired(false);
		password = new MPasswordField("Password").withRequired(false);
		isPasswordChangeRequired = new MCheckBox("Password Change Required?");
		isLocked = new MCheckBox("Is Locked?");
		isActive = new MCheckBox("Is Active?");
		form.addComponents(username, firstName, lastName, fullNameNative, email, password, isPasswordChangeRequired, isLocked, isActive);

		// security groups
		securityGroupCollectionFault = new TwinColSelect();
		securityGroupCollectionFault.setConverter((Converter) new BeanCollectionFaultToSetConverter<GxSecurityGroupBean>());
		securityGroupCollectionFault.setSizeFull();
		securityGroupCollectionFault.setLeftColumnCaption("Available");
		securityGroupCollectionFault.setRightColumnCaption("Assigned");

		// security policies
		securityPolicyCollectionFault = new TwinColSelect();
		securityPolicyCollectionFault.setConverter((Converter) new BeanCollectionFaultToSetConverter<GxSecurityPolicyBean>());
		securityPolicyCollectionFault.setSizeFull();
		securityPolicyCollectionFault.setLeftColumnCaption("Available");
		securityPolicyCollectionFault.setRightColumnCaption("Applied");

		TabSheet mainTabSheet = new TabSheet();
		mainTabSheet.setStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
		mainTabSheet.setWidth("100%");
		mainTabSheet.setHeight("100%");

		mainTabSheet.addTab(form, "Details");
		mainTabSheet.addTab(securityGroupCollectionFault, "Security Groups");
		mainTabSheet.addTab(securityPolicyCollectionFault, "Security Policies");

		MVerticalLayout layout = new MVerticalLayout(mainTabSheet);
		layout.setSizeFull();
		return layout;
	}

	@Override
	protected void postBinding(GxUserAccountBean entity) {
		GxNamespaceBean namespace = null;
		if (entity.getNamespaceFault() != null) {
			namespace = entity.getNamespaceFault().getBean();
		}
		List<GxSecurityGroupBean> securityGroups = namespace != null ? dataService.findSecurityGroupByNamespace(namespace) : dataService.findSecurityGroup();
		securityGroupCollectionFault.addItems(securityGroups);

		List<GxSecurityPolicyBean> securityPolicies = namespace != null ? dataService.findSecurityPolicyByNamespace(namespace) : dataService.findSecurityPolicy();
		securityPolicyCollectionFault.addItems(securityPolicies);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "User Account";
	}

	@Override
	protected String popupHeight() {
		return "400px";
	}

	@Override
	protected String popupWidth() {
		return "550px";
	}

}
