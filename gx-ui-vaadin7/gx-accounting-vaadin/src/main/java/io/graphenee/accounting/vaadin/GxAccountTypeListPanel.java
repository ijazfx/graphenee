package io.graphenee.accounting.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxAccountTypeListPanel extends AbstractEntityListPanel<GxAccountTypeBean> {

	@Autowired
	GxAccountingDataService gxAccountingDataService;

	@Autowired
	GxAccountTypeForm form;

	public GxAccountTypeListPanel() {
		super(GxAccountTypeBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxAccountTypeBean entity) {
		gxAccountingDataService.createOrUpdate(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxAccountTypeBean entity) {
		gxAccountingDataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxAccountTypeBean> fetchEntities() {
		return gxAccountingDataService.findAllAccountTypes();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "typeName", "typeCode" };
	}

	@Override
	protected TRAbstractForm<GxAccountTypeBean> editorForm() {
		return form;
	}

	@Override
	protected boolean shouldShowDeleteConfirmation() {
		return true;
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
	}

}
