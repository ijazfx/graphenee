package io.graphenee.core.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxMobileApplicationBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.renderer.BooleanRenderer;

@SpringComponent
@Scope("prototype")
public class GxMobileApplicationListPanel extends AbstractEntityListPanel<GxMobileApplicationBean> {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxMobileApplicationForm editorForm;

	private GxNamespaceBean selectedNamespace;

	private ComboBox namespaceComboBox;

	public GxMobileApplicationListPanel() {
		super(GxMobileApplicationBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxMobileApplicationBean entity) {
		dataService.createOrUpdate(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxMobileApplicationBean entity) {
		dataService.deleteMobileApplication(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxMobileApplicationBean> fetchEntities() {
		return dataService.findAllByNamespace(selectedNamespace);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "applicationName", "isActive" };
	}

	@Override
	protected TRAbstractForm<GxMobileApplicationBean> editorForm() {
		return editorForm;
	}

	@Override
	protected void postBuild() {
		super.postBuild();
		for (com.vaadin.ui.Grid.Column column : entityGrid().getColumns()) {
			if (column.getPropertyId().toString().matches("(isActive)")) {
				column.setRenderer(new BooleanRenderer(event -> {
					GxMobileApplicationBean item = (GxMobileApplicationBean) event.getItemId();
					item.setIsActive(!item.getIsActive());
					dataService.createOrUpdate(item);
					entityGrid().refreshRow(item);
				}), BooleanRenderer.SWITCH_CONVERTER);
			}
		}
	}

	@Override
	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
		namespaceComboBox = new ComboBox("Namespace");
		List<GxNamespaceBean> gxNamespaceBeans = dataService.findNamespace();
		namespaceComboBox.addItems(gxNamespaceBeans);
		namespaceComboBox.setValue(gxNamespaceBeans.get(0));
		selectedNamespace = (GxNamespaceBean) namespaceComboBox.getValue();
		namespaceComboBox.addValueChangeListener(event -> {
			selectedNamespace = (GxNamespaceBean) event.getProperty().getValue();
			refresh();
		});

		toolbar.addComponents(namespaceComboBox);
		super.addButtonsToToolbar(toolbar);
	}

	@Override
	protected void onAddButtonClick(GxMobileApplicationBean entity) {
		if (selectedNamespace != null) {
			entity.setGxNamespaceBeanFault(BeanFault.beanFault(selectedNamespace.getOid(), selectedNamespace));
		} else {
			entity.setGxNamespaceBeanFault(null);
		}
		super.onAddButtonClick(entity);
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.selectedNamespace = namespaceBean;
		namespaceComboBox.setVisible(namespaceBean == null);
	}

	@Override
	protected boolean shouldShowDeleteConfirmation() {
		return true;
	}

}
