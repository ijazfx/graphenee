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
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@Component
@Scope("prototype")
public class GxUserAccountList extends GxAbstractEntityList<GxUserAccountBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxUserAccountForm entityForm;

	private GxNamespaceBean namespace;

	public GxUserAccountList() {
		super(GxUserAccountBean.class);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "firstName", "lastName", "username", "email", "isActive" };
	}

	@Override
	protected Stream<GxUserAccountBean> getData() {
		if (namespace == null)
			return dataService.findUserAccount().stream();
		return dataService.findUserAccountByNamespace(namespace).stream();
	}

	@Override
	protected GxAbstractEntityForm<GxUserAccountBean> getEntityForm(GxUserAccountBean entity) {
		return entityForm;
	}

	@Override
	public void onSave(GxUserAccountBean entity) {
		dataService.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxUserAccountBean> entities) {
		for (GxUserAccountBean entity : entities) {
			dataService.delete(entity);
		}
	}

	@Override
	protected void preEdit(GxUserAccountBean entity) {
		if (entity.getOid() == null) {
			if (namespace == null) {
				namespace = dataService.findSystemNamespace();
			}
			entity.setNamespaceFault(new BeanFault<>(namespace.getOid(), namespace));
		}
	}

	@Override
	protected void decorateSearchForm(FormLayout searchForm, Binder<GxUserAccountBean> searchBinder) {
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
