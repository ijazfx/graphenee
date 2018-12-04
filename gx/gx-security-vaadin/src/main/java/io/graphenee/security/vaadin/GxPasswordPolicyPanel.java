package io.graphenee.security.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxPasswordPolicyBean;
import io.graphenee.security.api.GxPasswordPolicyDataService;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.renderer.BooleanRenderer;

@SpringComponent
@Scope("prototype")
public class GxPasswordPolicyPanel extends AbstractEntityListPanel<GxPasswordPolicyBean> {

	@Autowired
	GxPasswordPolicyForm editorForm;
	@Autowired
	GxPasswordPolicyDataService dataService;
	private GxNamespaceBean namespaceBean;

	public GxPasswordPolicyPanel() {
		super(GxPasswordPolicyBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxPasswordPolicyBean entity) {
		dataService.createOrUpdate(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxPasswordPolicyBean entity) {
		dataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected void postBuild() {
		super.postBuild();
		for (com.vaadin.ui.Grid.Column column : entityGrid().getColumns()) {
			if (column.getPropertyId().toString().matches("(isActive)")) {
				column.setRenderer(new BooleanRenderer(event -> {
					GxPasswordPolicyBean item = (GxPasswordPolicyBean) event.getItemId();
					item.setIsActive(!item.getIsActive());
					dataService.createOrUpdate(item);
					entityGrid().refreshRow(item);
				}), BooleanRenderer.SWITCH_CONVERTER);
			}
		}
	}

	@Override
	protected List<GxPasswordPolicyBean> fetchEntities() {
		return dataService.findAllPasswordPolicyByNamespace(namespaceBean);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "passwordPolicyName", "isActive" };
	}

	@Override
	protected TRAbstractForm<GxPasswordPolicyBean> editorForm() {
		return editorForm;
	}

	@Override
	protected void onAddButtonClick(GxPasswordPolicyBean entity) {
		// TODO Auto-generated method stub
		super.onAddButtonClick(entity);
		entity.setGxNamespaceBeanFault(new BeanFault<Integer, GxNamespaceBean>(namespaceBean.getOid(), namespaceBean));
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
	}

	@Override
	protected boolean shouldShowDeleteConfirmation() {
		return true;
	}
}
