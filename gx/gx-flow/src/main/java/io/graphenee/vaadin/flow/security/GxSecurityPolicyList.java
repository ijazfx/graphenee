package io.graphenee.vaadin.flow.security;

import java.util.Collection;
import java.util.stream.Stream;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@Component
@Scope("prototype")
public class GxSecurityPolicyList extends GxAbstractEntityList<GxSecurityPolicyBean> {
	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxSecurityPolicyForm securityPolicyForm;

	private GxNamespaceBean namespace;

	public GxSecurityPolicyList() {
		super(GxSecurityPolicyBean.class);
	}

	@Override
	protected Stream<GxSecurityPolicyBean> getData() {
		if (namespace == null)
			return dataService.findSecurityPolicy().stream();
		return dataService.findSecurityPolicyByNamespace(namespace).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "securityPolicyName", "priority", "isActive" };
	}

	@Override
	protected GxAbstractEntityForm<GxSecurityPolicyBean> getEntityForm(GxSecurityPolicyBean entity) {
		return securityPolicyForm;
	}

	@Override
	protected void onSave(GxSecurityPolicyBean entity) {
		dataService.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxSecurityPolicyBean> entities) {
		for (GxSecurityPolicyBean entity : entities) {
			dataService.delete(entity);
		}
	}

	protected void preEdit(GxSecurityPolicyBean entity) {
		if (entity.getOid() == null) {
			if (namespace == null) {
				namespace = dataService.findSystemNamespace();
			}
			entity.setNamespaceFault(new BeanFault<>(namespace.getOid(), namespace));
		}
	}

	@Override
	protected void decorateSearchForm(FormLayout searchForm, Binder<GxSecurityPolicyBean> searchBinder) {
		ComboBox<GxNamespaceBean> namespaceComboBox = new ComboBox<>("Namespace");
		namespaceComboBox.setItemLabelGenerator(GxNamespaceBean::getNamespace);
		namespaceComboBox.setClearButtonVisible(true);
		namespaceComboBox.setItems(dataService.findNamespace());
		namespaceComboBox.setValue(namespace);

		namespaceComboBox.addValueChangeListener(vcl -> {
			namespace = vcl.getValue();
			refresh();
		});

		searchForm.add(namespaceComboBox);
	}

	public void initializeWithNamespace(GxNamespaceBean namespace) {
		this.namespace = namespace;
		refresh();
	}

}
