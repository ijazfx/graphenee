package io.graphenee.security.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccessKeyBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.BeanCollectionFaultToSetConverter;

@SpringComponent
@Scope("prototype")
public class GxAccessKeyForm extends TRAbstractForm<GxAccessKeyBean> {
	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;
	ComboBox typeStatus;
	MLabel key, secret;
	MCheckBox isActive;

	TwinColSelect securityGroupCollectionFault;
	TwinColSelect securityPolicyCollectionFault;

	private GxNamespaceBean namespaceBean;

	@Override
	protected Component getFormComponent() {
		MFormLayout form = new MFormLayout().withStyleName(ValoTheme.FORMLAYOUT_LIGHT).withMargin(false);
		key = new MLabel().withCaption("key");
		secret = new MLabel().withCaption("secret");
		typeStatus = new ComboBox("type");
		typeStatus.setRequired(true);
		isActive = new MCheckBox("Is Active?");

		form.addComponents(key, secret, typeStatus, isActive);

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
	protected void postBinding(GxAccessKeyBean entity) {
		key.setValue(entity.getKey().toString());
		secret.setValue(entity.getSecret().toString());
		typeStatus.addItems("RETINA", "FINGERPRINT", "CARD");
		List<GxSecurityGroupBean> securityGroups = namespaceBean != null ? dataService.findSecurityGroupByNamespace(namespaceBean) : dataService.findSecurityGroup();
		securityGroupCollectionFault.addItems(securityGroups);

		List<GxSecurityPolicyBean> securityPolicies = namespaceBean != null ? dataService.findSecurityPolicyByNamespace(namespaceBean) : dataService.findSecurityPolicy();
		securityPolicyCollectionFault.addItems(securityPolicies);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Access Key";
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
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
