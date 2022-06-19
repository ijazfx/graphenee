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
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@Component
@Scope("prototype")
public class GxSecurityGroupList extends GxAbstractEntityList<GxSecurityGroupBean> {
	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxSecurityGroupForm entityForm;

	private GxNamespaceBean namespace;

	public GxSecurityGroupList() {
		super(GxSecurityGroupBean.class);
	}

	@Override
	protected Stream<GxSecurityGroupBean> getData() {
		if (namespace == null)
			return dataService.findSecurityGroup().stream();
		return dataService.findSecurityGroupByNamespace(namespace).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "securityGroupName", "priority", "isActive" };
	}

	@Override
	protected GxAbstractEntityForm<GxSecurityGroupBean> getEntityForm(GxSecurityGroupBean entity) {
		return entityForm;
	}

	@Override
	protected void onSave(GxSecurityGroupBean entity) {
		dataService.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxSecurityGroupBean> entities) {
		for (GxSecurityGroupBean entity : entities) {
			dataService.delete(entity);
		}
	}

	@Override
	protected void preEdit(GxSecurityGroupBean entity) {
		if (entity.getOid() == null) {
			if (namespace == null) {
				namespace = dataService.findSystemNamespace();
			}
			entity.setNamespaceFault(new BeanFault<>(namespace.getOid(), namespace));
		}
	}

	@Override
	protected void decorateSearchForm(FormLayout searchForm, Binder<GxSecurityGroupBean> searchBinder) {
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
