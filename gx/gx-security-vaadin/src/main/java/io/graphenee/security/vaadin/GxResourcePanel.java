package io.graphenee.security.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxResourceBean;
import io.graphenee.security.api.GxSecurityDataService;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxResourcePanel extends AbstractEntityListPanel<GxResourceBean> {

	private GxNamespaceBean namespace;
	@Autowired
	GxSecurityDataService gxSecurityDataService;
	@Autowired
	GxResourcesForm editorForm;

	public GxResourcePanel() {
		super(GxResourceBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxResourceBean entity) {
		gxSecurityDataService.createOrUpdate(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxResourceBean entity) {
		gxSecurityDataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<GxResourceBean> fetchEntities() {
		return null;
	}

	@Override
	protected <F> List<GxResourceBean> fetchEntities(F filter) {
		namespace = (GxNamespaceBean) filter;
		return gxSecurityDataService.findResourceByNamespace(namespace);
	}

	@Override
	protected String[] visibleProperties() {
		// TODO Auto-generated method stub
		return new String[] { "resourceName", "resourceDescription", "isActive" };
	}

	@Override
	protected TRAbstractForm<GxResourceBean> editorForm() {
		// TODO Auto-generated method stub
		return editorForm;
	}

	@Override
	protected void onAddButtonClick(GxResourceBean entity) {
		if (namespace != null) {
			entity.setGxNamespaceBeanFault(BeanFault.beanFault(namespace.getOid(), namespace));
		}
		super.onAddButtonClick(entity);
	}

	@Override
	protected boolean shouldShowDeleteConfirmation() {
		// TODO Auto-generated method stub
		return true;
	}
}
